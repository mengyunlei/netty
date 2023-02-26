package tuling.netty.danmu;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class DanmuServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 当客户端连接服务器完成就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
        channelGroup.writeAndFlush("[ 客户端 ]:有人上线了"+channel.remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[ 客户端 ]:有人下线了"+channel.remoteAddress());
        channelGroup.remove(channel);
    }

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象, 含有通道channel，管道pipeline
     * @param msg 就是客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Channel income = ctx.channel();
        String content = msg.text();
        for(Channel channel:channelGroup){
            if(income != channel){
                channel.writeAndFlush(new TextWebSocketFrame("[ 其他人 ]:"+content));
            }else{
                channel.writeAndFlush(new TextWebSocketFrame("[ 我 ]:"+content));
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        channelGroup.remove(ctx.channel());
    }
}

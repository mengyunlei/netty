package tuling.netty.danmu;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class DanmuServer {
     public static void main(String[] args) throws Exception {
         EventLoopGroup bossGroup = new NioEventLoopGroup(1);
         EventLoopGroup workerGroup = new NioEventLoopGroup(8);
         try{

             ServerBootstrap bootstrap = new ServerBootstrap();
             bootstrap.group(bossGroup,workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel channel) throws Exception {
                             ChannelPipeline pipeline = channel.pipeline();
                             pipeline.addLast(new HttpServerCodec());
                             pipeline.addLast(new ChunkedWriteHandler());
                             pipeline.addLast(new HttpObjectAggregator(1024*64));
                             pipeline.addLast(new IdleStateHandler(8, 10, 12));
                             pipeline.addLast(new WebSocketServerProtocolHandler("/myl"));
                             pipeline.addLast("client",new DanmuServerHandler());
                         }
                     });
             ChannelFuture future = bootstrap.bind(9000).sync();
             future.channel().closeFuture().sync();
         }finally{
             bossGroup.shutdownGracefully();
             workerGroup.shutdownGracefully();
         }


     }
}

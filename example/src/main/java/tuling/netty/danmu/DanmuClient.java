package tuling.netty.danmu;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class DanmuClient {
     public static void main(String[] args) throws Exception {
         EventLoopGroup workerGroup = new NioEventLoopGroup(8);
         try{

             Bootstrap bootstrap = new Bootstrap();
             bootstrap.group(workerGroup)
                     .channel(NioSocketChannel.class)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel channel) throws Exception {
                             ChannelPipeline pipeline = channel.pipeline();
                             pipeline.addLast("decoder",new StringDecoder());
                             pipeline.addLast("encoder",new StringEncoder());
                             pipeline.addLast("client",new DanmuClientHandler());
                         }
                     });
             ChannelFuture future = bootstrap.connect("127.0.0.1",9000).sync();
             Channel channel = future.channel();
             Scanner scanner = new Scanner(System.in);
             while (scanner.hasNextLine()) {
                 String msg = scanner.nextLine();
                 //通过 channel 发送到服务器端
                 channel.writeAndFlush(msg);
             }
         }finally{
             workerGroup.shutdownGracefully();
         }


     }
}

import com.talkingdata.netty.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.AttributeKey;

import java.util.Random;
import java.util.UUID;

public class HttpServer {
	public static void main(String[] args) throws InterruptedException {

		ServerBootstrap bootstrap = new ServerBootstrap();

		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup worker = new NioEventLoopGroup(1);

		try {
			bootstrap
					.group(boss, worker)
					.option(ChannelOption.SO_BACKLOG, 1)
					.childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1)
                    .childOption(ChannelOption.SO_TIMEOUT, 1)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel channel) throws Exception {
							channel.pipeline().addLast(new HttpRequestDecoder());
							channel.pipeline().addLast(new HttpResponseEncoder());
							//post不加这个不能用
							channel.pipeline().addLast(new HttpObjectAggregator(1048576));
							channel.pipeline().addLast(new HttpHandler());
						}

                        /**
                         * Handle the {@link Throwable} by logging and closing the {@link Channel}. Sub-classes may override this.
                         *
                         * @param ctx
                         * @param cause
                         */
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            cause.printStackTrace();
                            super.exceptionCaught(ctx, cause);
                        }
                    });

			Channel channel = bootstrap.bind(8000).sync().channel();
			channel.closeFuture().sync();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}

	}
}

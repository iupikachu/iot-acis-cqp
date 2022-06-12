package com.cqp.iot.bootstrap;


import com.cqp.iot.properties.InitBean;
import com.cqp.iot.utils.IPUtils;
import com.cqp.iot.utils.OSUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cqp
 * @version 1.0.0
 * @ClassName NettyBootstrapServer.java
 * @Description  netty 服务启动类
 * @createTime 2022年05月16日 16:21:00
 */

@Slf4j
public class NettyBootstrapServer extends AbstractBootstrapServer{

    private InitBean serverBean;

    public NettyBootstrapServer() {}

    public InitBean getServerBean() {
        return serverBean;
    }

    @Override
    public void setServerBean(InitBean serverBean) {
        this.serverBean = serverBean;
    }


    public NettyBootstrapServer(InitBean serverBean) {
        this.serverBean = serverBean;
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    ServerBootstrap bootstrap = null;
    Channel channel = null;

    /**
     * 服务开启
     */
    @Override
    public void start() {

        initEventPool();

        bootstrap.group(bossGroup, workGroup)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, serverBean.isReuseaddr())
                .option(ChannelOption.SO_BACKLOG, serverBean.getBacklog())
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // 初始化各种 Handler
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initHandler(ch.pipeline(), serverBean);
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, serverBean.isTcpNodelay())
                .childOption(ChannelOption.SO_KEEPALIVE, serverBean.isKeepalive())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

                try{

                    channel = bootstrap.bind(serverBean.getPort()).addListener(
                            channelFuture -> {
                                if(channelFuture.isSuccess()){

                                    log.info("服务端启动成功：【" + IPUtils.getHost() + ":" + serverBean.getPort() + "】");
                                }
                                else{
                                    log.info("服务端启动失败：【" + IPUtils.getHost() + ":" + serverBean.getPort() + "】");
                                }
                            }
                    ).sync().channel(); // 主线程阻塞住，释放cpu使用权，等待 eventGroup 线程建立完连接将其唤醒


                    channel.closeFuture().sync(); // 主线程阻塞住，等待channel.close事件完成将其唤醒，不然会直接执行 finally 的关闭代码
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }

    }

    /**
     * 初始化 EventPool 参数
     */
    public void initEventPool(){

        log.info(("EventPool 开始初始化"));

        bootstrap = new ServerBootstrap();

        if(useEpoll()){
            bossGroup = new EpollEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger bossNumber = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_BOSS_" + bossNumber.incrementAndGet());
                }
            });

            workGroup = new EpollEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger workNumber = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_WORK_" + workNumber.incrementAndGet());
                }
            });
        }else{
            bossGroup = new NioEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger bossNumber = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "BOSS_" + bossNumber.incrementAndGet());
                }
            });

            workGroup = new NioEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger workNumber = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "WORK_" + workNumber.incrementAndGet());
                }
            });

            log.info(("EventPool 初始化结束"));
        }
    }


    /**
     * 关闭资源
     */
    @Override
    public void shutdown() {
        try{
            channel.close();
        }catch (Exception e){
            throw new RuntimeException("cqp-iot 正常关闭失败",e);
        }
    }

    /**
     * 能否使用 Linux 的 epoll
     * @return
     */
    private boolean useEpoll(){
        return OSUtils.isLinuxPlatform() && Epoll.isAvailable();
    }

}

package com.github.billy.gateway.inbound;

import com.github.billy.gateway.ProxyServerOption;
import com.github.billy.gateway.inbound.HttpInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.ArrayList;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private ProxyServerOption proxyServerOption;

    public HttpInboundInitializer(ProxyServerOption option) {
        this.proxyServerOption = option;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
        p.addLast(new HttpServerCodec());
        //p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(new HttpObjectAggregator(1024 * 1024));

        p.addLast(new HttpInboundHandler(this.proxyServerOption));
    }
}

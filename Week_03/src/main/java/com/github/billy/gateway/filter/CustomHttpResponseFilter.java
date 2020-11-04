package com.github.billy.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

public class CustomHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse fullHttpResponse, ChannelHandlerContext ctx) {
        fullHttpResponse.headers().set("key","test");
    }
}

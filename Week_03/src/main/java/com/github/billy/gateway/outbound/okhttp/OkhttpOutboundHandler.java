package com.github.billy.gateway.outbound.okhttp;

import com.github.billy.gateway.ProxyServerOption;
import com.github.billy.gateway.filter.CustomHttpRequestFilter;
import com.github.billy.gateway.filter.CustomHttpResponseFilter;
import com.github.billy.gateway.filter.HttpRequestFilter;
import com.github.billy.gateway.filter.HttpResponseFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {

    private CloseableHttpAsyncClient httpclient;
    private OkHttpClient okHttpClient;
    private ExecutorService proxyService;


    private ProxyServerOption proxyServerOption;

    private ArrayList<HttpResponseFilter> filters;

    public OkhttpOutboundHandler(ProxyServerOption option) {
        //this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        this.proxyServerOption = option;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();

        this.proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new com.github.billy.gateway.outbound.httpclient4.NamedThreadFactory("proxyService"), handler);

        //初始化OKHttpClient
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.newBuilder().connectTimeout(1000, TimeUnit.MILLISECONDS).build();

        this.filters = new ArrayList<>();
        this.filters.add(new CustomHttpResponseFilter());
    }


    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        try {
            final String url = this.getProxyServerUrl() + fullRequest.uri();
            HttpHeaders headers = fullRequest.headers();
            System.out.println("header.nio===>" + headers.get("nio"));
            proxyService.submit(() -> fetchGet(fullRequest, ctx, url));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught(ctx, e);
        }
    }
    private String getProxyServerUrl() throws Exception {
        if (this.proxyServerOption == null) {
            throw new Exception("proxyServerOption == null");
        }
        //随机
        Random random = new Random();
        ProxyServerOption.ProxyServer proxyServer = this.proxyServerOption.getProxyServer().get(random.nextInt(this.proxyServerOption.getProxyServer().size()));
        String url = proxyServer.getUrl();
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        System.out.println("GetBackendUrl 通过随机获取地址========>" + url);
        return url;
    }
    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        Request request = new Request.Builder()
                .url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(inbound, ctx, response);
            }
        });
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final Response resultResponse) {
        FullHttpResponse response = null;
        try {
            String body = resultResponse.body().string();
            System.out.println("resultResponse =====>" + body);

            body += "【 数据来源接口:" + resultResponse.request().url() + " 】";
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body.getBytes()));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", body.getBytes().length);
            System.out.println("resultResponse =====>" + body.getBytes().length);

            FullHttpResponse finalResponse = response;
            this.filters.forEach(item -> item.filter(finalResponse, ctx));
            response = finalResponse;
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();

        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

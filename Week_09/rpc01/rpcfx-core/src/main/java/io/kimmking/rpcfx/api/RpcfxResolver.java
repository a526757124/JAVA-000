package io.kimmking.rpcfx.api;

import org.springframework.beans.BeansException;

public interface RpcfxResolver {

    <T> T resolve(Class<T> serviceClass) throws BeansException;

}

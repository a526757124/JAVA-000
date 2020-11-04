package com.github.billy.gateway;

import java.util.ArrayList;

public class ProxyServerOption {
    private ArrayList<ProxyServer> proxyServers;
    public  ProxyServerOption(){
        this.proxyServers=new ArrayList<>();
    }
    public  ArrayList<ProxyServer> getProxyServer(){
        return  proxyServers;
    }
    public void addProxyServer(String proxyServerUrl, int prot) {
        this.proxyServers.add(new ProxyServer(proxyServerUrl,prot));
    }

    public class ProxyServer {
        String proxyServerUrl;
        int port;
        public ProxyServer(String proxyServerUrl,int port){
            this.proxyServerUrl=proxyServerUrl;
            this.port=port;
        }
        public String getUrl(){
            return  this.proxyServerUrl+":"+this.port;
        }
    }
}



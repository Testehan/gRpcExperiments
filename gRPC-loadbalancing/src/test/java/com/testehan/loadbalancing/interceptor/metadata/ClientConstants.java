package com.testehan.loadbalancing.interceptor.metadata;

import io.grpc.Metadata;

public class ClientConstants {

    private static final Metadata METADATA = new Metadata();

    static {
        // THIS will work because "some-bank-secret-token" is what the server expects
//        METADATA.put(Metadata.Key.of("client-token",Metadata.ASCII_STRING_MARSHALLER), "some-bank-secret-token");

        // this will not work as we send a different token than what server expects
        METADATA.put(Metadata.Key.of("client-token",Metadata.ASCII_STRING_MARSHALLER), "ciuciucaaa");
    }

    public static Metadata getClientToken(){
        return METADATA;
    }
}

package com.testehan.loadbalancing.server.interceptor.metadata;

import io.grpc.Metadata;

public class ServerConstants {

    // this key corresponds to the key used to set the value on the client..
    public static final Metadata.Key<String> TOKEN = Metadata.Key.of("client-token",Metadata.ASCII_STRING_MARSHALLER);

}

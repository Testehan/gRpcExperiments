package com.testehan.loadbalancing.server.interceptor.metadata.constants;

import io.grpc.Context;
import io.grpc.Metadata;

public class ServerConstants {

    // this key corresponds to the key used to set the value on the client..
    public static final Metadata.Key<String> TOKEN = Metadata.Key.of("client-token",Metadata.ASCII_STRING_MARSHALLER);

    public static final Metadata.Key<String> USER_TOKEN = Metadata.Key.of("user-token",Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<UserRole> CONTEXT_USER_ROLE = Context.key("user-role");
}

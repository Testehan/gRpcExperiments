package com.testehan.loadbalancing.interceptor.metadata;

import com.testehan.models.ex08.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.protobuf.ProtoUtils;

public class ClientConstants {

    private static final Metadata METADATA = new Metadata();
    public static final Metadata.Key<String> USER_TOKEN = Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<WithdrawalError> WITHDRAWAL_ERROR_KEY = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());

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

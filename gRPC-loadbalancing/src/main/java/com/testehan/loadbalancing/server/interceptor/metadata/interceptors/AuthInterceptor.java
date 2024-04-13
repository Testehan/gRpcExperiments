package com.testehan.loadbalancing.server.interceptor.metadata.interceptors;

import com.testehan.loadbalancing.server.interceptor.metadata.constants.ServerConstants;
import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String token = metadata.get(ServerConstants.TOKEN);
        if (validate(token)){
            return serverCallHandler.startCall(serverCall,metadata);
        } else {
            Status status = Status.UNAUTHENTICATED.withDescription("invalid/expired token");
            serverCall.close(status,metadata);
        }
        // if this line is reached, it means the call was unauthenticated..
        // instead of null an empty obj is returned
        return new ServerCall.Listener<ReqT>() {};
    }

    private boolean validate(String token){
        // this value is set on the client when making the call
        return Objects.nonNull(token) && token.equals("some-bank-secret-token");
    }
}

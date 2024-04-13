package com.testehan.loadbalancing.server.interceptor.metadata;

import io.grpc.*;

import java.util.Objects;

public class UserTokenInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String token = metadata.get(ServerConstants.USER_TOKEN);
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
        return Objects.nonNull(token) && token.equals("29fad4bf-4943-41e1-bfd6-fca0793d97db");
    }
}

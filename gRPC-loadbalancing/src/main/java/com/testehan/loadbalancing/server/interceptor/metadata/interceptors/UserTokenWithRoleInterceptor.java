package com.testehan.loadbalancing.server.interceptor.metadata.interceptors;

import com.testehan.loadbalancing.server.interceptor.metadata.constants.ServerConstants;
import com.testehan.loadbalancing.server.interceptor.metadata.constants.UserRole;
import io.grpc.*;

import java.util.Objects;

public class UserTokenWithRoleInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String token = metadata.get(ServerConstants.USER_TOKEN);
        if (validate(token)){
            UserRole userRole = extractUserRole(token);
            var context = Context.current().withValue(ServerConstants.CONTEXT_USER_ROLE, userRole);

            return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
//            return serverCallHandler.startCall(serverCall,metadata);
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
        return Objects.nonNull(token) &&
                (token.startsWith("10d118f5-fe18-4d4a-94f5-e8de3877056e")
                    || token.startsWith("2695d56a-eaf7-4bbc-a5ae-020c5a691405"));
    }

    private UserRole extractUserRole(String token){
        return token.endsWith("prime") ? UserRole.PRIME : UserRole.STANDARD;
    }
}

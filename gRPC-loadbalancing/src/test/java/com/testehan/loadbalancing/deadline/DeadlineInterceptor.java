package com.testehan.loadbalancing.deadline;

import io.grpc.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DeadlineInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        Deadline deadline = callOptions.getDeadline();
        if(Objects.isNull(deadline)){   // the default deadline is 4 seconds...but if a custom one is set, that will be used
            callOptions = callOptions.withDeadline(Deadline.after(4, TimeUnit.SECONDS));
        }

        System.out.println("Inside DeadlineInterceptor");

        // forward the request
        return channel.newCall(methodDescriptor, callOptions);
    }
}

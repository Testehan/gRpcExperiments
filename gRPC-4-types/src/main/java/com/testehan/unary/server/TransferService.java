package com.testehan.unary.server;

import com.testehan.models.ex08.TransferRequest;
import com.testehan.models.ex08.TransferResponse;
import com.testehan.models.ex08.TransferServiceGrpc;
import com.testehan.unary.server.request.TransferStreamingRequest;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}

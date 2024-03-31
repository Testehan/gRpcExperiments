package com.testehan.unary.client;

import com.testehan.models.ex08.BankServiceGrpc;
import com.testehan.models.ex08.TransferRequest;
import com.testehan.models.ex08.TransferServiceGrpc;
import com.testehan.unary.server.TransferService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext().build();

        // this is Asynch
        this.transferServiceStub = TransferServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void transfer() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        TransferStreamingResponse transferResponse = new TransferStreamingResponse(countDownLatch);

        StreamObserver<TransferRequest> requestStreamObserver = this.transferServiceStub.transfer(transferResponse);

        for (int i = 0 ; i <5; i++) {
            var transferRequest = TransferRequest.newBuilder().setFromAccount(8).setToAccount(5).setAmount(10).build();
            requestStreamObserver.onNext(transferRequest);
        }

        requestStreamObserver.onCompleted();
        countDownLatch.await();
    }

}

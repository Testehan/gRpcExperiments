package com.testehan.unary.client;

import com.testehan.models.ex08.TransferResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TransferStreamingResponse implements StreamObserver<TransferResponse> {

    private CountDownLatch countDownLatch;

    public TransferStreamingResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(TransferResponse transferResponse) {
        System.out.println("Transfer status: " + transferResponse.getStatus());
        System.out.println("From account " +transferResponse.getFromAccount());
        System.out.println("To account " +transferResponse.getToAccount());
        System.out.println("-----------------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        countDownLatch.countDown();
        System.out.println("All transfers done");
    }
}

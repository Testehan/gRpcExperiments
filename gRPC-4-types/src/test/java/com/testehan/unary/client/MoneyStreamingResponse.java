package com.testehan.unary.client;

import com.testehan.models.ex08.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {

    private CountDownLatch countDownLatch;

    public MoneyStreamingResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("recieved asynch " + money.getAmount());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());

        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Done !!!");
        countDownLatch.countDown();
    }
}

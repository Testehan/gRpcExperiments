package com.testehan.loadbalancing.interceptor.metadata;

import com.testehan.models.ex08.Money;
import com.testehan.models.ex08.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.Status;
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
        Metadata metadata = Status.trailersFromThrowable(throwable);
        WithdrawalError withdrawalError = metadata.get(ClientConstants.WITHDRAWAL_ERROR_KEY);

        System.out.println(withdrawalError.getErrorMessage() + " " + withdrawalError.getAmount());

        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Done !!!");
        countDownLatch.countDown();
    }
}

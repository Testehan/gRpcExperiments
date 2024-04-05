package com.testehan.loadbalancing.serverside;

import com.testehan.models.ex08.AccountBalance;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class BalanceStreamObserver implements StreamObserver<AccountBalance> {

    private CountDownLatch countDownLatch;

    public BalanceStreamObserver(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(AccountBalance accountBalance) {
        System.out.println("Final balance " + accountBalance.getBalance());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        System.out.println("Server is Done");
    }
}

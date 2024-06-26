package com.testehan.loadbalancing.server.request;

import com.testehan.models.ex08.AccountBalance;
import com.testehan.models.ex08.DepositRequest;
import com.testehan.loadbalancing.server.repository.AccountRepository;
import io.grpc.stub.StreamObserver;

public class CashStreamingRequest implements StreamObserver<DepositRequest> {

    private StreamObserver<AccountBalance> accountBalanceStreamObserver;
    private int accountBalance = 0;

    public CashStreamingRequest(StreamObserver<AccountBalance> accountBalanceStreamObserver) {
        this.accountBalanceStreamObserver = accountBalanceStreamObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        var accountNr = depositRequest.getAccountNumber();
        var amount = depositRequest.getMoney().getAmount();
        System.out.println("Recieved " + amount + " for account number " + accountNr);
        accountBalance = AccountRepository.addAmount(accountNr,amount);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        var accountBalance = AccountBalance.newBuilder().setBalance(this.accountBalance).build();
        accountBalanceStreamObserver.onNext(accountBalance);
        accountBalanceStreamObserver.onCompleted();
        System.out.println("Done -> server finished adding all amounts");
    }
}

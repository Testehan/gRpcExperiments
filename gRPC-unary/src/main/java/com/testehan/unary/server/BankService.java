package com.testehan.unary.server;

import com.testehan.models.ex08.AccountBalance;
import com.testehan.models.ex08.BalanceCheckRequest;
import com.testehan.models.ex08.BankServiceGrpc;
import com.testehan.unary.server.repository.AccountRepository;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase{

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        System.out.println("Got a request for acc number : " + accountNumber);

        // some dummy value
        var accountBalance = AccountBalance.newBuilder()
                .setBalance(AccountRepository.getBalance(accountNumber))
                .setAccountNumber(accountNumber)
                .build();
        // set the value to be returned
        responseObserver.onNext(accountBalance);

        // mark operation as completed
        responseObserver.onCompleted();
    }
}

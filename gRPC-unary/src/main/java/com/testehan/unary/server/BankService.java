package com.testehan.unary.server;

import com.testehan.models.ex08.*;
import com.testehan.unary.server.repository.AccountRepository;
import com.testehan.unary.server.request.CashStreamingRequest;
import io.grpc.Status;
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

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var amount = request.getAmount();   // for this example we assume that this number is a multiple of 10
        System.out.println("Got a withdraw request for acc number : " + accountNumber);

        var balance = AccountRepository.getBalance(accountNumber);
        if (balance < amount) {
            Status notEnoughtMoneyToWithdraw = Status.FAILED_PRECONDITION.withDescription("Not enought money to withdraw");
            responseObserver.onError(notEnoughtMoneyToWithdraw.asRuntimeException());
            return;
        }

        for (int i = 0; i< amount / 10; i++){   // because we send back in 10 dollar increments
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            AccountRepository.deductAmount(accountNumber,10);
            try {
                System.out.println("Sending 10 money !");
                Thread.sleep(2000);     // for testing purposes :)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // mark operation as completed
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<AccountBalance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }
}

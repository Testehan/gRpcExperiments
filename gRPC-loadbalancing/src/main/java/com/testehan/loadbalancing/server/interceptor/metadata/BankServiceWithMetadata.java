package com.testehan.loadbalancing.server.interceptor.metadata;

import com.google.common.util.concurrent.Uninterruptibles;
import com.testehan.loadbalancing.server.interceptor.metadata.constants.ServerConstants;
import com.testehan.loadbalancing.server.interceptor.metadata.constants.UserRole;
import com.testehan.loadbalancing.server.repository.AccountRepository;
import com.testehan.loadbalancing.server.request.CashStreamingRequest;
import com.testehan.models.ex08.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class BankServiceWithMetadata extends BankServiceGrpc.BankServiceImplBase{

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        System.out.println("Got a request for acc number : " + accountNumber);

        var amount = AccountRepository.getBalance(accountNumber);

        // this ServerConstants.CONTEXT_USER_ROLE.get() is something like a ThreadLocal..
        UserRole userRole = ServerConstants.CONTEXT_USER_ROLE.get();
        amount = UserRole.PRIME.equals(userRole) ? amount : (amount - 5);      // users with PRIME role don't have commissions...others do :(

        var accountBalance = AccountBalance.newBuilder()
                .setBalance(amount)
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
            Status notEnoughtMoneyToWithdraw = Status.FAILED_PRECONDITION.withDescription("Not enough money to withdraw");
            responseObserver.onError(notEnoughtMoneyToWithdraw.asRuntimeException());
            return;
        }

        for (int i = 0; i< amount / 10; i++){   // because we send back in 10 dollar increments
            var money = Money.newBuilder().setAmount(10).build();
            if(!Context.current().isCancelled()){
                responseObserver.onNext(money);
                System.out.println("Delivered $10");
                AccountRepository.deductAmount(accountNumber, 10);
            }else{
                System.out.println("withdraw operation not performed because of timeout");
                break;
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

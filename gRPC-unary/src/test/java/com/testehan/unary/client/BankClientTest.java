package com.testehan.unary.client;


import com.testehan.models.ex08.AccountBalance;
import com.testehan.models.ex08.BalanceCheckRequest;
import com.testehan.models.ex08.BankServiceGrpc;
import com.testehan.models.ex08.WithdrawRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext().build();

        // this is a Sync/wait call (hence the name Blocking)
        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);

        // this is Asynch
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void getAccountBalance(){
        var balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(7).build();
        var accountBalance = this.bankServiceBlockingStub.getAccountBalance(balanceCheckRequest);

        System.out.println(accountBalance);
    }

    @Test       // streaming server example ...how to handle it in java code
    public void withdraw(){
        var withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(20).build();

        this.bankServiceBlockingStub.withdraw(withdrawRequest)
                .forEachRemaining(money -> System.out.println(money));

    }

    @Test       // streaming server example ...how to handle it in java code
    public void withdrawThrowsExceptionBecauseOfInvalidWithdrawAmount(){
        var withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(200).build();

        StatusRuntimeException thrown = assertThrows(StatusRuntimeException.class,
                ()->this.bankServiceBlockingStub.withdraw(withdrawRequest)
                    .forEachRemaining(money -> System.out.println(money))
        );

        Assertions.assertEquals("FAILED_PRECONDITION: Not enought money to withdraw", thrown.getMessage());

    }

    @Test       // Asynch streaming server example
    public void withdrawAsynch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        var withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(40).build();

        this.bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse(countDownLatch));

        countDownLatch.await();     // wait until the Async call finishes

    }
}

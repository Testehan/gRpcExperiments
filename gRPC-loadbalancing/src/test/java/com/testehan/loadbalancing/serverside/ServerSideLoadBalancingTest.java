package com.testehan.loadbalancing.serverside;

import com.testehan.models.ex08.BalanceCheckRequest;
import com.testehan.models.ex08.BankServiceGrpc;
import com.testehan.models.ex08.DepositRequest;
import com.testehan.models.ex08.Money;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerSideLoadBalancingTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;


    @BeforeAll
    public void setup(){                    // !! we switched port to go to 8585 because that is where nginx is running
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8585)
                .usePlaintext().build();

        // this is a Sync/wait call (hence the name Blocking)
        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);

        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void getAccountBalance(){
        for (int i = 1; i< 100; i++) {
            var balanceCheckRequest = BalanceCheckRequest
                    .newBuilder()
                    .setAccountNumber(ThreadLocalRandom.current().nextInt(1,11))
                    .build();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            var accountBalance = this.bankServiceBlockingStub.getAccountBalance(balanceCheckRequest);

            System.out.println(accountBalance);
        }
    }

    @Test
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> streamObserver = this.bankServiceStub.deposit(new BalanceStreamObserver(latch));

        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder()
                    .setAccountNumber(8)
                    .setMoney(
                            Money.newBuilder().setAmount(10).build()
                    ).build();
            streamObserver.onNext(depositRequest);
        }
        streamObserver.onCompleted();
        latch.await();
    }
}

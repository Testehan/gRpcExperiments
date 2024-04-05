package com.testehan.loadbalancing.client;

import com.testehan.models.ex08.BalanceCheckRequest;
import com.testehan.models.ex08.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxCientTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;

    @BeforeAll
    public void setup(){                    // !! we switched port to go to 8585 because that is where nginx is running
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",8585)
                .usePlaintext().build();

        // this is a Sync/wait call (hence the name Blocking)
        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);

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
}

package com.testehan.unary.client;


import com.testehan.models.ex08.AccountBalance;
import com.testehan.models.ex08.BalanceCheckRequest;
import com.testehan.models.ex08.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;

    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext().build();

        // this is a Sync/wait call (hence the name Blocking)
        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
    }

    @Test
    public void getAccountBallance(){
        var balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(10).build();
        var accountBalance = this.bankServiceBlockingStub.getAccountBalance(balanceCheckRequest);

        System.out.println(accountBalance);
    }
}

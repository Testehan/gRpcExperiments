package com.testehan.loadbalancing.interceptor.deadline;

import com.testehan.loadbalancing.clientside.ServiceRegistry;
import com.testehan.loadbalancing.clientside.TempNameResolverProvider;
import com.testehan.models.ex08.*;
import io.grpc.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientDeadlineTest {
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup(){
        List<String> instances = new ArrayList<>();
        instances.add("localhost:6565");        // GRpcServer1 instance
        instances.add("localhost:7575");        // GRpcServer2 instance
        ServiceRegistry.register("dns:///bank-service", instances);
        NameResolverRegistry.getDefaultRegistry().register(new TempNameResolverProvider());

        ManagedChannel managedChannel = ManagedChannelBuilder
                //.forAddress("localhost", 8585)
                .forTarget("bank-service")
                .intercept(new DeadlineInterceptor())
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test   // deadline is used to timeout a RPC if it takes longer than a specified amount of time
            // before running this, make sure to uncomment the line from BankService from method getAccountBalance
            //          Uninterruptibles.sleepUninterru
    public void balanceTestWithDeadline() {
        for (int i = 0; i < 100; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(ThreadLocalRandom.current().nextInt(1, 11))
                    .build();
            try {
                var balance = this.blockingStub
//                        .withDeadlineAfter(2, TimeUnit.SECONDS)           the best approach is to use the DeadlineInterceptor
                        .getAccountBalance(balanceCheckRequest);
                System.out.println("Received : " + balance.getBalance());
            } catch (StatusRuntimeException e){
                System.out.println("getAccountBalance took too long, so balance is 0");
            }
        }
    }

    @Test
    // deadline is used to timeout a RPC if it takes longer than a specified amount of time
    // before running this, make sure to uncomment the method with the comme from below from BankService
            // @Override       // with deadline (timeout) support
            //public void withdraw(
    public void withdrawTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                .setAccountNumber(6)
                .setAmount(50).build();
        try{
            this.blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .withdraw(withdrawRequest)
                    .forEachRemaining(money -> System.out.println("Received : " + money.getAmount()));
        } catch (StatusRuntimeException e){
            System.out.println("A timeout occured on the server");
        }

    }
}

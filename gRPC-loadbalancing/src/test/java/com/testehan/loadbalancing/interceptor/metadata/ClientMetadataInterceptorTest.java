package com.testehan.loadbalancing.interceptor.metadata;

import com.testehan.loadbalancing.clientside.ServiceRegistry;
import com.testehan.loadbalancing.clientside.TempNameResolverProvider;
import com.testehan.models.ex08.BalanceCheckRequest;
import com.testehan.models.ex08.BankServiceGrpc;
import com.testehan.models.ex08.WithdrawRequest;
import io.grpc.*;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientMetadataInterceptorTest {
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
                .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstants.getClientToken()))
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test       // depending on the value uncommented in ClientConstants, this can either work or not :)
    public void balanceTest() {
        for (int i = 0; i < 100; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(ThreadLocalRandom.current().nextInt(1, 11))
                    .build();
            try {
                var balance = this.blockingStub
                        .getAccountBalance(balanceCheckRequest);
                System.out.println("Received : " + balance.getBalance());
            } catch (StatusRuntimeException e){
                System.out.println(e.getStatus().getDescription());
            }
        }
    }

    @Test
    public void withdrawTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder()
                .setAccountNumber(6)
                .setAmount(50).build();
        try{
            this.blockingStub
                    .withdraw(withdrawRequest)
                    .forEachRemaining(money -> System.out.println("Received : " + money.getAmount()));
        } catch (StatusRuntimeException e){
            System.out.println("A timeout occured on the server");
        }

    }
}

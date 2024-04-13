package com.testehan.loadbalancing.server.interceptor.metadata;

import com.testehan.loadbalancing.server.interceptor.metadata.constants.ServerConstants;
import com.testehan.loadbalancing.server.interceptor.metadata.constants.UserRole;
import com.testehan.loadbalancing.server.repository.AccountRepository;
import com.testehan.loadbalancing.server.request.CashStreamingRequest;
import com.testehan.models.ex08.*;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

public class BankServiceErrorHandlingWithMetadata extends BankServiceGrpc.BankServiceImplBase{

    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
       // not relevant for error handling example
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var amount = request.getAmount();   // for this example we assume that this number is a multiple of 10
        System.out.println("Got a withdraw request for acc number : " + accountNumber);
        var balance = AccountRepository.getBalance(accountNumber);

        if (amount < 10 || ((amount % 10) != 0)){
            Metadata metadata = new Metadata();
            Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
            var withdrawalError = WithdrawalError.newBuilder().setAmount(balance).setErrorMessage(ErrorMessage.ONLY_TEN_MULTIPLES).build();
            metadata.put(errorKey, withdrawalError);

            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));

            return;
        }

        if (balance < amount) {
            Metadata metadata = new Metadata();
            Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
            var withdrawalError = WithdrawalError.newBuilder().setAmount(balance).setErrorMessage(ErrorMessage.INSUFFICIENT_BALANCE).build();
            metadata.put(errorKey, withdrawalError);

            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
            return;
        }

        for (int i = 0; i< amount / 10; i++){   // because we send back in 10 dollar increments
            var money = Money.newBuilder().setAmount(10).build();
                responseObserver.onNext(money);
                System.out.println("Delivered $10");
                AccountRepository.deductAmount(accountNumber, 10);
        }

        // mark operation as completed
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<AccountBalance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }
}

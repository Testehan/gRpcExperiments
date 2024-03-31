package com.testehan.unary.server.request;

import com.testehan.models.ex08.*;
import com.testehan.unary.server.repository.AccountRepository;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        var fromAccount = transferRequest.getFromAccount();
        var toAccount = transferRequest.getToAccount();
        var transferedAmount = transferRequest.getAmount();

        var balance = AccountRepository.getBalance(fromAccount);
        var transferStatus = TransferStatus.FAILED;

        if (balance >= transferedAmount && fromAccount != toAccount){
            AccountRepository.deductAmount(fromAccount, transferedAmount);
            AccountRepository.addAmount(toAccount, transferedAmount);
            transferStatus = TransferStatus.SUCCESS;
        }

        var transferResponse = TransferResponse.newBuilder().setStatus(transferStatus)
                .setFromAccount(AccountBalance.newBuilder()
                        .setAccountNumber(fromAccount)
                        .setBalance(AccountRepository.getBalance(fromAccount))
                        .build())
                .setToAccount(AccountBalance.newBuilder()
                        .setAccountNumber(toAccount)
                        .setBalance(AccountRepository.getBalance(toAccount))
                        .build())
                .build();

        transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountRepository.printAllAccounts();
        transferResponseStreamObserver.onCompleted();
    }
}

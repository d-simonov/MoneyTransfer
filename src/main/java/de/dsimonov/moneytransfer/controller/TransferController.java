package de.dsimonov.moneytransfer.controller;

import de.dsimonov.moneytransfer.model.Transfer;
import de.dsimonov.moneytransfer.service.TransferService;
import io.javalin.http.Context;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class TransferController {

    private TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    public void create(Context context) {
        Transfer transfer = context.bodyAsClass(Transfer.class);
        transfer = transferService.createTransfer(transfer);
        context.status(201);
        context.json(transfer);
    }

    public void execute(Context context) {
        Long transferId = Long.valueOf(Objects.requireNonNull(context.pathParam("transfer-id")));
        Transfer transfer = transferService.getTransfer(transferId);
        if (transfer == null) {
            context.status(404);
        } else {
            boolean execute = transferService.execute(transfer);
            context.json(execute);
        }
    }

    public void delete(Context context) {
        Long accountId = Long.valueOf(Objects.requireNonNull(context.pathParam("transfer-id")));
        transferService.deleteTransfer(accountId);
    }
}

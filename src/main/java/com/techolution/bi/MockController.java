package com.techolution.bi;

import com.techolution.bi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/electronicsafes")
public class MockController {
    @Autowired
    VBIClient vbiClient;

    @GetMapping("/test")
    @SuppressWarnings("unchecked")
    public String testCall(@RequestParam(value = "safeguid") String safeguid) {
        return vbiClient.getEndPoints(safeguid);

    }

    @PostMapping("/machinedata")
    @SuppressWarnings("unchecked")
    public BiTransaction machineDataConsumer(@RequestBody RevolutionTransactionsType revolutionTransactionsType) {
        BiTransaction biTransaction = new BiTransaction();
        TransactionType transaction = revolutionTransactionsType.getTransaction();
        biTransaction.safeOperationId = transaction.getParentTxId();
        biTransaction.transactionId = transaction.getID();
        biTransaction.transactionBusinessDate = transaction.getCreateTime().getFull();
        biTransaction.transactionCreatedOn = transaction.getCreateTime().getFull();
        biTransaction.safeOperatorId = transaction.getType().getID();
        ComponentTransaction componentTransaction = new ComponentTransaction();
        componentTransaction.componentId = transaction.getNetValue().getContainer().getIndex();
        componentTransaction.tenderTypeId = transaction.getNetValue().getContainer().getName();
        componentTransaction.quantity = transaction.getNetValue().getTotalValue().getValue();
        Denomination denomination = new Denomination();
        denomination.currencyCode = transaction.getNetValue().getWad().getCurrency();
        denomination.denominationType = transaction.getNetValue().getWad().getDenomination();
        denomination.faceValue = transaction.getNetValue().getWad().getCount();
        List<ComponentTransaction> allTX = new ArrayList<ComponentTransaction>();
        allTX.add(componentTransaction);
        componentTransaction.denomination = denomination;
        biTransaction.transactionItems = allTX;
        return vbiClient.createTransaction(biTransaction);

    }
}

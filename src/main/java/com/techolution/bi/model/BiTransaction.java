package com.techolution.bi.model;

import lombok.Data;

import java.util.List;

@Data
public class BiTransaction {
    public BiTransaction() {}
    public String transactionId;
    public String transactionCreatedOn;
    public String transactionBusinessDate;
    public String safeOperationId;
    public String safeOperatorId;
    public List<ComponentTransaction> transactionItems;
}


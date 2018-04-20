package com.techolution.bi.model;

import lombok.Data;

@Data
public class ComponentTransaction {
    public String componentId;
    public String tenderTypeId;
    public Denomination denomination;
    public String quantity;
}
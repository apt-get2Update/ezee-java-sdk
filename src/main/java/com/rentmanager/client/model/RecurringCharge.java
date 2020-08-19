package com.rentmanager.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.JavaBean;

@JavaBean(defaultProperty = "RecurringCharges")
public class RecurringCharge {

    @JsonProperty("RecurringChargeID")
    public Integer recurringChargeID;
    @JsonProperty("EntityType")
    public String entityType;
    @JsonProperty("EntityKeyID")
    public Integer entityKeyID;
    @JsonProperty("UnitID")
    public Integer unitID;
    @JsonProperty("Frequency")
    public Integer frequency;
    @JsonProperty("ChargeTypeID")
    public Integer chargeTypeID;
    @JsonProperty("Comment")
    public String comment;
    @JsonProperty("FromDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public String fromDate;
    @JsonProperty("ToDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public String toDate;
    @JsonProperty("IsCalculated")
    public Boolean isCalculated;
    @JsonProperty("Calculation")
    public String calculation;
    @JsonProperty("SortOrder")
    public Integer sortOrder;
    @JsonProperty("CreateDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public String createDate;
    @JsonProperty("CreateUserID")
    public Integer createUserID;
    @JsonProperty("UpdateDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public String updateDate;
    @JsonProperty("UpdateUserID")
    public Integer updateUserID;
    @JsonProperty("ChargeType")
    public ChargeType chargeType;

}
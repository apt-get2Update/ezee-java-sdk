package com.rentmanager.client.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ProspectStage {

    @JsonProperty("ProspectStageID")
    public Integer prospectStageID;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("IsActive")
    public Boolean isActive;
    @JsonProperty("SortOrder")
    public Integer sortOrder;
    @JsonProperty("CreateDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public Date createDate;
    @JsonProperty("CreateUserID")
    public Integer createUserID;
    @JsonProperty("UpdateDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public Date updateDate;
    @JsonProperty("UpdateUserID")
    public Integer updateUserID;
    @JsonProperty("CreateUser")
    public CreateUser createUser;
    @JsonProperty("UpdateUser")
    public UpdateUser updateUser;
    @JsonProperty("ProspectStageScreeningDecisions")
    public List<Object> prospectStageScreeningDecisions = null;
    @JsonProperty("ProspectStageChargeTypes")
    public List<Object> prospectStageChargeTypes = null;

}
package com.rentmanager.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.JavaBean;

@JavaBean(defaultProperty = "AssetTypes")
public class AssetType {

    @JsonProperty("AssetTitleStatusID")
    public Integer assetTitleStatusID;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Description")
    public String description;
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
    @JsonProperty("CreateUser")
    public CreateUser createUser;

}
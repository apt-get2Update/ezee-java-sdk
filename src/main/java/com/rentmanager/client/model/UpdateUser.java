package com.rentmanager.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UpdateUser {
    @JsonProperty("UserID")
    public Integer userID;
    @JsonProperty("MainLocationUserID")
    public Integer mainLocationUserID;
    @JsonProperty("Username")
    public String username;
    @JsonProperty("Lastname")
    public String lastname;
    @JsonProperty("Firstname")
    public String firstname;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("IsActive")
    public Boolean isActive;
    @JsonProperty("IsAdmin")
    public Boolean isAdmin;
    @JsonProperty("IsStrongPassword")
    public Boolean isStrongPassword;
    @JsonProperty("Email")
    public String email;
    @JsonProperty("LockoutDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    public Date lockoutDate;
    @JsonProperty("LastPasswordChangeDate")
    public String lastPasswordChangeDate;
    @JsonProperty("PhoneSystemExtension")
    public String phoneSystemExtension;
    @JsonProperty("CreateDate")
    public Date createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    @JsonProperty("CreateUserID")
    public Integer createUserID;
    @JsonProperty("UpdateDate")
    public Date updateDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    @JsonProperty("UpdateUserID")
    public Integer updateUserID;
    @JsonProperty("DefaultLocationID")
    public Integer defaultLocationID;

}
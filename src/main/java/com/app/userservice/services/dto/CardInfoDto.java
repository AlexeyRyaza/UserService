package com.app.userservice.services.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class CardInfoDto {

    private Integer id;

    @NotNull(message = "User ID is mandatory")
    private Integer userId;

    @NotBlank(message = "Card number is mandatory")
    private String number;

    @NotBlank(message = "Card holder is mandatory")
    private String holder;

    @Future(message = "Expiration date must be in the future")
    private Date expirationDate;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getNumber() {
        return number;
    }

    public String getHolder() {
        return holder;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}


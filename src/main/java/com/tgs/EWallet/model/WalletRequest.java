package com.tgs.EWallet.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletRequest {
    @NotNull(message = "valletId is required")
    public UUID valletId; // Match the "valletId" spelling from your task
    @NotNull(message = "operationType is required")
    public String operationType;
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    public BigDecimal amount;
}
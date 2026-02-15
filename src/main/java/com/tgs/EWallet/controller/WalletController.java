package com.tgs.EWallet.controller;

import com.tgs.EWallet.model.WalletRequest;
import com.tgs.EWallet.service.WalletService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
    private final WalletService service;

    public WalletController(WalletService service) { this.service = service; }

    @PostMapping("/wallet")
    public void handle(@Valid @RequestBody WalletRequest request) {
        service.process(request);
    }

    @GetMapping("/wallets/{id}")
    public BigDecimal get(@Valid @PathVariable UUID id) {
        return service.getBalance(id);
    }
}
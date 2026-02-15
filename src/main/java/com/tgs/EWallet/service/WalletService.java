package com.tgs.EWallet.service;

import com.tgs.EWallet.model.Wallet;
import com.tgs.EWallet.model.WalletRequest;
import com.tgs.EWallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository repository;

    public WalletService(WalletRepository repository) { this.repository = repository; }

    @Transactional
    public void process(WalletRequest request) {
        Wallet wallet = repository.findByIdWithLock(request.valletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if ("DEPOSIT".equals(request.operationType)) {
            wallet.setBalance(wallet.getBalance().add(request.amount));
        } else if ("WITHDRAW".equals(request.operationType)) {
            if (wallet.getBalance().compareTo(request.amount) < 0) {
                throw new IllegalStateException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(request.amount));
        } else {
            throw new IllegalArgumentException("Invalid Operation Type");
        }
        repository.save(wallet);
    }

    public BigDecimal getBalance(UUID id) {
        return repository.findById(id)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    }
}
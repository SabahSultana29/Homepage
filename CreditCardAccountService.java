package com.scb.creditcardorigination.userStory6.service;
import com.scb.creditcardorigination.userStory6.model.CreditCardAccount;
import com.scb.creditcardorigination.userStory6.repository.CreditCardAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditCardAccountService {

    private final CreditCardAccountRepository repository;

    public CreditCardAccountService(CreditCardAccountRepository repository) {
        this.repository = repository;
    }

    public CreditCardAccount saveAccount(CreditCardAccount account) {
        return repository.save(account);
    }
}

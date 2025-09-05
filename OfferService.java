//package com.scb.creditcardorigination.userStory6.service;
//import com.scb.creditcardorigination.userStory6.dto.AcceptOfferRequest;
//import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
//import com.scb.creditcardorigination.userStory6.model.CreditCardAccount;
//import com.scb.creditcardorigination.userStory6.model.CreditCardOffer;
//import com.scb.creditcardorigination.userStory6.repository.CreditCardAccountRepository;
//import com.scb.creditcardorigination.userStory6.repository.OfferRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class OfferService {
//
//    private final OfferRepository offerRepository;
//    private final CreditCardAccountRepository accountRepository;
//    private final EmailService emailService;
//    private final PrintShopService printShopService;
//
//    public OfferService(OfferRepository offerRepository,
//                        CreditCardAccountRepository accountRepository,
//                        EmailService emailService,
//                        PrintShopService printShopService) {
//        this.offerRepository = offerRepository;
//        this.accountRepository = accountRepository;
//        this.emailService = emailService;
//        this.printShopService = printShopService;
//    }
//
//    @Transactional
//    public ResponseEntity<AcceptOfferResponse> acceptOffer(AcceptOfferRequest request) {
//        CreditCardOffer offer = offerRepository.findById(request.getOfferId()).orElse(null);
//
//        if (offer == null) {
//            return new ResponseEntity<>(
//                    new AcceptOfferResponse(false, "Offer not found",generateCardNumber()),
//                    HttpStatus.NOT_FOUND
//            );
//        }
//
//        CreditCardAccount account = new CreditCardAccount();
//        account.setId(request.getOfferId());
//        account.setCardNumber(generateCardNumber());
//        account.setCardNumber(offer.getCardNumber());
//        account.setCreatedAt(offer.getCreditLimit());
//        account.setStatus("ACTIVE");
//        account.setCreatedAt(LocalDateTime.now());
//        accountRepository.save(account);
//        try{
//            emailService.sendConfirmation(account.getEmailId(), account.getCardNumber());
//
//        }catch (Exception e)
//        {
//            System.out.println("Failed to send confirmation email: " + e.getMessage());
//        }
//        printShopService.printCard(account.getId());
//
//        AcceptOfferResponse response =
//                new AcceptOfferResponse(true, "Card created successfully",generateCardNumber());
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    private String generateCardNumber() {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 16; i++) {
//            sb.append(random.nextInt(10));
//        }
//        return sb.toString();
//    }
//
//    public List<CreditCardOffer> getAllOffers() {
//        return offerRepository.findAll();
//    }
//
//    public ResponseEntity<String> declineOffer(Long offerId) {
//        // Just check if offer exists
//        if (!offerRepository.existsById(offerId)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Offer with ID " + offerId + " not found");
//        }
//
//        // For now, you can either delete it OR simply acknowledge decline
//        // offerRepository.deleteById(offerId);
//
//        return ResponseEntity.ok("Offer with ID " + offerId + " declined successfully");
//    }
//}
//


package com.scb.creditcardorigination.userStory6.service;

import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
import com.scb.creditcardorigination.userStory6.dto.OfferAcceptanceRequest;
import com.scb.creditcardorigination.userStory6.model.CreditCardAccount;
import com.scb.creditcardorigination.userStory6.model.CreditCardOffer;
import com.scb.creditcardorigination.userStory6.repository.CreditCardAccountRepository;
import com.scb.creditcardorigination.userStory6.repository.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final CreditCardAccountRepository accountRepository;

    public OfferService(OfferRepository offerRepository,
                        CreditCardAccountRepository accountRepository) {
        this.offerRepository = offerRepository;
        this.accountRepository = accountRepository;
    }

    // Fetch all offers
    public List<CreditCardOffer> getAllOffers() {
        return offerRepository.findAll();
    }

    // Accept offer
    public ResponseEntity<AcceptOfferResponse> acceptOffer(OfferAcceptanceRequest request) {
        Optional<CreditCardOffer> offerOpt = offerRepository.findById(request.getOfferId());

        if (offerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AcceptOfferResponse(false, "Offer not found", null));
        }

        CreditCardOffer offer = offerOpt.get();

        // Create credit card account
        CreditCardAccount account = new CreditCardAccount();
        account.setCardNumber(request.getCardNumber());
        account.setEmailId(request.getEmail()); // ✅ from DTO
        account.setCreatedAt(LocalDateTime.now());
        account.setOffer(offer);

        accountRepository.save(account);

        AcceptOfferResponse response = new AcceptOfferResponse(
                true,
                "Offer accepted successfully",
                account.getCardNumber()
        );

        return ResponseEntity.ok(response);
    }

    // Decline offer
    public ResponseEntity<String> declineOffer(Long offerId) {
        Optional<CreditCardOffer> offerOpt = offerRepository.findById(offerId);

        if (offerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer not found");
        }

        CreditCardOffer offer = offerOpt.get();
        offer.setStatus("Declined");  // even if your model doesn’t persist this, good for future

        offerRepository.save(offer);

        return ResponseEntity.ok("Offer declined successfully");
    }
}
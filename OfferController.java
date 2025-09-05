//package com.scb.creditcardorigination.userStory6.controller;
//import com.scb.creditcardorigination.userStory6.dto.AcceptOfferRequest;
//import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
//import com.scb.creditcardorigination.userStory6.model.CreditCardOffer;
//import com.scb.creditcardorigination.userStory6.service.OfferService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/offers")
//public class OfferController {
//    private final OfferService offerService;
//    public OfferController(OfferService offerService){
//        this.offerService = offerService;
//    }
//
// //api : accept credit card offer
//@PostMapping("/accept")
//    public ResponseEntity<AcceptOfferResponse> acceptOffer(@RequestBody AcceptOfferRequest request){
//        return offerService.acceptOffer(request);
//}
//@GetMapping
//    public List<CreditCardOffer> getAllOffers(){
//        return offerService.getAllOffers();
//}
//
//
//}



//updated code
package com.scb.creditcardorigination.userStory6.controller;

import com.scb.creditcardorigination.userStory6.dto.AcceptOfferResponse;
import com.scb.creditcardorigination.userStory6.dto.OfferAcceptanceRequest;
import com.scb.creditcardorigination.userStory6.model.CreditCardOffer;
import com.scb.creditcardorigination.userStory6.service.OfferOrchestrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferOrchestrationService orchestrationService;

    public OfferController(OfferOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/accept")
    public ResponseEntity<AcceptOfferResponse> acceptOffer(@RequestBody OfferAcceptanceRequest request) {
        return orchestrationService.processOfferAcceptance(request);
    }

    @PostMapping("/decline/{offerId}")
    public ResponseEntity<String> declineOffer(@PathVariable Long offerId) {
        return orchestrationService.processOfferDecline(offerId);
    }

//    @GetMapping
//    public List<CreditCardOffer> getAllOffers() {
//        return offerService.getAllOffers();
//    }
}


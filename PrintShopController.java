package com.scb.creditcardorigination.userStory6.controller;
import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import com.scb.creditcardorigination.userStory6.service.PrintShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/printShop")
public class PrintShopController {

    private final PrintShopService printshopService;

    public PrintShopController(PrintShopService printshopService) {
        this.printshopService = printshopService;
    }

    @PostMapping("/request")
    public ResponseEntity<PrintShopRequest> sendToPrintShop(@RequestBody PrintShopRequest request) {
        PrintShopRequest savedRequest = printshopService.saveRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/requests")
    public List<PrintShopRequest> getAllRequests() {
        return printshopService.getAllRequests();
    }
    @PostMapping("/print/{id}")
    public  String printCard(@PathVariable long id){
        printshopService.printCard(id);
        return "Print job started for PrintShop Request ID:" + id;
    }
}

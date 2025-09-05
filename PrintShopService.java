package com.scb.creditcardorigination.userStory6.service;
import com.scb.creditcardorigination.userStory6.repository.PrintShop;
import com.scb.creditcardorigination.userStory6.model.PrintShopRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PrintShopService {

    private final PrintShop printShopRepository;

    // 🔹 Define a formatter for requestTime
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public PrintShopService(PrintShop printShopRepository) {
        this.printShopRepository = printShopRepository;
    }

    // save request
    public PrintShopRequest saveRequest(PrintShopRequest request) {

        // 🔹 Auto-set requestTime if not provided
        if (request.getRequestTime() == null) {
            request.setRequestTime(LocalDateTime.now());
        }

        // 🔹 Auto-update accountId if not provided (increment latest one)
        if (request.getAccountId() == 0) {
            Long maxId = printShopRepository.findAll()
                    .stream()
                    .mapToLong(PrintShopRequest::getAccountId)
                    .max()
                    .orElse(1000L); // default start
            request.setAccountId(maxId + 1);
        }

        PrintShopRequest saved = printShopRepository.save(request);

        System.out.println("PrintShop Request Created:");
        System.out.println("AccountID: " + saved.getAccountId());
        System.out.println("RequestTime: " + saved.getRequestTime().format(FORMATTER));
        System.out.println("Status=" + saved.getStatus());
        System.out.println("Details=" + saved.getDetails());

        return saved;
    }

    // fetch all requests
    public List<PrintShopRequest> getAllRequests() {
        List<PrintShopRequest> requests = printShopRepository.findAll();
        System.out.println(" All PrintShop Requests:");
        for (PrintShopRequest req : requests) {
            System.out.println("----------------------------");
            System.out.println("ID=" + req.getId());
            System.out.println("AccountID: " + req.getAccountId());
            System.out.println("RequestTime: " +
                    (req.getRequestTime() != null ? req.getRequestTime().format(FORMATTER) : "N/A"));
            System.out.println("Status=" + req.getStatus());
            System.out.println("Details=" + req.getDetails());
        }
        return requests;
    }

    // print card
    public void printCard(long id) {
        PrintShopRequest req = printShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + id));

        System.out.println("Printing card for PrintShop Request:");
        System.out.println("ID=" + req.getId());
        System.out.println("AccountID: " + req.getAccountId());
        System.out.println("RequestTime: " +
                (req.getRequestTime() != null ? req.getRequestTime().format(FORMATTER) : "N/A"));
        System.out.println("Status=" + req.getStatus());
        System.out.println("Details=" + req.getDetails());
    }
}
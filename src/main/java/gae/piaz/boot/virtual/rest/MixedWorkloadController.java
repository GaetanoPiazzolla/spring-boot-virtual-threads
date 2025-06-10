package gae.piaz.boot.virtual.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gae.piaz.boot.virtual.domain.Book;
import gae.piaz.boot.virtual.domain.Order;
import gae.piaz.boot.virtual.repository.BookRepository;
import gae.piaz.boot.virtual.repository.OrderRepository;
import gae.piaz.boot.virtual.service.MixedWorkloadService;

@RestController
@RequestMapping("/mixed")
public class MixedWorkloadController {

    private final Logger log = LoggerFactory.getLogger(MixedWorkloadController.class);

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MixedWorkloadService workloadService;

    public record ComplexOperationResult(
        List<Order> recentOrders,
        String userStatus,
        List<Book> recommendedBooks,
        String weatherForecast
    ) {}

    @GetMapping("/complex-operation/{userId}")
    @Transactional
    public ResponseEntity<ComplexOperationResult> complexOperation(@PathVariable Integer userId) {
        log.info("Starting complex operation for user {}", userId);
        
        // 1. Database call - get user's recent orders
        List<Order> recentOrders = orderRepository.findByUserUserIdAndCreatedAtAfter(
            userId, LocalDateTime.now().minusDays(30));
        
        // 2. External API call - validate user status
        String userStatus = callExternalUserService(userId);
        
        // 3. Another database call - get recommended books
        List<Book> recommendedBooks = bookRepository.findTop5ByOrderByCreatedAtDesc();
        
        // 4. External API call - get weather for shipping estimate
        String weatherData = callWeatherService();

        ComplexOperationResult result = new ComplexOperationResult(
            recentOrders, userStatus, recommendedBooks, weatherData);
            
        log.info("Completed complex operation for user {}", userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/complex-operation/optimized/{userId}")
    public ResponseEntity<ComplexOperationResult> optimized_complexOperation(@PathVariable Integer userId) {
        log.info("Starting optimized mixed operation for user {}", userId);

        // 1. Database call - get user's recent orders (separate transaction)
        List<Order> recentOrders = workloadService.getUserRecentOrders(userId);

        // 2. External API call - validate user status (no DB connection held)
        String userStatus = callExternalUserService(userId);

        // 3. Another database call - get recommended books (separate transaction)
        List<Book> recommendedBooks = workloadService.getRecommendedBooks();

        // 4. External API call - get weather for shipping estimate (no DB connection held)
        String weatherData = callWeatherService();

        ComplexOperationResult result = new ComplexOperationResult(
            recentOrders, userStatus, recommendedBooks, weatherData);

        log.info("Completed optimized mixed operation for user {}", userId);
        return ResponseEntity.ok(result);
    }
    
    private String callExternalUserService(Integer userId) {
        try {
            log.debug("Calling external user service for user {}", userId);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                "https://httpbin.org/delay/0.5", String.class);
            return "ACTIVE";
        } catch (Exception e) {
            log.error("Error calling external user service", e);
            return "UNKNOWN";
        }
    }
    
    private String callWeatherService() {
        try {
            log.debug("Calling weather service");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                "https://httpbin.org/delay/0.3", String.class);
            return "SUNNY";
        } catch (Exception e) {
            log.error("Error calling weather service", e);
            return "UNKNOWN";
        }
    }

}
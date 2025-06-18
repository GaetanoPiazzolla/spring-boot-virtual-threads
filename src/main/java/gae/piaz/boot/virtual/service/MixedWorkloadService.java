package gae.piaz.boot.virtual.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import gae.piaz.boot.virtual.domain.Book;
import gae.piaz.boot.virtual.domain.Order;
import gae.piaz.boot.virtual.repository.BookRepository;
import gae.piaz.boot.virtual.repository.OrderRepository;
import gae.piaz.boot.virtual.rest.MixedWorkloadController;

@Service
public class MixedWorkloadService {

    private final Logger log = LoggerFactory.getLogger(MixedWorkloadService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;


    @Transactional
    public MixedWorkloadController.ComplexOperationResult executeComplexOperationInSameTransaction(Integer userId) {
        log.info("Starting complex operation for user {} - Optimized - {}", userId);

        // 1. Database call - get user's recent orders
        List<Order> recentOrders = getUserRecentOrders(userId);

        // 2. External API call - validate user status
        String userStatus = callExternalUserService(userId);

        // 3. Another database call - get recommended books
        List<Book> recommendedBooks = getRecommendedBooks();

        // 4. External API call - get weather for shipping estimate
        String weatherData = callWeatherService();

        MixedWorkloadController.ComplexOperationResult result = new MixedWorkloadController.ComplexOperationResult(recentOrders, userStatus, recommendedBooks,
            weatherData);

        log.info("Completed complex operation for user {}", userId);
        return result;
    }

    private String callExternalUserService(Integer userId) {
        try {
            log.debug("Calling external user service for user {}", userId);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity("https://httpbin.org/delay/0.5", String.class);
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
            ResponseEntity<String> response = restTemplate.getForEntity("https://httpbin.org/delay/0.3", String.class);
            return "SUNNY";
        } catch (Exception e) {
            log.error("Error calling weather service", e);
            return "UNKNOWN";
        }
    }

    // Separate transactional methods for DB operations
    public List<Order> getUserRecentOrders(Integer userId) {
        log.debug("Fetching recent orders for user {}", userId);
        return orderRepository.findByUserUserIdAndCreatedAtAfter(userId, LocalDateTime.now()
            .minusDays(30));
    }

    public List<Book> getRecommendedBooks() {
        log.debug("Fetching recommended books");
        return bookRepository.findTop5ByOrderByCreatedAtDesc();
    }

}

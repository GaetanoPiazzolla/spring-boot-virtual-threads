package gae.piaz.boot.virtual.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Separate transactional methods for DB operations
    @Transactional(readOnly = true)
    public List<Order> getUserRecentOrders(Integer userId) {
        log.debug("Fetching recent orders for user {}", userId);
        return orderRepository.findByUserUserIdAndCreatedAtAfter(
            userId, LocalDateTime.now().minusDays(30));
    }

    @Transactional(readOnly = true)
    public List<Book> getRecommendedBooks() {
        log.debug("Fetching recommended books");
        return bookRepository.findTop5ByOrderByCreatedAtDesc();
    }

}

package gae.piaz.boot.virtual.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gae.piaz.boot.virtual.domain.Book;
import gae.piaz.boot.virtual.domain.Order;
import gae.piaz.boot.virtual.service.MixedWorkloadService;

@RestController
@RequestMapping("/mixed")
public class MixedWorkloadController {

    @Autowired
    private MixedWorkloadService workloadService;

    public record ComplexOperationResult(List<Order> recentOrders,
                                         String userStatus,
                                         List<Book> recommendedBooks,
                                         String weatherForecast) {}

    @GetMapping("/complex-operation/{userId}")
    @Transactional
    public ResponseEntity<ComplexOperationResult> complexOperation(@PathVariable Integer userId) {
        return ResponseEntity.ok(workloadService.executeComplexOperationInSameTransaction(userId));
    }

    @GetMapping("/complex-operation/optimized/{userId}")
    public ResponseEntity<ComplexOperationResult> optimized_complexOperation(@PathVariable Integer userId) {
        return ResponseEntity.ok(workloadService.executeComplexOperationMultipleTransactions(userId));
    }

}
package gae.piaz.boot.virtual.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gae.piaz.boot.virtual.repository.BookRepository;
import gae.piaz.boot.virtual.repository.OrderRepository;

@RestController
@RequestMapping("/database")
public class DBCleanController {

    private final Logger log = LoggerFactory.getLogger(DBCleanController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Before each test you should run the following script
     * to make every load test independent of the previous executions.
     */
    @GetMapping("/clean")
    public ResponseEntity<Void> databaseClean() {
        log.info("cleaning up database");
        bookRepository.deleteAllBut(1);
        orderRepository.truncate();
        return ResponseEntity.noContent()
            .build();
    }

}
package gae.piaz.boot.virtual.rest;

import gae.piaz.boot.virtual.domain.dto.OrderDTO;
import gae.piaz.boot.virtual.domain.Book;
import gae.piaz.boot.virtual.domain.Order;
import gae.piaz.boot.virtual.domain.User;
import gae.piaz.boot.virtual.repository.BookRepository;
import gae.piaz.boot.virtual.repository.OrderRepository;
import gae.piaz.boot.virtual.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<OrderDTO> addOrder(@RequestParam("bookIsbn") String bookIsbn, @RequestParam("firstName") String firstName) {

        UUID uuid = UUID.randomUUID();
        log.info("addOrder() {} running", uuid);

        User user = userRepository.findByFirstName(firstName);
        Book book = bookRepository.findByIsbn(bookIsbn);
        log.info("addOrder() {} I've got user and book", uuid);

        Order order = new Order();
        order.setUser(user);
        order.setQuantity(1);
        order.setBook(book);
        order = orderRepository.save(order);

        OrderDTO orderDTO = new OrderDTO(order.getOrderId(), order.getQuantity(), book.getBookId(), user.getUserId());
        log.info("addOrder() {} executed", uuid);
        return ResponseEntity.ok(orderDTO);

    }

}

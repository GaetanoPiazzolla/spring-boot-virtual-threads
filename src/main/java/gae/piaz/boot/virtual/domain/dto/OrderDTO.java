package gae.piaz.boot.virtual.domain.dto;


import java.io.Serializable;

public record OrderDTO (Integer orderId, Integer quantity, Integer bookId, Integer userId) implements Serializable {
}

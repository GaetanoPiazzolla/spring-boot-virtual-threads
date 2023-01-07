package gae.piaz.boot.virtual.domain.dto;

import java.io.Serializable;

public record BookDTO(Integer bookId, String author, String isbn, String title, Integer year) implements Serializable {
}



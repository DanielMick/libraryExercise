package com.library.hidden.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import org.antlr.v4.runtime.misc.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	@NotBlank
	private String title;

	@NotBlank
	private String author;

	@NotBlank
	private String isbn;

	@NotNull
	@PastOrPresent
	private LocalDate publishedDate;
}

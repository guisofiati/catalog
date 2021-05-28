package io.github.guisofiati.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import io.github.guisofiati.dscatalog.entities.Product;
import io.github.guisofiati.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	private long existingID;
	private long nonExistingID;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingID = 1L;
		nonExistingID = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {

		Product product = Factory.createProduct();
		product.setId(null);

		product = repository.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		repository.deleteById(existingID);

		Optional<Product> result = repository.findById(existingID);
		
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingID);
		});
	}

	@Test
	public void findByIdShouldReturnNonEmptyOptionalProductWhenIdExists() {

		Optional<Product> result = repository.findById(existingID);
		
		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyOptionalProductWhenIdDoesNotExists() {

		Optional<Product> result = repository.findById(nonExistingID);
		
		Assertions.assertTrue(result.isEmpty());
	}
}
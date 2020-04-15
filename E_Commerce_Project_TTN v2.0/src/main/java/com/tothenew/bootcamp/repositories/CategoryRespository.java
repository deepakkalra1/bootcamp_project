package com.tothenew.bootcamp.repositories;

import com.tothenew.bootcamp.entity.ProductContent.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRespository extends CrudRepository<Category,Integer> {
}

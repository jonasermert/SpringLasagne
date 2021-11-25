package com.jonasermert.springlasagne.repositories;

import com.jonasermert.springlasagne.domain.Ingredient;

public interface IngredientRepository {

    Iterable<Ingredient> findAll();
    Ingredient findById(String id);
    Ingredient save(Ingredient ingredient);

}

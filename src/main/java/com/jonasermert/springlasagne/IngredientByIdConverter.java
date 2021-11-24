package com.jonasermert.springlasagne;

import com.jonasermert.springlasagne.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {


    @Override
    public Ingredient convert(String id) {
        return ingredientRepo.findById(id);
        // return null;
    }
}

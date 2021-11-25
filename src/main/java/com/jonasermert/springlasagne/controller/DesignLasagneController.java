package com.jonasermert.springlasagne.controller;


import com.jonasermert.springlasagne.domain.Ingredient;
import com.jonasermert.springlasagne.domain.Lasagne;
import com.jonasermert.springlasagne.domain.Order;
import com.jonasermert.springlasagne.repositories.IngredientRepository;
import com.jonasermert.springlasagne.repositories.LasagneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignLasagneController {

    private final IngredientRepository ingredientRepo;
    private LasagneRepository designRepo;

    @Autowired
    public DesignLasagneController(IngredientRepository ingredientRepo, LasagneRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "lasagne")
    public Lasagne lasagne() {
        return new Lasagne();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Lasagne design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) {
            return "design";
        }
        Lasagne saved = designRepo.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList())
                ;
    }

}

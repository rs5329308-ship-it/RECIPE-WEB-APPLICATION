package com.recipeapp.service;

import com.recipeapp.dto.RecipeDto;
import com.recipeapp.entity.*;
import com.recipeapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public List<RecipeDto> getAllRecipes(String username) {
        List<Recipe> recipes = recipeRepository.findAll();
        Set<Long> bookmarkedIds = getBookmarkedIds(username);
        return recipes.stream().map(r -> toDto(r, bookmarkedIds)).collect(Collectors.toList());
    }

    public RecipeDto getRecipeById(Long id, String username) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        Set<Long> bookmarkedIds = getBookmarkedIds(username);
        return toDto(recipe, bookmarkedIds);
    }

    public List<RecipeDto> searchByIngredients(List<String> ingredients, String username) {
        List<String> lowerIngredients = ingredients.stream()
                .map(String::toLowerCase).collect(Collectors.toList());
        List<Recipe> recipes = recipeRepository.findByIngredientNames(lowerIngredients);
        Set<Long> bookmarkedIds = getBookmarkedIds(username);
        return recipes.stream().map(r -> toDto(r, bookmarkedIds)).collect(Collectors.toList());
    }

    public List<RecipeDto> searchRecipes(String query, String username) {
        List<Recipe> recipes = recipeRepository.searchByNameOrDescription(query);
        Set<Long> bookmarkedIds = getBookmarkedIds(username);
        return recipes.stream().map(r -> toDto(r, bookmarkedIds)).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> toggleBookmark(Long recipeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        boolean isBookmarked;
        if (user.getBookmarkedRecipes().contains(recipe)) {
            user.getBookmarkedRecipes().remove(recipe);
            isBookmarked = false;
        } else {
            user.getBookmarkedRecipes().add(recipe);
            isBookmarked = true;
        }
        userRepository.save(user);
        return Map.of("bookmarked", isBookmarked, "recipeId", recipeId);
    }

    public List<RecipeDto> getBookmarkedRecipes(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Set<Long> bookmarkedIds = user.getBookmarkedRecipes().stream()
                .map(Recipe::getId).collect(Collectors.toSet());
        return user.getBookmarkedRecipes().stream()
                .map(r -> toDto(r, bookmarkedIds))
                .collect(Collectors.toList());
    }

    private Set<Long> getBookmarkedIds(String username) {
        if (username == null) return Collections.emptySet();
        return userRepository.findByUsername(username)
                .map(u -> u.getBookmarkedRecipes().stream()
                        .map(Recipe::getId).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    private RecipeDto toDto(Recipe recipe, Set<Long> bookmarkedIds) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setCuisine(recipe.getCuisine());
        dto.setCategory(recipe.getCategory());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setCaloriesPerServing(recipe.getCaloriesPerServing());
        dto.setProteinPerServing(recipe.getProteinPerServing());
        dto.setFiberPerServing(recipe.getFiberPerServing());
        dto.setCarbsPerServing(recipe.getCarbsPerServing());
        dto.setFatPerServing(recipe.getFatPerServing());
        dto.setBookmarked(bookmarkedIds.contains(recipe.getId()));

        if (recipe.getIngredients() != null) {
            dto.setIngredients(recipe.getIngredients().stream().map(ri -> {
                RecipeDto.IngredientItemDto i = new RecipeDto.IngredientItemDto();
                i.setId(ri.getId());
                i.setIngredientName(ri.getIngredient().getName());
                i.setQuantity(ri.getQuantity());
                i.setUnit(ri.getUnit());
                i.setOptional(ri.getOptional());
                return i;
            }).collect(Collectors.toList()));
        }

        if (recipe.getSteps() != null) {
            dto.setSteps(recipe.getSteps().stream().map(s -> {
                RecipeDto.StepDto step = new RecipeDto.StepDto();
                step.setStepNumber(s.getStepNumber());
                step.setInstruction(s.getInstruction());
                return step;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}

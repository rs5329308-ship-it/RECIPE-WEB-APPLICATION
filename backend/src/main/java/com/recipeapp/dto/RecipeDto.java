package com.recipeapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private String cuisine;
    private String category;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String difficulty;
    private String imageUrl;
    private Integer caloriesPerServing;
    private Double proteinPerServing;
    private Double fiberPerServing;
    private Double carbsPerServing;
    private Double fatPerServing;
    private List<IngredientItemDto> ingredients;
    private List<StepDto> steps;
    private boolean bookmarked;

    @Data
    public static class IngredientItemDto {
        private Long id;
        private String ingredientName;
        private String quantity;
        private String unit;
        private Boolean optional;
    }

    @Data
    public static class StepDto {
        private Integer stepNumber;
        private String instruction;
    }
}

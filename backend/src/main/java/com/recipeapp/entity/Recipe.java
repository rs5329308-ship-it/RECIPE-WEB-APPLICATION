package com.recipeapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String cuisine;

    @Column(length = 50)
    private String category;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "cook_time")
    private Integer cookTime;

    private Integer servings;

    @Column(length = 20)
    private String difficulty;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Nutrition info
    @Column(name = "calories_per_serving")
    private Integer caloriesPerServing;

    @Column(name = "protein_per_serving")
    private Double proteinPerServing;

    @Column(name = "fiber_per_serving")
    private Double fiberPerServing;

    @Column(name = "carbs_per_serving")
    private Double carbsPerServing;

    @Column(name = "fat_per_serving")
    private Double fatPerServing;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<RecipeStep> steps;
}

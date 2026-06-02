package com.recipeapp.controller;

import com.recipeapp.dto.RecipeDto;
import com.recipeapp.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAllRecipes(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(recipeService.getAllRecipes(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(recipeService.getRecipeById(id, username));
    }

    @GetMapping("/search/ingredients")
    public ResponseEntity<List<RecipeDto>> searchByIngredients(
            @RequestParam List<String> ingredients,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(recipeService.searchByIngredients(ingredients, username));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDto>> searchRecipes(
            @RequestParam String query,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(recipeService.searchRecipes(query, username));
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<Map<String, Object>> toggleBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(recipeService.toggleBookmark(id, userDetails.getUsername()));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<RecipeDto>> getBookmarks(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(recipeService.getBookmarkedRecipes(userDetails.getUsername()));
    }
}

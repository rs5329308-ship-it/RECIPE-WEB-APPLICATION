package com.recipeapp.repository;

import com.recipeapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri JOIN ri.ingredient i " +
           "WHERE LOWER(i.name) IN :ingredientNames")
    List<Recipe> findByIngredientNames(@Param("ingredientNames") List<String> ingredientNames);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri JOIN ri.ingredient i " +
           "WHERE LOWER(i.name) IN :ingredientNames " +
           "GROUP BY r.id HAVING COUNT(DISTINCT i.name) = :count")
    List<Recipe> findByAllIngredients(@Param("ingredientNames") List<String> ingredientNames,
                                      @Param("count") long count);

    List<Recipe> findByCuisineIgnoreCase(String cuisine);
    List<Recipe> findByCategoryIgnoreCase(String category);
    List<Recipe> findByDifficultyIgnoreCase(String difficulty);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Recipe> searchByNameOrDescription(@Param("query") String query);
}

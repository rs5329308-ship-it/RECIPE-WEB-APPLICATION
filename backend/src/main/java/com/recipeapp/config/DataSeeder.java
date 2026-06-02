package com.recipeapp.config;

import com.recipeapp.entity.*;
import com.recipeapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (recipeRepository.count() > 0) {
            log.info("Database already seeded, skipping...");
            return;
        }
        log.info("Seeding database with sample recipes...");
        seedIngredients();
        seedRecipes();
        log.info("Database seeding complete!");
    }

    private Map<String, Ingredient> ingredientMap = new HashMap<>();

    private void seedIngredients() {
        String[][] ingredients = {
            {"Chicken Breast", "Protein"}, {"Eggs", "Protein"}, {"Salmon", "Protein"},
            {"Ground Beef", "Protein"}, {"Tofu", "Protein"}, {"Chickpeas", "Legume"},
            {"Lentils", "Legume"}, {"Black Beans", "Legume"},
            {"Pasta", "Grain"}, {"Rice", "Grain"}, {"Bread", "Grain"}, {"Oats", "Grain"},
            {"All-Purpose Flour", "Grain"}, {"Quinoa", "Grain"},
            {"Tomatoes", "Vegetable"}, {"Onion", "Vegetable"}, {"Garlic", "Vegetable"},
            {"Spinach", "Vegetable"}, {"Bell Pepper", "Vegetable"}, {"Broccoli", "Vegetable"},
            {"Carrot", "Vegetable"}, {"Potato", "Vegetable"}, {"Mushrooms", "Vegetable"},
            {"Zucchini", "Vegetable"}, {"Cucumber", "Vegetable"},
            {"Milk", "Dairy"}, {"Butter", "Dairy"}, {"Cheese", "Dairy"},
            {"Heavy Cream", "Dairy"}, {"Greek Yogurt", "Dairy"},
            {"Olive Oil", "Fat"}, {"Coconut Oil", "Fat"},
            {"Salt", "Seasoning"}, {"Black Pepper", "Seasoning"}, {"Cumin", "Seasoning"},
            {"Paprika", "Seasoning"}, {"Turmeric", "Seasoning"}, {"Cinnamon", "Seasoning"},
            {"Oregano", "Seasoning"}, {"Basil", "Seasoning"}, {"Chili Powder", "Seasoning"},
            {"Ginger", "Seasoning"}, {"Soy Sauce", "Condiment"}, {"Lemon", "Fruit"},
            {"Sugar", "Sweetener"}, {"Honey", "Sweetener"},
            {"Chicken Broth", "Liquid"}, {"Vegetable Broth", "Liquid"},
            {"Paneer", "Dairy"}, {"Garam Masala", "Seasoning"}, {"Coriander", "Seasoning"}
        };
        for (String[] ing : ingredients) {
            Ingredient ingredient = ingredientRepository.save(
                Ingredient.builder().name(ing[0]).category(ing[1]).build()
            );
            ingredientMap.put(ing[0], ingredient);
        }
    }

    private void seedRecipes() {
        // 1. Spaghetti Bolognese
        Recipe spagBol = Recipe.builder()
            .name("Spaghetti Bolognese")
            .description("A classic Italian meat sauce served over al dente pasta. Rich, hearty, and deeply satisfying.")
            .cuisine("Italian").category("Dinner")
            .prepTime(15).cookTime(45).servings(4).difficulty("Easy")
            .imageUrl("https://images.unsplash.com/photo-1622973536968-3ead9e780960?w=600")
            .caloriesPerServing(520).proteinPerServing(28.0).fiberPerServing(4.5)
            .carbsPerServing(58.0).fatPerServing(18.0).build();
        spagBol = recipeRepository.save(spagBol);
        addIngredients(spagBol, new String[][]{
            {"Pasta","400","g"}, {"Ground Beef","500","g"}, {"Tomatoes","400","g"},
            {"Onion","1","whole"}, {"Garlic","4","cloves"}, {"Olive Oil","2","tbsp"},
            {"Salt","1","tsp"}, {"Black Pepper","0.5","tsp"}, {"Basil","1","tsp"}, {"Cheese","50","g"}
        });
        addSteps(spagBol, new String[]{
            "Boil salted water and cook pasta according to package directions. Reserve 1 cup pasta water.",
            "Heat olive oil in a large pan over medium heat. Sauté diced onion until translucent, about 5 minutes.",
            "Add minced garlic and cook for 1 minute until fragrant.",
            "Add ground beef and cook, breaking up lumps, until browned, about 8 minutes.",
            "Stir in crushed tomatoes, salt, pepper, and dried basil. Simmer on low for 30 minutes.",
            "Toss pasta with sauce, adding pasta water as needed. Top with grated cheese and serve."
        });

        // 2. Butter Chicken (Murgh Makhani)
        Recipe butterChicken = Recipe.builder()
            .name("Butter Chicken")
            .description("India's most beloved curry — tender chicken in a velvety, spiced tomato-butter sauce.")
            .cuisine("Indian").category("Dinner")
            .prepTime(20).cookTime(40).servings(4).difficulty("Medium")
            .imageUrl("https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=600")
            .caloriesPerServing(480).proteinPerServing(35.0).fiberPerServing(3.0)
            .carbsPerServing(22.0).fatPerServing(28.0).build();
        butterChicken = recipeRepository.save(butterChicken);
        addIngredients(butterChicken, new String[][]{
            {"Chicken Breast","600","g"}, {"Tomatoes","400","g"}, {"Butter","50","g"},
            {"Heavy Cream","100","ml"}, {"Onion","1","whole"}, {"Garlic","4","cloves"},
            {"Ginger","1","tbsp"}, {"Garam Masala","2","tsp"}, {"Cumin","1","tsp"},
            {"Paprika","1","tsp"}, {"Turmeric","0.5","tsp"}, {"Salt","1","tsp"}
        });
        addSteps(butterChicken, new String[]{
            "Marinate chicken in yogurt, half the spices, and lemon juice for at least 30 minutes.",
            "Cook marinated chicken in a hot pan until charred and cooked through. Set aside.",
            "Melt butter, sauté onions until golden. Add garlic and ginger paste, cook 2 minutes.",
            "Add blended tomatoes, remaining spices, and simmer 15 minutes until thick.",
            "Blend sauce smooth, return to pan, add cream and chicken. Simmer 10 minutes.",
            "Garnish with cream and fresh coriander. Serve with rice or naan."
        });

        // 3. Avocado Toast with Eggs
        Recipe avoToast = Recipe.builder()
            .name("Avocado Toast with Poached Eggs")
            .description("A trendy yet nutritious breakfast with creamy avocado and perfectly poached eggs on sourdough.")
            .cuisine("American").category("Breakfast")
            .prepTime(5).cookTime(10).servings(2).difficulty("Easy")
            .imageUrl("https://images.unsplash.com/photo-1525351484163-7529414344d8?w=600")
            .caloriesPerServing(320).proteinPerServing(14.0).fiberPerServing(7.0)
            .carbsPerServing(28.0).fatPerServing(18.0).build();
        avoToast = recipeRepository.save(avoToast);
        addIngredients(avoToast, new String[][]{
            {"Bread","4","slices"}, {"Eggs","4","whole"}, {"Lemon","1","whole"},
            {"Salt","0.5","tsp"}, {"Black Pepper","0.25","tsp"}, {"Olive Oil","1","tsp"}
        });
        addSteps(avoToast, new String[]{
            "Toast bread slices until golden and crispy.",
            "Bring a pot of water to a gentle simmer. Add a splash of vinegar.",
            "Crack eggs into small cups and gently slide into simmering water. Poach for 3-4 minutes.",
            "Mash avocado with lemon juice, salt, and pepper.",
            "Spread avocado on toast, top with poached eggs.",
            "Season with flaky salt, pepper, and red chili flakes if desired."
        });

        // 4. Chicken Stir Fry
        Recipe stirFry = Recipe.builder()
            .name("Chicken & Vegetable Stir Fry")
            .description("A quick, colorful stir fry packed with tender chicken and crisp vegetables in a savory sauce.")
            .cuisine("Chinese").category("Dinner")
            .prepTime(15).cookTime(15).servings(3).difficulty("Easy")
            .imageUrl("https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=600")
            .caloriesPerServing(310).proteinPerServing(30.0).fiberPerServing(5.0)
            .carbsPerServing(25.0).fatPerServing(10.0).build();
        stirFry = recipeRepository.save(stirFry);
        addIngredients(stirFry, new String[][]{
            {"Chicken Breast","400","g"}, {"Broccoli","200","g"}, {"Bell Pepper","2","whole"},
            {"Carrot","2","whole"}, {"Soy Sauce","3","tbsp"}, {"Garlic","3","cloves"},
            {"Ginger","1","tsp"}, {"Olive Oil","2","tbsp"}, {"Rice","300","g"}
        });
        addSteps(stirFry, new String[]{
            "Cook rice according to package directions.",
            "Slice chicken into thin strips. Chop all vegetables into bite-sized pieces.",
            "Mix soy sauce, ginger, and a pinch of sugar for the sauce.",
            "Heat oil in a wok over high heat. Stir fry chicken 5 minutes until cooked.",
            "Add harder vegetables (carrot, broccoli) and stir fry 3 minutes.",
            "Add remaining vegetables and sauce. Toss everything for 2 minutes. Serve over rice."
        });

        // 5. Lentil Soup
        Recipe lentilSoup = Recipe.builder()
            .name("Red Lentil Soup")
            .description("A warming, nourishing soup loaded with plant protein and aromatic spices. Vegan-friendly.")
            .cuisine("Middle Eastern").category("Lunch")
            .prepTime(10).cookTime(30).servings(4).difficulty("Easy")
            .imageUrl("https://images.unsplash.com/photo-1547592180-85f173990554?w=600")
            .caloriesPerServing(280).proteinPerServing(16.0).fiberPerServing(11.0)
            .carbsPerServing(45.0).fatPerServing(5.0).build();
        lentilSoup = recipeRepository.save(lentilSoup);
        addIngredients(lentilSoup, new String[][]{
            {"Lentils","300","g"}, {"Onion","1","whole"}, {"Garlic","3","cloves"},
            {"Tomatoes","200","g"}, {"Carrot","2","whole"}, {"Cumin","2","tsp"},
            {"Turmeric","0.5","tsp"}, {"Olive Oil","2","tbsp"}, {"Vegetable Broth","1","L"},
            {"Salt","1","tsp"}, {"Lemon","1","whole"}
        });
        addSteps(lentilSoup, new String[]{
            "Rinse lentils thoroughly under cold water.",
            "Sauté diced onion in olive oil until golden. Add garlic, cumin, and turmeric.",
            "Add diced carrots and tomatoes, cook for 3 minutes.",
            "Add lentils and vegetable broth. Bring to a boil.",
            "Reduce heat and simmer 20-25 minutes until lentils are soft.",
            "Blend half the soup for a creamier texture. Season with salt and a squeeze of lemon."
        });

        // 6. Paneer Tikka Masala
        Recipe paneerTikka = Recipe.builder()
            .name("Paneer Tikka Masala")
            .description("Smoky chargrilled paneer cubes in a rich, spiced tomato gravy. A vegetarian classic.")
            .cuisine("Indian").category("Dinner")
            .prepTime(20).cookTime(35).servings(4).difficulty("Medium")
            .imageUrl("https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600")
            .caloriesPerServing(420).proteinPerServing(22.0).fiberPerServing(4.0)
            .carbsPerServing(20.0).fatPerServing(28.0).build();
        paneerTikka = recipeRepository.save(paneerTikka);
        addIngredients(paneerTikka, new String[][]{
            {"Paneer","400","g"}, {"Tomatoes","400","g"}, {"Onion","2","whole"},
            {"Garlic","4","cloves"}, {"Ginger","1","tbsp"}, {"Heavy Cream","100","ml"},
            {"Butter","30","g"}, {"Garam Masala","2","tsp"}, {"Paprika","1","tsp"},
            {"Turmeric","0.5","tsp"}, {"Coriander","1","tsp"}, {"Salt","1","tsp"}
        });
        addSteps(paneerTikka, new String[]{
            "Cube paneer and marinate with yogurt, garam masala, paprika, and salt for 30 minutes.",
            "Grill or pan-fry paneer until charred. Set aside.",
            "Blend onion, tomatoes, garlic, and ginger into a smooth paste.",
            "Cook paste in butter for 10 minutes until oil separates.",
            "Add all dry spices and cook for 2 minutes.",
            "Add grilled paneer and cream. Simmer 10 minutes. Garnish with coriander and cream."
        });

        // 7. Greek Salad
        Recipe greekSalad = Recipe.builder()
            .name("Classic Greek Salad")
            .description("A refreshing Mediterranean salad with crisp vegetables, olives, and feta cheese.")
            .cuisine("Greek").category("Lunch")
            .prepTime(10).cookTime(0).servings(2).difficulty("Easy")
            .imageUrl("https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=600")
            .caloriesPerServing(220).proteinPerServing(8.0).fiberPerServing(4.5)
            .carbsPerServing(15.0).fatPerServing(15.0).build();
        greekSalad = recipeRepository.save(greekSalad);
        addIngredients(greekSalad, new String[][]{
            {"Tomatoes","3","whole"}, {"Cucumber","1","whole"}, {"Bell Pepper","1","whole"},
            {"Onion","0.5","whole"}, {"Cheese","100","g"}, {"Olive Oil","3","tbsp"},
            {"Lemon","1","whole"}, {"Salt","0.5","tsp"}, {"Black Pepper","0.25","tsp"},
            {"Oregano","1","tsp"}
        });
        addSteps(greekSalad, new String[]{
            "Chop tomatoes, cucumber, and bell pepper into large chunks.",
            "Thinly slice red onion and separate into rings.",
            "Combine all vegetables in a large bowl.",
            "Drizzle with olive oil and lemon juice.",
            "Season with salt, pepper, and dried oregano.",
            "Top with crumbled feta cheese and serve immediately."
        });

        // 8. Oatmeal Bowl
        Recipe oatmeal = Recipe.builder()
            .name("Power Oatmeal Bowl")
            .description("A hearty, nutrient-dense oatmeal breakfast loaded with fiber, protein and natural sweetness.")
            .cuisine("American").category("Breakfast")
            .prepTime(5).cookTime(10).servings(1).difficulty("Easy")
            .imageUrl("https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600")
            .caloriesPerServing(350).proteinPerServing(12.0).fiberPerServing(8.0)
            .carbsPerServing(55.0).fatPerServing(8.0).build();
        oatmeal = recipeRepository.save(oatmeal);
        addIngredients(oatmeal, new String[][]{
            {"Oats","100","g"}, {"Milk","250","ml"}, {"Honey","1","tbsp"},
            {"Cinnamon","0.5","tsp"}, {"Salt","0.25","tsp"}
        });
        addSteps(oatmeal, new String[]{
            "Bring milk to a gentle simmer in a saucepan.",
            "Add oats and a pinch of salt. Cook on medium-low heat, stirring frequently.",
            "Cook for 5-7 minutes until oats are creamy and thick.",
            "Remove from heat, stir in cinnamon and honey.",
            "Pour into a bowl and top with your favourite fruits, nuts, or seeds.",
            "Drizzle extra honey if desired and serve warm."
        });

        // 9. Salmon with Quinoa
        Recipe salmon = Recipe.builder()
            .name("Pan-Seared Salmon with Quinoa")
            .description("Omega-3 rich salmon with fluffy protein-packed quinoa and steamed broccoli. Perfect clean meal.")
            .cuisine("American").category("Dinner")
            .prepTime(10).cookTime(20).servings(2).difficulty("Medium")
            .imageUrl("https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=600")
            .caloriesPerServing(490).proteinPerServing(42.0).fiberPerServing(6.0)
            .carbsPerServing(35.0).fatPerServing(18.0).build();
        salmon = recipeRepository.save(salmon);
        addIngredients(salmon, new String[][]{
            {"Salmon","300","g"}, {"Quinoa","150","g"}, {"Broccoli","200","g"},
            {"Lemon","1","whole"}, {"Garlic","2","cloves"}, {"Olive Oil","2","tbsp"},
            {"Salt","1","tsp"}, {"Black Pepper","0.5","tsp"}
        });
        addSteps(salmon, new String[]{
            "Rinse quinoa and cook in 300ml water for 15 minutes until fluffy.",
            "Steam or boil broccoli until tender-crisp, about 5 minutes.",
            "Pat salmon dry and season with salt, pepper, and garlic.",
            "Heat oil in a pan over medium-high heat. Sear salmon skin-side up for 4 minutes.",
            "Flip salmon and cook 3 more minutes until cooked through.",
            "Serve salmon over quinoa with broccoli and a squeeze of fresh lemon."
        });

        // 10. Tofu Curry
        Recipe tofuCurry = Recipe.builder()
            .name("Coconut Tofu Curry")
            .description("A vibrant vegan curry with silky tofu in aromatic coconut sauce — comforting and nourishing.")
            .cuisine("Thai").category("Dinner")
            .prepTime(15).cookTime(25).servings(3).difficulty("Medium")
            .imageUrl("https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=600")
            .caloriesPerServing(360).proteinPerServing(18.0).fiberPerServing(5.0)
            .carbsPerServing(30.0).fatPerServing(20.0).build();
        tofuCurry = recipeRepository.save(tofuCurry);
        addIngredients(tofuCurry, new String[][]{
            {"Tofu","400","g"}, {"Bell Pepper","2","whole"}, {"Spinach","100","g"},
            {"Onion","1","whole"}, {"Garlic","3","cloves"}, {"Ginger","1","tsp"},
            {"Turmeric","0.5","tsp"}, {"Cumin","1","tsp"}, {"Coconut Oil","2","tbsp"},
            {"Salt","1","tsp"}, {"Rice","200","g"}
        });
        addSteps(tofuCurry, new String[]{
            "Press tofu to remove excess water, then cube it.",
            "Fry tofu cubes in coconut oil until golden on all sides. Set aside.",
            "Sauté onion in the same pan, add garlic and ginger.",
            "Add spices and cook for 1 minute until fragrant.",
            "Add bell peppers and a splash of vegetable broth or coconut milk. Simmer 10 minutes.",
            "Add spinach and tofu, cook 3 more minutes. Serve over steamed rice."
        });
    }

    private void addIngredients(Recipe recipe, String[][] data) {
        List<RecipeIngredient> list = new ArrayList<>();
        for (String[] row : data) {
            Ingredient ing = ingredientMap.get(row[0]);
            if (ing != null) {
                list.add(RecipeIngredient.builder()
                    .recipe(recipe).ingredient(ing)
                    .quantity(row[1]).unit(row[2]).optional(false)
                    .build());
            }
        }
        recipe.setIngredients(list);
        recipeRepository.save(recipe);
    }

    private void addSteps(Recipe recipe, String[] steps) {
        List<RecipeStep> list = new ArrayList<>();
        for (int i = 0; i < steps.length; i++) {
            list.add(RecipeStep.builder()
                .recipe(recipe).stepNumber(i + 1).instruction(steps[i]).build());
        }
        recipe.setSteps(list);
        recipeRepository.save(recipe);
    }
}

import React, { useState, useEffect, useCallback } from 'react';
import { recipeService } from '../services/api';
import RecipeCard from '../components/RecipeCard';

const CUISINES = ['All', 'Italian', 'Indian', 'Chinese', 'American', 'Greek', 'Thai', 'Middle Eastern'];
const CATEGORIES = ['All', 'Breakfast', 'Lunch', 'Dinner', 'Snack', 'Dessert'];

export default function HomePage() {
  const [recipes, setRecipes] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [ingredientInput, setIngredientInput] = useState('');
  const [selectedIngredients, setSelectedIngredients] = useState([]);
  const [activeCuisine, setActiveCuisine] = useState('All');
  const [activeCategory, setActiveCategory] = useState('All');

  const loadRecipes = useCallback(async () => {
    setLoading(true);
    try {
      const res = await recipeService.getAll();
      setRecipes(res.data);
      setFiltered(res.data);
    } catch {
      console.error('Failed to load recipes');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { loadRecipes(); }, [loadRecipes]);

  useEffect(() => {
    let result = [...recipes];
    if (activeCuisine !== 'All') result = result.filter(r => r.cuisine === activeCuisine);
    if (activeCategory !== 'All') result = result.filter(r => r.category === activeCategory);
    setFiltered(result);
  }, [activeCuisine, activeCategory, recipes]);

  const handleTextSearch = async (e) => {
    e.preventDefault();
    if (!searchQuery.trim()) { setFiltered(recipes); return; }
    setLoading(true);
    try {
      const res = await recipeService.search(searchQuery);
      setFiltered(res.data);
      setActiveCuisine('All'); setActiveCategory('All');
    } catch { } finally { setLoading(false); }
  };

  const addIngredient = () => {
    const val = ingredientInput.trim();
    if (val && !selectedIngredients.includes(val)) {
      setSelectedIngredients(prev => [...prev, val]);
    }
    setIngredientInput('');
  };

  const removeIngredient = (ing) => {
    setSelectedIngredients(prev => prev.filter(i => i !== ing));
  };

  const searchByIngredients = async () => {
    if (!selectedIngredients.length) return;
    setLoading(true);
    try {
      const res = await recipeService.searchByIngredients(selectedIngredients);
      setFiltered(res.data);
      setActiveCuisine('All'); setActiveCategory('All');
    } catch { } finally { setLoading(false); }
  };

  const clearSearch = () => {
    setSearchQuery(''); setSelectedIngredients([]);
    setActiveCuisine('All'); setActiveCategory('All');
    setFiltered(recipes);
  };

  const handleBookmarkToggle = (recipeId, isBookmarked) => {
    const update = list => list.map(r => r.id === recipeId ? { ...r, bookmarked: isBookmarked } : r);
    setRecipes(update);
    setFiltered(update);
  };

  return (
    <div>
      {/* Hero */}
      <section className="hero">
        <h1>Find recipes with<br /><em>what you have</em></h1>
        <p>Search by ingredients, cuisine, or dish name. Discover meals tailored to your kitchen.</p>

        {/* Text search */}
        <form className="search-bar" onSubmit={handleTextSearch} style={{ marginBottom: 32 }}>
          <input
            type="text" placeholder="Search by recipe name or keyword..."
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
          />
          <button type="submit">Search</button>
        </form>

        {/* Ingredient search */}
        <div className="ingredient-search-area">
          <h3>Or search by ingredients</h3>
          <div className="ingredient-tags">
            {selectedIngredients.map(ing => (
              <span key={ing} className="tag">
                {ing}
                <button onClick={() => removeIngredient(ing)}>×</button>
              </span>
            ))}
          </div>
          <div className="ingredient-input-row">
            <input
              type="text" placeholder="Add ingredient (e.g. chicken, tomatoes, rice)..."
              value={ingredientInput}
              onChange={e => setIngredientInput(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && (e.preventDefault(), addIngredient())}
            />
            <button className="btn btn-ghost btn-sm" onClick={addIngredient}>Add</button>
            {selectedIngredients.length > 0 && (
              <button className="btn btn-primary btn-sm" onClick={searchByIngredients}>
                Find Recipes
              </button>
            )}
          </div>
        </div>
      </section>

      {/* Results */}
      <section className="section" style={{ paddingTop: 0 }}>
        <div className="container">
          {/* Cuisine Filter */}
          <div style={{ marginBottom: 12 }}>
            <div className="filters-row">
              {CUISINES.map(c => (
                <button
                  key={c} className={`filter-chip ${activeCuisine === c ? 'active' : ''}`}
                  onClick={() => setActiveCuisine(c)}
                >{c}</button>
              ))}
            </div>
            <div className="filters-row">
              {CATEGORIES.map(c => (
                <button
                  key={c} className={`filter-chip ${activeCategory === c ? 'active' : ''}`}
                  onClick={() => setActiveCategory(c)}
                >{c}</button>
              ))}
            </div>
          </div>

          <div className="section-header" style={{ marginBottom: 20 }}>
            <h2>{filtered.length} Recipe{filtered.length !== 1 ? 's' : ''}</h2>
            {(searchQuery || selectedIngredients.length > 0 || activeCuisine !== 'All' || activeCategory !== 'All') && (
              <button className="btn btn-ghost btn-sm" onClick={clearSearch}>Clear filters</button>
            )}
          </div>

          {loading ? (
            <div className="loading"><div className="spinner" /><span>Finding recipes...</span></div>
          ) : filtered.length === 0 ? (
            <div className="empty-state">
              <div className="icon">🥘</div>
              <h3>No recipes found</h3>
              <p>Try different ingredients or search terms</p>
            </div>
          ) : (
            <div className="recipe-grid">
              {filtered.map(recipe => (
                <RecipeCard
                  key={recipe.id} recipe={recipe}
                  onBookmarkToggle={handleBookmarkToggle}
                />
              ))}
            </div>
          )}
        </div>
      </section>
    </div>
  );
}

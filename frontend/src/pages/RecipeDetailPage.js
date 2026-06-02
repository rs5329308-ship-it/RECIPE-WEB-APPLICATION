import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { recipeService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';

export default function RecipeDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth();
  const [recipe, setRecipe] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    recipeService.getById(id)
      .then(res => setRecipe(res.data))
      .catch(() => navigate('/'))
      .finally(() => setLoading(false));
  }, [id, navigate]);

  const handleBookmark = async () => {
    if (!isLoggedIn) { toast.info('Please sign in to bookmark recipes'); return; }
    try {
      const res = await recipeService.toggleBookmark(recipe.id);
      setRecipe(r => ({ ...r, bookmarked: res.data.bookmarked }));
      toast.success(res.data.bookmarked ? '📌 Bookmarked!' : 'Bookmark removed');
    } catch { toast.error('Failed to update bookmark'); }
  };

  if (loading) return <div className="loading"><div className="spinner" /><span>Loading recipe...</span></div>;
  if (!recipe) return null;

  const totalTime = (recipe.prepTime || 0) + (recipe.cookTime || 0);

  return (
    <div className="recipe-detail">
      <Link to="/" className="back-btn">← Back to Recipes</Link>

      <div className="recipe-detail-hero">
        <img
          src={recipe.imageUrl || 'https://images.unsplash.com/photo-1495521821757-a1efb6729352?w=900'}
          alt={recipe.name}
          onError={e => { e.target.src = 'https://images.unsplash.com/photo-1495521821757-a1efb6729352?w=900'; }}
        />
        <div className="recipe-detail-hero-overlay">
          <h1>{recipe.name}</h1>
          <div className="meta">
            {recipe.cuisine && <span>🌍 {recipe.cuisine}</span>}
            {recipe.category && <span>🏷 {recipe.category}</span>}
            {totalTime > 0 && <span>⏱ {totalTime} min total</span>}
            {recipe.servings && <span>🍽 {recipe.servings} servings</span>}
            {recipe.difficulty && <span>📊 {recipe.difficulty}</span>}
          </div>
        </div>
      </div>

      {/* Actions */}
      <div style={{ display: 'flex', gap: 12, marginBottom: 28 }}>
        <button
          className={`btn ${recipe.bookmarked ? 'btn-primary' : 'btn-outline'}`}
          onClick={handleBookmark}
        >
          {recipe.bookmarked ? '🔖 Saved' : '🔖 Save Recipe'}
        </button>
      </div>

      {/* Description */}
      {recipe.description && (
        <p style={{ fontSize: 16, color: 'var(--warm-gray)', lineHeight: 1.7, marginBottom: 28 }}>
          {recipe.description}
        </p>
      )}

      {/* Nutrition */}
      {(recipe.caloriesPerServing || recipe.proteinPerServing) && (
        <div style={{ marginBottom: 36 }}>
          <h2 style={{ marginBottom: 16, fontSize: 22 }}>Nutrition per serving</h2>
          <div className="nutrition-grid">
            <div className="nutrition-card">
              <div className="value">{recipe.caloriesPerServing || '—'}</div>
              <div className="label">🔥 Calories</div>
            </div>
            <div className="nutrition-card">
              <div className="value">{recipe.proteinPerServing ? `${recipe.proteinPerServing}g` : '—'}</div>
              <div className="label">💪 Protein</div>
            </div>
            <div className="nutrition-card">
              <div className="value">{recipe.fiberPerServing ? `${recipe.fiberPerServing}g` : '—'}</div>
              <div className="label">🌾 Fiber</div>
            </div>
            <div className="nutrition-card">
              <div className="value">{recipe.carbsPerServing ? `${recipe.carbsPerServing}g` : '—'}</div>
              <div className="label">🍞 Carbs</div>
            </div>
            <div className="nutrition-card">
              <div className="value">{recipe.fatPerServing ? `${recipe.fatPerServing}g` : '—'}</div>
              <div className="label">🥑 Fat</div>
            </div>
          </div>
        </div>
      )}

      {/* Ingredients */}
      {recipe.ingredients?.length > 0 && (
        <div style={{ marginBottom: 36 }}>
          <h2 style={{ marginBottom: 16, fontSize: 22 }}>
            Ingredients
            <span style={{ fontSize: 14, fontFamily: 'DM Sans', fontWeight: 400, color: 'var(--warm-gray)', marginLeft: 12 }}>
              for {recipe.servings} servings
            </span>
          </h2>
          <ul className="ingredients-list">
            {recipe.ingredients.map((ing, i) => (
              <li key={i}>
                <span style={{ fontWeight: 600 }}>
                  {ing.quantity} {ing.unit}
                </span>
                <span>{ing.ingredientName}</span>
                {ing.optional && <span style={{ fontSize: 12, color: 'var(--warm-gray)', marginLeft: 'auto' }}>(optional)</span>}
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Steps */}
      {recipe.steps?.length > 0 && (
        <div>
          <h2 style={{ marginBottom: 20, fontSize: 22 }}>Instructions</h2>
          <ol className="steps-list">
            {recipe.steps.map((step) => (
              <li key={step.stepNumber} className="step-item">
                <div className="step-num">{step.stepNumber}</div>
                <div className="step-text">{step.instruction}</div>
              </li>
            ))}
          </ol>
        </div>
      )}
    </div>
  );
}

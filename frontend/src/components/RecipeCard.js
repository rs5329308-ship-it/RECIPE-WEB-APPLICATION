import React from 'react';
import { useNavigate } from 'react-router-dom';
import { recipeService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';

export default function RecipeCard({ recipe, onBookmarkToggle }) {
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth();

  const difficultyClass = `badge badge-difficulty-${recipe.difficulty?.toLowerCase() || 'easy'}`;

  const handleBookmark = async (e) => {
    e.stopPropagation();
    if (!isLoggedIn) {
      toast.info('Please sign in to bookmark recipes');
      return;
    }
    try {
      const res = await recipeService.toggleBookmark(recipe.id);
      onBookmarkToggle?.(recipe.id, res.data.bookmarked);
      toast.success(res.data.bookmarked ? '📌 Bookmarked!' : 'Bookmark removed');
    } catch {
      toast.error('Failed to update bookmark');
    }
  };

  return (
    <div className="recipe-card" onClick={() => navigate(`/recipe/${recipe.id}`)}>
      <div className="recipe-card-img">
        <img
          src={recipe.imageUrl || `https://images.unsplash.com/photo-1495521821757-a1efb6729352?w=600`}
          alt={recipe.name}
          onError={e => { e.target.src = 'https://images.unsplash.com/photo-1495521821757-a1efb6729352?w=600'; }}
        />
        <div className="card-badges">
          {recipe.cuisine && <span className="badge badge-cuisine">{recipe.cuisine}</span>}
          {recipe.difficulty && <span className={difficultyClass}>{recipe.difficulty}</span>}
        </div>
        <button
          className={`bookmark-btn ${recipe.bookmarked ? 'active' : ''}`}
          onClick={handleBookmark}
          title={recipe.bookmarked ? 'Remove bookmark' : 'Bookmark'}
        >
          {recipe.bookmarked ? '🔖' : '🔖'}
        </button>
      </div>
      <div className="recipe-card-body">
        <h3>{recipe.name}</h3>
        <p>{recipe.description}</p>
        <div className="recipe-meta">
          {recipe.prepTime && <span>⏱ {recipe.prepTime + (recipe.cookTime || 0)} min</span>}
          {recipe.servings && <span>🍽 {recipe.servings} servings</span>}
          {recipe.category && <span>🏷 {recipe.category}</span>}
        </div>
        {(recipe.caloriesPerServing || recipe.proteinPerServing || recipe.fiberPerServing) && (
          <div className="nutrition-bar">
            <div className="nutrition-item">
              <div className="val">{recipe.caloriesPerServing || '—'}</div>
              <div className="lbl">Calories</div>
            </div>
            <div className="nutrition-item">
              <div className="val">{recipe.proteinPerServing ? `${recipe.proteinPerServing}g` : '—'}</div>
              <div className="lbl">Protein</div>
            </div>
            <div className="nutrition-item">
              <div className="val">{recipe.fiberPerServing ? `${recipe.fiberPerServing}g` : '—'}</div>
              <div className="lbl">Fiber</div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

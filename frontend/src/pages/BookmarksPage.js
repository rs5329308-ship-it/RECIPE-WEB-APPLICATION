import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { recipeService } from '../services/api';
import RecipeCard from '../components/RecipeCard';

export default function BookmarksPage() {
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    recipeService.getBookmarks()
      .then(res => setBookmarks(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const handleBookmarkToggle = (recipeId, isBookmarked) => {
    if (!isBookmarked) setBookmarks(prev => prev.filter(r => r.id !== recipeId));
  };

  return (
    <div className="section">
      <div className="container">
        <div className="section-header">
          <h2>📌 Saved Recipes</h2>
          <Link to="/" className="btn btn-ghost btn-sm">Browse More</Link>
        </div>

        {loading ? (
          <div className="loading"><div className="spinner" /><span>Loading your bookmarks...</span></div>
        ) : bookmarks.length === 0 ? (
          <div className="empty-state">
            <div className="icon">📌</div>
            <h3>No saved recipes yet</h3>
            <p>Browse recipes and click the bookmark icon to save your favourites</p>
            <Link to="/" className="btn btn-primary" style={{ marginTop: 24, display: 'inline-flex' }}>
              Discover Recipes
            </Link>
          </div>
        ) : (
          <div className="recipe-grid">
            {bookmarks.map(recipe => (
              <RecipeCard
                key={recipe.id}
                recipe={{ ...recipe, bookmarked: true }}
                onBookmarkToggle={handleBookmarkToggle}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

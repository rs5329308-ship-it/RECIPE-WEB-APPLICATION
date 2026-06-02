import React from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';

export default function Navbar() {
  const { user, logout, isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    toast.success('Logged out successfully');
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="navbar-brand">
          <span className="logo-icon">🍳</span>
          <span>cookmate</span>
        </Link>
        <div className="navbar-links">
          <NavLink to="/" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`} end>
            Discover
          </NavLink>
          {isLoggedIn && (
            <NavLink to="/bookmarks" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
              📌 Saved
            </NavLink>
          )}
          {isLoggedIn ? (
            <div className="nav-user">
              <div className="nav-avatar">
                {(user.fullName || user.username || '?')[0].toUpperCase()}
              </div>
              <span style={{ fontSize: 14, fontWeight: 500, color: 'var(--warm-gray)' }}>
                {user.fullName || user.username}
              </span>
              <button className="btn-nav" onClick={handleLogout}>Sign Out</button>
            </div>
          ) : (
            <>
              <Link to="/login" className="nav-link">Sign In</Link>
              <Link to="/register"><button className="btn-nav">Get Started</button></Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}

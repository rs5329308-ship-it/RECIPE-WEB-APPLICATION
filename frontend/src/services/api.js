import axios from 'axios';

const API_BASE = '/api';

const api = axios.create({ baseURL: API_BASE });

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

export const authService = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
};

export const recipeService = {
  getAll: () => api.get('/recipes'),
  getById: (id) => api.get(`/recipes/${id}`),
  search: (query) => api.get(`/recipes/search?query=${query}`),
  searchByIngredients: (ingredients) =>
    api.get(`/recipes/search/ingredients?ingredients=${ingredients.join(',')}`),
  toggleBookmark: (id) => api.post(`/recipes/${id}/bookmark`),
  getBookmarks: () => api.get('/recipes/bookmarks'),
};

export const ingredientService = {
  getAll: () => api.get('/ingredients'),
  search: (q) => api.get(`/ingredients/search?query=${q}`),
};

export default api;

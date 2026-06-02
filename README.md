# 🍳 FlavorFind — Recipe Finder App

A full-stack web application to discover recipes by ingredients, with user authentication and bookmarking.

**Stack:** React 18 · Spring Boot 3.2 · MySQL 8 · JWT Auth

---

## ✨ Features

- 🔍 **Search by ingredients** — add multiple ingredients, get matching recipes
- 📝 **Keyword search** — search by recipe name or description
- 🏷 **Filter by cuisine & category** — Italian, Indian, Chinese, etc.
- 📊 **Nutrition info** — calories, protein, fiber, carbs, and fat per serving
- 🔐 **User auth** — register/login with JWT tokens
- 📌 **Bookmarks** — save favourite recipes (login required)
- 🌱 **Auto-seeded data** — 10 sample recipes loaded on first run

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8+
- Maven 3.8+

---

### 1. Database Setup

Open MySQL and run:
```sql
CREATE DATABASE IF NOT EXISTS recipedb;
```

> The app auto-creates tables on first run via `spring.jpa.hibernate.ddl-auto=update`

---

### 2. Configure Database Credentials

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.username=root       # ← your MySQL username
spring.datasource.password=root       # ← your MySQL password
```

---

### 3. Run the Backend

```bash
cd backend
mvn spring-boot:run
```

Backend starts on **http://localhost:8080**

The database will be auto-seeded with 10 recipes on first startup.

---

### 4. Run the Frontend

```bash
cd frontend
npm install
npm start
```

Frontend starts on **http://localhost:3000**

---

## 📁 Project Structure

```
recipe-app/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/recipeapp/
│       ├── RecipeApplication.java
│       ├── config/
│       │   ├── DataSeeder.java        ← Seeds 10 sample recipes
│       │   └── SecurityConfig.java
│       ├── controller/
│       │   ├── AuthController.java
│       │   ├── RecipeController.java
│       │   └── IngredientController.java
│       ├── dto/
│       │   ├── AuthDto.java
│       │   └── RecipeDto.java
│       ├── entity/
│       │   ├── User.java
│       │   ├── Recipe.java
│       │   ├── Ingredient.java
│       │   ├── RecipeIngredient.java
│       │   └── RecipeStep.java
│       ├── repository/
│       │   ├── UserRepository.java
│       │   ├── RecipeRepository.java
│       │   └── IngredientRepository.java
│       ├── security/
│       │   ├── JwtUtils.java
│       │   └── JwtAuthFilter.java
│       └── service/
│           ├── AuthService.java
│           ├── RecipeService.java
│           └── UserDetailsServiceImpl.java
│
└── frontend/
    ├── package.json
    └── src/
        ├── App.js
        ├── index.css
        ├── context/
        │   └── AuthContext.js
        ├── services/
        │   └── api.js
        ├── components/
        │   ├── Navbar.js
        │   └── RecipeCard.js
        └── pages/
            ├── HomePage.js
            ├── RecipeDetailPage.js
            ├── LoginPage.js
            ├── RegisterPage.js
            └── BookmarksPage.js
```

---

## 🔌 API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, returns JWT |

### Recipes
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/recipes` | Get all recipes |
| GET | `/api/recipes/{id}` | Get recipe by ID |
| GET | `/api/recipes/search?query=` | Search by name/description |
| GET | `/api/recipes/search/ingredients?ingredients=` | Search by ingredients (comma-separated) |
| POST | `/api/recipes/{id}/bookmark` | Toggle bookmark (auth required) |
| GET | `/api/recipes/bookmarks` | Get user's bookmarked recipes (auth required) |

### Ingredients
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/ingredients` | Get all ingredients |
| GET | `/api/ingredients/search?query=` | Search ingredients |

---

## 🍽 Sample Recipes Included

| Recipe | Cuisine | Calories |
|--------|---------|----------|
| Spaghetti Bolognese | Italian | 520 |
| Butter Chicken | Indian | 480 |
| Avocado Toast with Eggs | American | 320 |
| Chicken & Vegetable Stir Fry | Chinese | 310 |
| Red Lentil Soup | Middle Eastern | 280 |
| Paneer Tikka Masala | Indian | 420 |
| Classic Greek Salad | Greek | 220 |
| Power Oatmeal Bowl | American | 350 |
| Pan-Seared Salmon with Quinoa | American | 490 |
| Coconut Tofu Curry | Thai | 360 |

---

## 🔧 Troubleshooting

**"Access denied" on MySQL:**
```sql
GRANT ALL PRIVILEGES ON recipedb.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

**Port 8080 in use:**
Change `server.port=8081` in `application.properties` and update `"proxy"` in `frontend/package.json`.

**CORS issues:**
Ensure `app.cors.allowed-origins=http://localhost:3000` matches your frontend URL.

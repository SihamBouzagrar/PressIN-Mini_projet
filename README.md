<div align="center">

# 🧺 Pressin

**Digital laundry pickup & delivery management platform**

*Replacing WhatsApp chaos with real-time order tracking, live driver dispatch, and a centralized admin dashboard.*

[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-brightgreen)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)]()
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)]()

</div>

---

## 📌 The Problem

Students today coordinate laundry pickups through an unstructured mix of **WhatsApp messages and phone calls**. This leads to:

- ❌ **Missed collections** — no reliable scheduling system
- ❌ **Zero visibility** — customers have no idea where their order stands
- ❌ **Endless follow-ups** — repeated "where is my laundry?" messages to drivers and staff
- ❌ **No accountability** — no structured record of orders, drivers, or statuses

**Pressin** solves this by digitizing the entire pickup-to-delivery workflow into a single platform with dedicated experiences for customers, drivers, and administrators.

---

## ✨ Key Features by Role

### 👤 Customer
1. Log in to the application
2. Select the type of clothes, choose the desired treatment/service, and provide the pickup location
3. Confirm and submit the order
4. Track the order in real time
5. Benefit from an integrated **AI assistant (chatbot)** for instant help

### 🚗 Driver
1. View daily assignments
2. Accept pickup requests
3. Update order status live
4. Track the order by **GPS location**
5. Access personal statistics via a dedicated dashboard

### 🛠️ Admin
1. Monitor all orders across the platform
2. Assign drivers to orders
3. Access a **real-time dashboard** of platform activity
4. Track orders in real time alongside clients
5. Create and manage driver profiles
6. Check and update order statuses

---

## 🧱 Tech Stack

| Layer          | Technology                                      |
|-----------------|--------------------------------------------------|
| Backend         | Spring Boot 2.7 (Java 17), Spring Data JPA / Hibernate |
| Database        | PostgreSQL 18                                    |
| Frontend        | HTML / CSS / JavaScript, served via Nginx        |
| AI Assistant    | Groq API                                         |
| Containerization| Docker & Docker Compose                          |
| Security        | Spring Security, BCrypt password hashing         |

---

## 🏗️ Architecture

```
┌───────────────────┐      ┌────────────────────┐      ┌────────────────────┐
│  frontend-nginx    │ ───► │   pressin-app       │ ───► │   postgres-db       │
│  Customer / Driver  │      │  Spring Boot API    │      │  PostgreSQL 18      │
│  / Admin UI          │      │  (port 8083)         │      │  (port 5432)         │
│  (port 86)            │      │                       │      │                       │
└───────────────────┘      └────────────────────┘      └────────────────────┘
```

| Service    | Container name     | Host port | Responsibility               |
|------------|----------------------|-----------|--------------------------------|
| `db`       | `postgres-db`       | 5432      | PostgreSQL database             |
| `app`      | `pressin-app`       | 8083      | Spring Boot REST API            |
| `frontend` | `frontend-nginx`    | 86        | Web interface (Nginx)           |

---

## 🚀 Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) & Docker Compose
- A `.env` file at the project root containing:

```env
GROQ_API_KEY=your_groq_api_key
```

### Installation

```bash
# 1. Clone the repository
git clone <repository-url>
cd TP_Java

# 2. Configure environment variables
# Create a .env file at the root with your GROQ_API_KEY

# 3. Build and start all services
docker-compose up -d --build

# 4. Access the application
# Frontend:  http://localhost:86
# API:       http://localhost:8083
```

---

## 🛠️ Useful Commands

| Action                                   | Command                                                       |
|--------------------------------------------|-----------------------------------------------------------------|
| Start all containers                       | `docker-compose up -d`                                          |
| Rebuild after code changes (backend)       | `docker-compose up -d --build app`                              |
| Rebuild after code changes (frontend)      | `docker-compose up -d --build frontend`                         |
| Full rebuild without cache                 | `docker-compose build --no-cache`                               |
| Follow backend logs                        | `docker logs -f pressin-app`                                    |
| Follow database logs                       | `docker logs -f postgres-db`                                    |
| Stop all containers                        | `docker-compose down`                                           |
| Stop and remove volumes (⚠️ deletes data)   | `docker-compose down -v`                                        |
| Open a psql shell in the DB container      | `docker exec -it postgres-db psql -U postgres -d pressin_db`    |

---

## 📁 Project Structure

```
TP_Java/
├── src/                              # Spring Boot source code
│   └── main/java/com/example/demo/
│       ├── auth/                      # Authentication (login, register)
│       ├── dto/                        # Data Transfer Objects
│       ├── rest/                       # REST controllers
│       └── utilisateur/
│           ├── entity/                 # JPA entities (Users, Livraison, Commande...)
│           └── service/                # Business logic
├── frontend/                          # Static web files (HTML/CSS/JS)
│   └── Dockerfile
├── Dockerfile                         # Backend build definition
├── docker-compose.yml
├── pom.xml
└── .env                                # Environment variables (not versioned)
```

---

## 🗃️ Data Model (Overview)

| Entity          | Description                                                        |
|-------------------|----------------------------------------------------------------------|
| **Users**          | Account holder — customer, driver, or admin (`ROLE_CLIENT`, `ROLE_LIVREUR`, `ROLE_ADMIN`) |
| **Commandes**      | An order: number, status, amounts, linked client, driver, and delivery |
| **Livraisons**     | A delivery: status, pickup & destination addresses, planned/actual dates, assigned driver |
| **Services**        | Catalog of available laundry treatments and pricing                  |
| **ServiceOptions**  | Add-ons / supplements attached to a service                          |
| **Article**          | Individual clothing items tied to an order                            |

---

## 🔌 Core API Endpoints

### Authentication
| Method | Endpoint          | Description         |
|--------|--------------------|------------------------|
| POST   | `/auth/login`      | Log in                |
| POST   | `/auth/register`   | Register (customer / driver) |
| GET    | `/auth/logout`     | Log out                |

### Users
| Method | Endpoint                              | Description                  |
|--------|-----------------------------------------|----------------------------------|
| GET    | `/rest/user/{id}`                      | Get user details                 |
| GET    | `/rest/user/clients`                   | List all customers               |
| GET    | `/rest/user/admins`                    | List all admins                  |
| GET    | `/rest/user/livreurs`                  | List all drivers                 |
| POST   | `/rest/user/admin/create-livreur`      | Create a driver account (admin)  |
| PUT    | `/rest/user/update/{id}`               | Update a user profile            |
| DELETE | `/rest/user/{id}`                      | Delete a user                    |

### Deliveries (Livraisons)
| Method | Endpoint                                             | Description                        |
|--------|---------------------------------------------------------|----------------------------------------|
| GET    | `/rest/livraisons/all`                                  | List all deliveries (admin)            |
| GET    | `/rest/livraisons/livreur/{livreurId}`                   | Deliveries assigned to a driver        |
| GET    | `/rest/livraisons/commande/{commandeId}`                 | Delivery linked to an order            |
| GET    | `/rest/livraisons/statut/{statut}`                        | Filter deliveries by status            |
| POST   | `/rest/livraisons/create/{commandeId}/{livreurId}`        | Create and assign a delivery           |
| PUT    | `/rest/livraisons/statut/{id}`                             | Update delivery status                 |
| PUT    | `/rest/livraisons/dates/{id}`                              | Update pickup/delivery dates           |

---

## 🩺 Troubleshooting

| Issue                                            | Likely cause / fix                                                             |
|-----------------------------------------------------|------------------------------------------------------------------------------------|
| Backend fails to build                             | Ensure base images (`eclipse-temurin`, `postgres`) are reachable and up to date    |
| `401 Unauthorized` on login                        | Verify the account exists and the BCrypt hash matches the submitted password       |
| `500` error on entity retrieval                    | Ensure every `@Entity` class has a no-args constructor (`@NoArgsConstructor`)      |
| Postgres 18 refuses to start with old data          | Postgres 18 expects a mount at `/var/lib/postgresql`, not `/var/lib/postgresql/data` — see [docker-library/postgres#37](https://github.com/docker-library/postgres/issues/37) |

---

## 📄 License

Academic project — for educational purposes.

# 🧺 PressIN - Application de Gestion de Pressing

## 📋 Description du Projet


PressIN est une application web complète de gestion de pressing permettant aux clients de passer des commandes en ligne et aux administrateurs de gérer les services de nettoyage, repassage et entretien de vêtements.
=======
/PressIN-app
│── index.html         # Page d'accueil
│── login.html         # Page de connexion
│── register.html      # Page d'inscription
│── profile.html       # Page pour afficher/modifier le profil utilisateur
│── commande.html      # Page de gestion des commandes
│── dashboard.html     # Tableau de bord (utilisateur connecté / admin)

│── service.html       # Gestion des services


## 🏗️ Architecture du Projet


Le projet suit une architecture Client-Serveur avec :
- **Front-End** : HTML, CSS, JavaScript
- **Back-End** : Java (Spring Boot)
- **Base de Données** : PostgreSQL
=======
src/main/java/com/example/demo/rest
├── AuthController.java      # Gestion de l'authentification (login, logout, session)

├── CommandeController.java  # Gestion des commandes
├── PersonController.java    # Gestion des informations utilisateur (profil)


---

## 📁 Structure du Projet

```
PressIN-app/
│
├── 📂 Front-End/
│   ├── index.html              # Page d'accueil
│   ├── login.html              # Page de connexion
│   ├── register.html           # Page d'inscription
│   ├── profile.html            # Page profil utilisateur
│   ├── commande.html           # Page de gestion des commandes
│   ├── dashboard.html          # Tableau de bord 
│  
│   └── service.html            # Gestion des services
│
├── 📂 Back-End/
│   └── src/main/java/com/example/demo/rest/
│       ├── AuthController.java        # Authentification (login, logout, session)
│       ├── ArticlesController.java    # Gestion des articles/vêtements
│       ├── CommandeController.java    # Gestion des commandes
│       └── PersonController.java      # Gestion des profils utilisateurs
│
├── 📂 assets/
│   ├── css/
│   ├── js/
│   └── images/
│
└── README.md                   # Ce fichier
```

---


## 👥 Contributeurs

- **Siham Bouzagrar **



## 🙏 Remerciements

Merci d'utiliser PressIN ! N'hésitez pas à contribuer au projet ou à signaler des bugs.

---

**Dernière mise à jour** :  29 Janvier 2026

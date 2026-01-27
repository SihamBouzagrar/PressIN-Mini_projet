📁 Structure du projet
Front-End

Le front-end est situé à la racine dans /PressIN-app et contient les pages HTML suivantes :

/PressIN-app
│── index.html         # Page d'accueil
│── login.html         # Page de connexion
│── register.html      # Page d'inscription
│── profile.html       # Page pour afficher/modifier le profil utilisateur
│── commande.html      # Page de gestion des commandes
│── dashboard.html     # Tableau de bord (utilisateur connecté / admin)
│── article.html       # Gestion des articles / vêtements
│── service.html       # Gestion des services
Back-End

Le back-end est développé en Java (Spring Boot) et se trouve dans :

src/main/java/com/example/demo/rest
├── AuthController.java      # Gestion de l'authentification (login, logout, session)
├── ArticlesController.java  # Gestion des articles / vêtements
├── CommandeController.java  # Gestion des commandes
├── PersonController.java    # Gestion des informations utilisateur (profil)

AuthController = login/logout, session

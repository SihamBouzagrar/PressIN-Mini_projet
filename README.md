Structure actuel du Front End de mon projet :
/PressIN-app

│── index.html        (Accueil)
│── login.html      ( page pour se connecter)
│── register.html   (page pour s’inscrire)
│── profile.html   (page pour afficher/modifier le profil utilisateur)
│── commande.html     (commande)
│── dashboard.html    (tableau de bord (souvent pour l’admin ou l’utilisateur connecté).)
│── article.html      (Articles / vêtements)
│── service.html      (Services)

Cote Back End :
src/main/java/com/example/demo/rest
    ├── AuthController.java
    ├── ArticlesController.java
    ├── CommandeController.java
    ├── PersonController.java
    
ArticlesController = gérer uniquement les articles

CommandeController = gérer uniquement les commandes

AuthController = login/logout, session

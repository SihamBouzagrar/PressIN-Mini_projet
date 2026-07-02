FROM nginx:alpine

# Copier les fichiers statiques
COPY . /usr/share/nginx/html

# Exposer le port 80
EXPOSE 86

# Démarrer Nginx
CMD ["nginx", "-g", "daemon off;"]
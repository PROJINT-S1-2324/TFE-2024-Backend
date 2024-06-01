# Utiliser l'image officielle de Node.js comme image de base
FROM node:18

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers de l'application dans le répertoire de travail
COPY . /app

# Installer les dépendances de l'application
RUN npm install

# Exposer le port sur lequel l'application va écouter
EXPOSE 8080

# Définir le point d'entrée pour le conteneur
CMD ["npm", "start"]

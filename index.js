const express = require('express');
const ParseServer = require('parse-server').ParseServer;
const path = require('path');

const api = new ParseServer({
    databaseURI: 'mongodb://localhost:27017/dev', // URI de votre base de données
    cloud: process.env.CLOUD_CODE_MAIN || __dirname + '/cloud/main.js', // chemin vers le fichier de cloud code
    appId: 'myAppId',
    masterKey: 'myMasterKey', // Gardez cette clé secrète
    serverURL: process.env.SERVER_URL || 'http://localhost:8080/parse',  // Ne pas oublier de changer en https si nécessaire
});

const app = express();

// Serve the Parse API on the /parse URL prefix
app.use('/parse', api);

// Changer le port pour écouter sur 8080 ou le port défini par l'environnement
const port = process.env.PORT || 8080;
app.listen(port, function() {
    console.log('parse-server-example running on port ' + port + '.');
});

const express = require('express');
const ParseServer = require('parse-server').ParseServer;
const path = require('path');

const api = new ParseServer({
    databaseURI: 'mongodb://localhost:27017/dev', // URI de votre base de données
    cloud: process.env.CLOUD_CODE_MAIN || __dirname + '/cloud/main.js', // chemin vers le fichier de cloud code
    appId: 'myAppId',
    masterKey: 'myMasterKey', // Gardez cette clé secrète
    serverURL: 'http://localhost:1337/parse',  // Ne pas oublier de changer en https si nécessaire
});

const app = express();

// Serve the Parse API on the /parse URL prefix
app.use('/parse', api);

app.listen(1337, function() {
    console.log('parse-server-example running on port 1337.');
});

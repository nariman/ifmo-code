/**
 * Markdown server
 * MongoDB connection
 */

const MongoClient = require("mongodb").MongoClient;

const MONGO_URL = "mongodb://localhost:27017";
const MONGO_DB = "markdown";

module.exports = function (app) {
    MongoClient.connect(MONGO_URL)
        .then((connection) => {
            app.dbconn = connection
            app.db = connection.db(MONGO_DB);

            app.files = app.db.collection("files");

            console.log("Database connection established")
        })
        .catch((err) => console.error(err))
};

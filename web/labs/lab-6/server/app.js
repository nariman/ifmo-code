/**
 * Markdown server
 * Koa app
 */

const Koa = require("koa");
const Router = require("koa-router");
const BodyParser = require("koa-bodyparser");
const Logger = require('koa-logger');
const Cors = require('@koa/cors');

const ObjectID = require("mongodb").ObjectID;


//
// Application
//

const app = new Koa();
const router = new Router();


//
// Routes
//

router.get("/", async ctx => {
    ctx.body = await ctx.app.files.find().toArray();
});

router.get("/:id", async ctx => {
    ctx.body = await ctx.app.files.findOne({"_id": ObjectID(ctx.params.id)});
});

router.post("/", async ctx => {
    ctx.request.body.timestamp = new Date();
    ctx.body = await ctx.app.files.insert(ctx.request.body);
});

router.put("/:id", async ctx => {
    ctx.body = await ctx.app.files.replaceOne({"_id": ObjectID(ctx.params.id)}, ctx.request.body);
});

router.delete("/:id", async ctx => {
    ctx.body = await ctx.app.files.deleteOne({"_id": ObjectID(ctx.params.id)});
});


//
// Middleware chain
//

// Simple res/req logger
app.use(Logger());

// CORS
app.use(Cors());

// Body Parser middleware for JSON parsing
app.use(BodyParser());

// Add routes to the chain
app.use(router.routes()).use(router.allowedMethods());


//
// DB
//

require("./mongo")(app);

//
// Server
//

app.listen(3000, () => {
    console.log('Server running on port 3000');
});

const path = require('path');
const express = require('express');
const session = require('express-session');
const csrf = require('csurf');

const sessionConfig = require('./config/session');
const db = require('./data/database');
const blogRoutes = require('./routes/blog');
const authRoutes = require('./routes/auth');
const authMiddleware = require('./middlewares/auth-middlewares');
const addCSRFTokenMiddleware = require('./middlewares/csrf-token-middlewares');

const app = express();

const mongoDbSessionStore = sessionConfig.createSessionStore(session);

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.use(express.static('public'));
app.use(express.urlencoded({ extended: false }));

app.use(session(sessionConfig.createSessionConfig(mongoDbSessionStore)));  // 올바르게 설정된 세션 미들웨어

app.use(csrf());
app.use(addCSRFTokenMiddleware);
app.use(authMiddleware);

app.use(authRoutes);
app.use(blogRoutes);


app.use(function(error, req, res, next) {
  res.render('500');
});

db.connectToDatabase().then(function () {
  app.listen(3000);
});

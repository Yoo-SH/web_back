const path = require('path');

const express = require('express');

const db = require('./data/database');
const demoRoutes = require('./routes/demo');
const session = require('express-session');
const mongodbStore = require('connect-mongodb-session');


const app = express();
const MongoDBStore = mongodbStore(session);
const sessionStore = new MongoDBStore({
  uri: 'mongodb://localhost:27017/auth-demo',
  databaseName: 'auth-demo',  
  collection: 'sessions'
});

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.use(express.static('public'));
app.use(express.urlencoded({ extended: false }));

app.use(session({
  secret: 'super-secret',
  resave: false,
  saveUninitialized: false,
  store: sessionStore
}));


app.use(async function(req, res, next) { //모든 라우트에 사용자 정보를 전달하기 위해 미들웨어를 사용한다
  const user = req.session.user; //세션에 저장된 유저정보를 가져온다
  const isAuth = req.session.isAuthenticated;  //세션에 저장된 어드민 정보를 가져온다
  

  if(!user || !isAuth) { //유저정보나 어드민 정보가 없다면
    return next(); //다음 미들웨어로 넘어간다
  }

  const userDoc = await db.getDb().collection('users').findOne({_id: user.id}); //유저정보를 데이터베이스에서 찾는다
  const isAdmin = userDoc.isAdmin; //유저정보에서 어드민 정보를 가져온다

  res.locals.isAuth = isAuth; //res.locals에 어드민 정보를 저장한다
  res.locals.isAdmin = isAdmin; //res.locals에 어드민 정보를 저장한다

  next();
});

app.use(demoRoutes);

app.use(function(error, req, res, next) {
  res.render('500');
})
  
db.connectToDatabase().then(function () {
  app.listen(3000);

});

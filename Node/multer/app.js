const path = require('path');

const express = require('express');

const userRoutes = require('./routes/users');
const db = require('./data/database');

const app = express();

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.use(express.urlencoded({ extended: false }));
app.use(express.static('public'));
app.use('/images', express.static('images')); //사용자가 서버 경로에 직접 접근 불가능하니, images폴더를 static으로 설정하여 제공(사용자는 보기만 가능), /images 경로가 있는 서버에 요청이 도달하는 경우에만 미들웨어가 활성화됨.


app.use(userRoutes);

db.connectToDatabase().then(function () {
  app.listen(3000);
});

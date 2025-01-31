const path = require('path');

const express = require('express');


const defaultRoutes = require('./routes/default');
const restaurantRouters = require('./routes/restaurants');
const app = express();


app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'ejs');

app.use('/', defaultRoutes); // default 라우터에 존재하지 않으면 app.js에서 해당 경로 탐색.
app.use('/', restaurantRouters)

app.use(express.static('public')); //모든 수신 요청에 대해 이 공용 폴더에서 찾을 수 있는 파일에 대한 요청인지 확인해야 한다고 express에게 알림.
app.use(express.urlencoded({ extended: false }));


app.use(function (req, res) { //모든 정의된 라우트와 미들웨어가 요청을 처리한 후에도 응답을 보내지 않은 경우 이 미들웨어가 실행됩니다. 즉, 요청된 URL이 정의된 라우트와 일치하지 않을 때 호출
    res.status(404).render('404');
})

app.use(function (error, req, res, next) { //error 변수는 express에서 감시  
    res.status(500).render('500');
})
app.listen(3000); 

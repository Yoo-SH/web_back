const fs = require('fs');
const path = require('path');

const express = require('express');

const restaurantData =require('./util/restaurant-data'); //해당 경로에 exports된 매소드를 사용할 수 있는 객체
const app = express();



app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'ejs');


app.use(express.static('public')); //모든 수신 요청에 대해 이 공용 폴더에서 찾을 수 있는 파일에 대한 요청인지 확인해야 한다고 express에게 알림.
app.use(express.urlencoded({ extended: false }));


app.get('/', function (req, res) {
    res.render('index');
});

app.get('/restaurants', function (req, res) {
    const storedRestaurants = restaurantData.getStoredRestaurants();
        

    res.render('restaurants', { numberOfInputN: storedRestaurants.length, restaurants: storedRestaurants });
});


app.get('/restaurants/:id', function (req, res) { //동적으로 url을 할당받아 
    const restaurantId = req.params.id;
    const storedRestaurants = restaurantData.getStoredRestaurants();

    for (const restaurant of storedRestaurants) {
        if (restaurantId === restaurant.id) {
            return res.render('restaurant-detail', { restaurant: restaurant });
        }
    }

    res.status('404').render('404');
});


app.get('/recommend', function (req, res) {
    res.render('recommend');
});


app.post('/recommend', function (req, res) {
    const restaurant = req.body;
    restaurant.id = uuid.v4(); //js에서는 객체에 해당 고유속상값이 존재하지 않으면, 고유 속성값을 생성하고 추가함. id는 객체 고유 속상값에 존재 x
    const restaurants = restaurantData.getStoredRestaurants();

    restaurants.push(restaurant);

    restaurantData.storedRestaurants(restaurants);

    res.redirect('/confirm');

});

app.get('/confirm', function (req, res) {
    res.render('confirm');
});

app.get('/about', function (req, res) {
    res.render('about');
});

app.use(function (req, res) { //모든 정의된 라우트와 미들웨어가 요청을 처리한 후에도 응답을 보내지 않은 경우 이 미들웨어가 실행됩니다. 즉, 요청된 URL이 정의된 라우트와 일치하지 않을 때 호출
    res.status(404).render('404');
})

app.use(function (error, req, res, next) { //error 변수는 express에서 감시  
    res.status(500).render('500');
})
app.listen(3000); 

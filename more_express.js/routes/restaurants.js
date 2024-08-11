const express = require('express');
const uuid = require('uuid');

const restaurantData =require('../util/restaurant-data'); //해당 경로에 exports된 매소드를 사용할 수 있는 객체
const router = express.Router();

router.get('/restaurants', function (req, res) {
    const storedRestaurants = restaurantData.getStoredRestaurants();


    res.render('restaurants', { numberOfInputN: storedRestaurants.length, restaurants: storedRestaurants });
});


router.get('/restaurants/:id', function (req, res) { //동적으로 url을 할당받아 
    const restaurantId = req.params.id;
    const storedRestaurants = restaurantData.getStoredRestaurants();

    for (const restaurant of storedRestaurants) {
        if (restaurantId === restaurant.id) {
            return res.render('restaurant-detail', { restaurant: restaurant });
        }
    }

    res.status('404').render('404');
});


router.get('/recommend', function (req, res) {
    res.render('recommend');
});


router.post('/recommend', function (req, res) {
    const restaurant = req.body;
    restaurant.id = uuid.v4(); //js에서는 객체에 해당 고유속상값이 존재하지 않으면, 고유 속성값을 생성하고 추가함. id는 객체 고유 속상값에 존재 x
    const restaurants = restaurantData.getStoredRestaurants();

    restaurants.push(restaurant);

    restaurantData.storedRestaurants(restaurants);

    res.redirect('/confirm');

});

router.get('/confirm', function (req, res) {
    res.render('confirm');
});

module.exports = router;
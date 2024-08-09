const fs = require('fs');
const path = require('path');

const express = require('express');
const app = express();

app.set('views', path.join(__dirname,'views'))
app.set('view engine', 'ejs');


app.use(express.static('public')); //모든 수신 요청에 대해 이 공용 폴더에서 찾을 수 있는 파일에 대한 요청인지 확인해야 한다고 express에게 알림.
app.use(express.urlencoded({extended:false}));

app.get('/', function(req,res){
    res.render('index');
});

app.get('/restaurants', function(req,res){
    res.render('restaurants');
});



app.get('/recommend', function(req,res){
    res.render('recommend');
});


app.post('/recommend', function(req, res) {
    const restaurant = req.body;
    const filePath = path.join(__dirname, 'data', 'restaurants.json');

    try {
        const fileData = fs.readFileSync(filePath);
        const storedRestaurants = JSON.parse(fileData);

        storedRestaurants.push(restaurant);

        fs.writeFileSync(filePath, JSON.stringify(storedRestaurants));
        res.redirect('/confirm');
    } catch (error) {
        console.error('Error processing file:', error);
        res.status(500).send('Internal Server Error');
    }

});

app.get('/confirm', function(req,res){
    res.render('confirm');
});

app.get('/about', function(req,res){
    res.render('about');
});


app.listen(3000);
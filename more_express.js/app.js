const path = require('path');

const express =require('express');


const app = express();
app.use(express.static('public')); //모든 수신 요청에 대해 이 공용 폴더에서 찾을 수 있는 파일에 대한 요청인지 확인해야 한다고 express에게 알림.


app.get('/', function(req,res){
    const htmlFilePath = path.join(__dirname,'views', 'index.html');
    res.sendFile(htmlFilePath);
});

app.get('/restaurants', function(req,res){
    const htmlFilePath = path.join(__dirname,'views', 'restaurants.html');
    res.sendFile(htmlFilePath);
});

app.get('/recommend', function(req,res){
    const htmlFilePath = path.join(__dirname,'views', 'recommend.html');
    res.sendFile(htmlFilePath);
});

app.get('/confirm', function(req,res){
    const htmlFilePath = path.join(__dirname,'views', 'confirm.html');
    res.sendFile(htmlFilePath);
});

app.get('/about', function(req,res){
    const htmlFilePath = path.join(__dirname,'views', 'about.html');
    res.sendFile(htmlFilePath);
});


app.listen(3000);
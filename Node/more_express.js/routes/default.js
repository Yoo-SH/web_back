const express = require('express');

const router = express.Router();


router.get('/', function (req, res) {
    res.render('index');
});

router.get('/about', function (req, res) {
    res.render('about');
});

module.exports = router; //라우터 객체를 exports하여 다른 파일에서 사용 가능

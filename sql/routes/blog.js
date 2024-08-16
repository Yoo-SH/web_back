const express = require('express');


const db = require('../data/database'); // Import the database pool
const router = express.Router();   // Create a new router


router.get('/', function(req, res) {
    res.redirect('/posts');
}); 

router.get('/posts', function(req, res) {
    res.render('posts-list');
});

router.get('/new-post', async function(req, res) {
    const [authors] = await db.query('SELECT * FROM authors'); // db가져오는 경우, 다른 머신일 떄 시간이 걸릴 수 있음. 따라서 promise로 비동기적 처리
    res.render('create-post', { authors : authors });
});

module.exports = router; // Export the router
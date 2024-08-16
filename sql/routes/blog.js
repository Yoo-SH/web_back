const express = require('express');

const router = express.Router();   // Create a new router

router.get('/', function(req, res) {
    res.redirect('/posts');
}); 

router.get('/posts', function(req, res) {
    res.render('posts-list');
});

router.get('/new-post', function(req, res) {
    res.render('create-post');
});

module.exports = router; // Export the router
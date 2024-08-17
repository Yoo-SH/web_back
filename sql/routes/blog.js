const express = require('express');


const db = require('../data/database'); // Import the database pool
const router = express.Router();   // Create a new router


router.get('/', function(req, res) {
    res.redirect('/posts');
}); 

router.get('/posts', async function(req, res) {
    const query = `
        SELECT posts.*, authors.name AS author_name FROM posts
        JOIN authors ON posts.author_id = authors.id
    `;
    const [posts] = await db.query(query);
    res.render('posts-list', { posts: posts });
});

router.get('/new-post', async function(req, res) {
    const [authors] = await db.query('SELECT * FROM authors'); // db가져오는 경우, 다른 머신일 떄 시간이 걸릴 수 있음. 따라서 promise로 비동기적 처리
    res.render('create-post', { authors : authors });
});

router.post('/posts', async function(req, res) { //해당 경로를 가져올 떄, 실행되는 함수를 비도기적으로 실행하는 것은 당연
    const data =[
        req.body.title,
        req.body.summary,
        req.body.content,
        req.body.author
    ];
    
    await db.query('INSERT INTO posts (title, summary, body, author_id) VALUES (?)',[data]); //awati는 비동기 작업이 완료될 때 까지 실행을 일시 중지하고 결과를 기다린다음 해당 결과를 반환, 오류나면 다음 코드 실행x, =>try catch로 처리
    res.redirect('/posts');
    

    
});

module.exports = router; // Export the router
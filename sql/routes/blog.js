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


//라우트가 로드된 자리에서 해당 ID자리 표시자의 구체적인 값을 추출하는 방법
router.get('/posts/:id', async function(req, res) {
    const query = `
        SELECT posts.*, authors.name AS author_name, authors.email AS author_email FROM posts
        INNER JOIN authors ON posts.author_id = authors.id
        WHERE posts.id = ?
    `;
    const [posts] = await db.query(query, [req.params.id]); //db.query는 일반적으로 쿼리 결과와 메타데이터를 배열로 반환. posts[0]은 쿼리 결과에서 첫 번쨰 행(row)을 의미. posts[0]은 데이터베이스에서 조회된 첫 번째(그리고 유일한) 행을 의미합니다.
    
    if(!posts || posts.length ===0) {  //게시물이 존재하지 않는 경우
        res.status(404).render('404');
        return;
    }


    const postData = {
        ...posts[0],
        date : posts[0].date.toISOString(),
        humanReadableDate : posts[0].date.toLocaleString('en-US', {}),
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'

    }

    res.render('post-detail', { post: postData });
});

    router.get('/posts/:id/edit', async function(req, res) {
        const query = `
            SELECT * FROM posts
            WHERE id = ?
        `;

        const [posts] = await db.query(query, [req.params.id]);
        if(!posts || posts.length === 0) {
            res.status(404).render('404');
            return;
        }

        res.render('update-post', { post: posts[0] });
    });

    router.post('/posts/:id/edit', async function(req, res) {
        const query = `
            UPDATE posts
            SET title = ?, summary = ?, body = ?
            WHERE id = ?
        `; //db의 내용을 업데이트하여 게시물을 수정하는 쿼리

        await db.query(query, [
            req.body.title,
            req.body.summary,
            req.body.content,
            req.params.id
        ]);

        res.redirect('/posts');
    });


module.exports = router; // Export the router
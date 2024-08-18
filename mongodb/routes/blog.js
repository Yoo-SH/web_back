const express = require('express');
const mongodb = require('mongodb');

const db = require('../data/database');

const ObjectId = mongodb.ObjectId; //objectID 생성자 함수를 가져옴. 인스턴스화할 수 있는 클래스 함수를 이용해서 MongoDB의 _id를 생성할 수 있음.

const router = express.Router();

router.get('/', function (req, res) {
  res.redirect('/posts');
});

router.get('/posts', async function (req, res) {
  const posts = await db
  .getDb()
  .collection('posts')
  .find({} , { title: 1, summary: 1, 'author.name': 1 })   //서버의 과부하를 줄이기 위해 필요한 데이터만을 추출하도록 프로젝션을 이용함 , 1은 포함 0은 미포함 지정
  .toArray(); 
 
  console.log(posts);
  res.render('posts-list', { posts: posts });
});

router.get('/new-post', async function (req, res) {
  const authors = await db.getDb().collection('authors').find().toArray();
  res.render('create-post', { authors: authors });
});


router.post('/posts', async function (req, res) {

  const authorID = new ObjectId(req.body.author);
  console.log(authorID);

  const author = await db.getDb().collection('authors').findOne({ _id: authorID });

  const newPost = { // form에서 받아온 데이터를 객체로 만들어서 저장
    title: req.body.title,
    content: req.body.content,
    summary: req.body.summary,
    body: req.body.content,
    date: new Date(),
    author: {
      id: authorID,
      name: author,
      email: author.email

    }
  }

  const result = await db.getDb().collection('posts').insertOne(newPost); //이 갹체는 몽고 DB 패키지에 의해 데이터베이스에 문서로 삽입됨
  console.log(result);
  res.redirect('/posts');
});


 router.get('/posts/:postId', async function (req, res) {
  const postId = req.params.postId;
  const post = await db
  .getDb()
  .collection('posts')
  .findOne({ _id: new ObjectId(postId) }, {summary: 0});

  if(!post) {
    return res.status(404).render('404');
  }

  
  post.humanReadableDate = post.date.toLocaleString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });

  post.date = post.date.toISOString();


  res.render('post-detail', { post: post });

 });

 router.get('/posts/:id/edit', async function (req, res) {
  const postId = req.params.id;
  const post = await db
  .getDb()
  .collection('posts')
  .findOne({_id: new ObjectId(postId)}, {title :1 ,summary: 1, body : 1});

  if(!post) {
    return res.status(404).render('404');
  }

  res.render('update-post', { post: post });

});

router.post('/posts/:id/edit', async function (req, res) {
  const postId = req.params.id;
  const result = await db
  .getDb()
  .collection('posts')
  .updateOne(
    {_id: new ObjectId(postId)}, 
    {$set: 
      {
        title: req.body.title, 
        summary: req.body.summary, 
        body: req.body.content
      }
    }
  );

  

  res.redirect('/posts');

});


module.exports = router;
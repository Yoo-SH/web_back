const express = require('express');

const db = require('../data/database');
const bcrypt = require('bcryptjs');

const router = express.Router();

router.get('/', function (req, res) {
  res.render('welcome');
});

router.get('/signup', function (req, res) {
  res.render('signup');
});

router.get('/login', function (req, res) {
  res.render('login');
});

router.post('/signup', async function (req, res) {
  const userData = req.body;
  const enteredEmail = userData.email;
  const enteredConfirmEmail = userData['confirm-email']; //'-'문자는 .으로 접근시 사용불가는하기에 ['']한 방법으로 대신 접근 가능하다
  const enteredPassword = userData.password;

  if ( //가입시에 유저가 입력한 정보의 유효성을 검사
    !enteredEmail || 
    !enteredConfirmEmail ||
    !enteredPassword ||
    enteredEmail !== enteredConfirmEmail ||
    !enteredEmail.includes('@')
  ) {
    console.log('Invalid input');
    return res.redirect('/signup');
  }


  const existingUser = await db

  .getDb()
  .collection('users')
  .findOne({ email : enteredEmail });

  if (existingUser) { //이메일의 중복을 검사하여 가입을 방지
    console.log('User exists already');
    return res.redirect('/signup');
  }

  const hashedPassword = await bcrypt.hash(enteredPassword, 12);

  const user = {
    email: enteredEmail,
    password: hashedPassword

  }

  await db.getDb().collection('users').insertOne(user);

  res.redirect('/login');


});

router.post('/login', async function (req, res) {
  const userData = req.body;
  const enteredEmail = userData.email;
  const enteredPassword = userData.password;

  const existingUser = await db
    .getDb()
    .collection('users')
    .findOne({ email: enteredEmail });

  if (!existingUser) { //처음에는 이메일의 유효성을 검사
    console.log('Invalid email');
    return res.redirect('/login');
  }

  const passwordsAreEqual = await bcrypt.compare(
    enteredPassword,
    existingUser.password
  ); //해쉬된 비밀번호와 입력된 비밀번호를 비교

  if (!passwordsAreEqual) { //이후 이메일의 우효성을 검사
    console.log('Invalid password');
    return res.redirect('/login');
  }

  console.log('User logged in');
  res.redirect('/admin');
});

router.get('/admin', function (req, res) {
  res.render('admin');
});

router.post('/logout', function (req, res) { });

module.exports = router;

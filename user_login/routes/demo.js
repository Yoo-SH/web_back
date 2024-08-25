const express = require('express');

const db = require('../data/database');
const bcrypt = require('bcryptjs');

const router = express.Router();

router.get('/', function (req, res) {
  res.render('welcome');
});

router.get('/signup', function (req, res) {
  let sessionInputData = req.session.inputData;
  
  if(!sessionInputData) {
    sessionInputData = {
      hasError: false,
      message: null,
      email: '',
      confirmEmail: '',
      password: ''
    };

  }

  req.session.inputData = null;

  res.render('signup', {inputData: sessionInputData});
});

router.get('/login', function (req, res) {
  let sessionInputData = req.session.inputData;
  
  if(!sessionInputData) {
    sessionInputData = {
      hasError: false,
      message: null,
      email: '',
      confirmEmail: '',
      password: ''
    };

  }

  req.session.inputData = null;

  res.render('login', {inputData: sessionInputData});

  
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

    req.session.inputData = {
      hasError: true,
      message: 'Invalid input - please check your input',
      email: enteredEmail,
      confirmEmail: enteredConfirmEmail,
      password: enteredPassword
    };

    return req.session.save(function () {
       res.redirect('/signup');
    });
  }


  const existingUser = await db

  .getDb()
  .collection('users')
  .findOne({ email : enteredEmail });

  if (existingUser) { //이메일의 중복을 검사하여 가입을 방지

    req.session.inputData = {
      hasError: true,
      message: 'User exists already - please login',
      email: enteredEmail,
      confirmEmail: enteredConfirmEmail,
      password: enteredPassword,
    };
    req.session.save(function () {
      res.redirect('/signup');
    });
    return;  
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

    req.session.inputData = {
      hasError: true,
      message: 'could not find user with that email',
      email: enteredEmail,
      password: enteredPassword,
    };
    req.session.save(function () {  
       res.redirect('/login');
    });
    return;
  }

  const passwordsAreEqual = await bcrypt.compare(
    enteredPassword,
    existingUser.password
  ); //해쉬된 비밀번호와 입력된 비밀번호를 비교

  if (!passwordsAreEqual) { //이후 이메일의 우효성을 검사
    req.session.inputData = {
      hasError: true,
      message: 'could not find user with that email',
      email: enteredEmail,
      password: enteredPassword,
    };
    req.session.save(function () {  
       res.redirect('/login');
    });
    return;
  }


  req.session.user = {id: existingUser._id, email: existingUser.email}; //세션에 유저 정보를 저장
  req.session.isAuthenticated = true; //세션에 로그인 여부를 저장
  req.session.save(function () { //세션을 저장
    res.redirect('/profile');
  });
  
});


router.get('/admin', async function (req, res) {
  if(!req.session.isAuthenticated) { //if (!req.session.isAuthenticated) 
    return res.status(401).render('401');
  }
  
  const user = await db
    .getDb()
    .collection('users')
    .findOne({ _id: req.session.user.id });

  if (!user || !user.isAdmin){
    return res.status(403).render('403');
  }

  res.render('admin');
});


router.get('/profile', function (req, res) {
  if(!req.session.isAuthenticated) { //if (!req.session.isAuthenticated) 
    return res.status(401).render('401');
  } 
  res.render('profile');
});

router.post('/logout', function (req, res) {
  req.session.user = null;
  req.session.isAuthenticated = false;
  res.redirect('/') //로그아웃시에 세션을 초기화하고 홈페이지로 리다이렉트(장바구니와 같은 기능을 위해 세션을 삭제하지는 않음.)
 });

module.exports = router;

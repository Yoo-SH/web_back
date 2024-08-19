const express = require('express');
const muter = require('multer');

const storageConfig = muter.diskStorage({
  destination : function(req, file, cb) {
    cb(null, 'images');
  },
  filename : function(req, file, cb) {
    cb(null, Date.now() + '-' + file.originalname); //file.originalname에는 확장자가 포함되어 있음.
  }
});


const upload = muter({ storage : storageConfig});
const router = express.Router();

router.get('/', function(req, res) {
  res.render('profiles');
});

router.get('/new-user', function(req, res) {
  res.render('new-user');
});

router.post('/profiles', upload.single('image'), function(req, res) {
 const uploadedImageFile = req.file;
 const userData = req.body;

 console.log(uploadedImageFile);
 console.log(userData);

 res.redirect('/');
});


module.exports = router;
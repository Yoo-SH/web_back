const express = require('express');

const db = require('./data/database');

const app = express();
const todosRoutes = require('./routes/todos.routes');

app.use(express.json());
app.use('/todos', todosRoutes);

app.use(function (error, req, res, next) {
    console.error(error.stack);
    res.status(500).json({
      message: 'Something went wrong!',
      error: error.message, // 에러 메시지를 응답에 포함
    });
  });


db.initDb()
  .then(function () {
    app.listen(3000);
  })
  .catch(function (error) {
    console.log('Connecting to the database failed!');
  });

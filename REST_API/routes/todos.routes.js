const express = require('express');
const todoController = require('../controllers/todos.controller');
const router = express.Router();

router.get('/', todoController.getAllTodos);
router.post('/', todoController.addTodo);


module.exports = router;    

const mysql = require('mysql2/promise');

//한번에 많은 연결을 관리하는 객체를 만들어줌, connection 보다 효율적임
const pool = mysql.createPool({
    host: 'localhost',
    database: 'blog',
    user: 'root',
    password: '',
}); 


module.exports = pool; // Export the pool
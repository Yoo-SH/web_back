const db = require('./data/database')
const quoteRoutes = require('./routes/quotes.routes')
const app = require('express')();


app.use('/quote', quoteRoutes)


app.use(function (error, req, res, next) {
    res.status(500).json({
        message: 'Something went wrong'
    });
});


db.initDb()
    .then(function() {
        app.listen(3000)
    }).catch(function (error) {
        console.log('Connection error', error);
    });


const express = require('express');
const app = express();

app.get('/quote', (req, res, next) => {
    res.json({
        quote: 'The only way to do great work is to love what you do.',
    });
});


app.listen(3000);

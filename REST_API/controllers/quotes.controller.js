const model_quote = require('../models/quote.model')

function getRandomQuote(req, res, next){
    return model_quote.getRandomQuote()
        .then(quote => {
            res.json({
                quote: quote
            })
        })
        .catch(error => {
            next(error)
        })
}


module.exports = {
    getRandomQuote : getRandomQuote
}
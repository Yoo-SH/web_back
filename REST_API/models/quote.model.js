const db = require('../data/database')

class Quote {
    static async getRandomQuote(){
        const quotes = await db.getDb().collection('quotes').find().toArray()
        const randomQuoteIndex = Math.floor(Math.random() * quotes.length)
        //math.random() returns a number between 0 and 1, so we multiply it by the length of the array to get a random index
        //[1,2,3,4,5] -> 5 => length: 5 => ex) 0.1 * 5 => 0.5 = Math.floor(0.5) => 0

        const randomQuote = quotes[randomQuoteIndex]

        return randomQuote.text;
    }
}

module.exports = Quote;
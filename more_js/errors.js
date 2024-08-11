const fs = require('fs');

function readFile()
{
    try {
        const fileData = fs.readFileSync('data.json');
    } catch{
        console.log('readFile error')
    }
    console.log('readFile함수 동작')
}

readFile()
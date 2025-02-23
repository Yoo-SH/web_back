const fs = require('fs');

function readFile()
{
    let fileData
    try {
        const fileData = fs.readFileSync('data.json'); //같은 이름의 변수이지만, 덮어쓰기 됨. 더 높은 우선순위를 가짐, 쉐도잉 기법이라고 함.
    } catch{
        console.log('readFile error')
    }
    console.log(fileData) //가변범위로 let fileData 적용
    console.log('readFile함수 동작')
}

readFile()

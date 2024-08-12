const fs = require('fs');

function readFile() {
    let fileData;

    fileData = fs.readFile('data.json', function(error,fileData){ //비동기식 파일 읽기.
        console.log('File parsing done!')
    }); 

    console.log('readFile error')
    console.log(fileData.toString()) //비동기로 파일을 읽기 떄문에, fileData가 할당되지 않았는데 읽어서 오류날 수 도 있음.
}


const fs = require('fs');

function readFile() {
    let fileData;

    fileData = fs.readFile('data.json', function(error,fileData){ //비동기식 파일 읽기.
        console.log('File parsing done!')
        console.log(fileData.toString()) //콜백함수(readFile이 완료되면 함수실행) 안에 넣어서 동기식으로 처리 가능. 여러개 프로그램 돌릴 떄 이런식으로 
    }); 

    console.log('readFile error')
  
}


readFile()
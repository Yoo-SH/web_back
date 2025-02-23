const fs = require('fs');

function readFile() {
    let fileData;

    fileData = fs.readFile('data.json', function (error, fileData) { //비동기식 파일 읽기.
        console.log('File parsing done!')
    });

    console.log('readFile error')
    console.log(fileData.toString()) //비동기로 파일을 읽기 떄문에, fileData가 할당되지 않았는데 읽어서 오류날 수 도 있음.
}


function readFile() {
    let fileData;

    fileData = fs.readFile('data.json', function (error, fileData) { //비동기식 파일 읽기.
        console.log('File parsing done!')
        console.log(fileData.toString()) //콜백함수(readFile이 완료되면 함수실행) 안에 넣어서 동기식으로 처리 가능. 여러개 프로그램 돌릴 떄 이런식으로 비동기 에러 처리
    });

    console.log('readFile error')

}


//콜백 함수가 여러 중첩으로 존재하는 콜백헬 현상(가시성이 떨어지는 현상)을 피하기 위해 프로미스를 쓸 수 있음.
fs = require('fs/promises');
function readFile() {
    let fileData;

    fileData = fs.readFile('data.json')
        .then(function (error, fileData) { //비동기식 파일 읽기.
            console.log('File parsing done!')
            console.log(fileData.toString()) //콜백함수(readFile이 완료되면 함수실행) 안에 넣어서 동기식으로 처리 가능. 여러개 프로그램 돌릴 떄 이런식으로 비동기 에러 처리
        })
        .then(function () { })
        .catch(function (error) {
            console.log(error)
        })


    console.log('readFile error')

}

//프로미스 코드를 async/await를 이용하여 더 간결하게 작성할 수있음/ 동시에 여러 작업을 하지 않을 경우 유용+try catch로도 사용가능
async function readFile() {

    try{
        fileData = await fs.readFile('data.json')
    } catch(error){
        console.log(error);
    }
      

    console.log('File parsing done!')
    console.log(fileData.toString()) //콜백함수(readFile이 완료되면 함수실행) 안에 넣어서 동기식으로 처리 가능. 여러개 프로그램 돌릴 떄 이런식으로 비동기 에러 처리
    console.log('readFile error')
}

readFile()
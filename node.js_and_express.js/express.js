const fs = require('fs'); //file system
const path = require('path'); //모든 os에서 작동하는 완전한 경로를 쉽게 지정

const express = require('express'); // express는 http와 달리 객체가 아닌 함수
const exp = require('constants');
const app = express();  // express()가 객체

//미들웨어 함수, 어떤 종류의 데이터가 있는지 확인 한 뒤에 해당 데이터를 추출.
app.use(express.urlencoded({extended : false})); //들어오는 요청 데이터 파서가 모든 데이터를 확인하는데, urlencoded가 찾는 데이터라면 포함된 데이터를 구문 분석하여 자바스크립트 객체로 변환시킴.

// localhost:3000/currenttime
app.get('/currenttime', function(request, response) {  // 'function'을 올바르게 작성합니다.
    response.send('<h1>' + new Date().toISOString() + '</h1>');  // express는 end가 아닌 send 현재 시간을 ISO 형식으로 응답
});

// localhost:3000/
app.get('/', function(request, response) {
    response.send('<form action="/store-user" method="POST"><label>Your Name:</label><input type="text" name="username"><button>Submit</button> '); // 응답 준비를 끝내고 HTML 요소를 보냅니다.  
})

app.get('/users', function(req,res){
    const filePath = path.join(__dirname, 'data', 'users.json');
    const fileData = fs.readFileSync(filePath);
    existingUsers = JSON.parse(fileData);

    let responseData = '<ul>';

    for(const users of existingUsers)
        responseData += '<li>' + users + '</li>'

    responseData += '<ul>';

    res.send(responseData)
})

app.post('/store-user', function(request, response) {
    const username = request.body.username; // 올바른 변수 참조

    const filePath = path.join(__dirname, 'data', 'users.json'); // 이 프로젝트 디렉토리에 대한 절대 경로를 실제 보류하는 유효한 JS 변수 상수

    let existingUsers = [];
    
    // 파일 읽기 및 예외 처리
    try {
        const fileData = fs.readFileSync(filePath);
        existingUsers = JSON.parse(fileData); // JSON 형식의 데이터를 포함한 일부 텍스트를 원시 JS 객체 또는 배열로 변환
    } catch (error) {
        // 파일이 없거나 비어 있는 경우, 빈 배열로 초기화
        if (error.code === 'ENOENT') {
            existingUsers = []; // 파일이 없으면 빈 배열로 초기화
        } else {
            throw error; // 다른 오류는 그대로 던집니다.
        }
    }

    existingUsers.push(username); // JS에서 배열 끝에 새로운 요소를 추가

    // 파일 쓰기
    fs.writeFileSync(filePath, JSON.stringify(existingUsers)); // JSON 형태로 변환하여 해당 경로에 텍스트로 다시 저장

    response.send('<h1>Username stored!</h1>');
});



// 서버가 3000번 포트에서 요청을 수신합니다.
app.listen(3000)

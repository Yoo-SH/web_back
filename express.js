const express = require('express'); // express는 http와 달리 객체가 아닌 함수
const app = express();  // express()가 객체

//미들웨어 함수, 어떤 종류의 데이터가 있는지 확인 한 뒤에 해당 데이터를 추출.
app.use(express.urlencoded({extended : false})); //들어오는 요청 데이터 파서가 모든 데이터를 확인하는데, urlencoded가 찾는 데이터라면 포함된 데이터를 구문 분석하여 자바스크립트 객체로 변환시킴.

// localhost:3000/currenttime
app.get('/currenttime', function(request, response) {  // 'function'을 올바르게 작성합니다.
    response.send('<h1>' + new Date().toISOString() + '</h1>');  // express는 end가 아닌 send 현재 시간을 ISO 형식으로 응답
});

// localhost:3000/
app.get('/', function(request, response) {
    response.send('<form action="/store-user" method="POST"><label>Your Name</label><input type="text" name="username"><button>Submit</button> '); // 응답 준비를 끝내고 HTML 요소를 보냅니다.  
})

app.post('/store-user', function(request,response){
    const username = request.body.username;
    console.log(username);
    response.send('<h1>Username stored!</h1>')
});


// 서버가 3000번 포트에서 요청을 수신합니다.
app.listen(3000)

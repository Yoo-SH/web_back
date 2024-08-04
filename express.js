const express = require('express'); // express는 http와 달리 객체가 아닌 함수
const app = express();  // express()가 객체

// localhost:3000/currenttime
app.get('/currenttime', function(request, response) {  // 'function'을 올바르게 작성합니다.
    response.send('<h1>' + new Date().toISOString() + '</h1>');  // express는 end가 아닌 send 현재 시간을 ISO 형식으로 응답
});

// localhost:3000/
app.get('/', function(request, response) {
    response.send("<h1>Hello client</h1>"); // 응답 준비를 끝내고 HTML 요소를 보냅니다.  
})
// 서버가 3000번 포트에서 요청을 수신합니다.
app.listen(3000)

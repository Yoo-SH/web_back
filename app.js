const http = require('http');


// request와 response는 Node.js에 의해 자동으로 할당됩니다.
function handleRequest(request, response) {
    if (request.url === '/currenttime') { // Node.js로 인해 할당된 request의 URL에 따라 새로운 response를 할당합니다.
        response.statusCode = 200;
        response.end('<h1>' + new Date().toISOString() + '</h1>'); // Date 객체를 생성하여 현재 시간을 ISO 문자열로 반환합니다.
    } else if (request.url === '/') {
        response.statusCode = 200; // 브라우저에 요청이 성공했는지 여부를 알리는 간단한 방법입니다. 200은 성공을 의미합니다.
        response.end("<h1>Hello client</h1>"); // 응답 준비를 끝내고 HTML 요소를 보냅니다.
    } else {
        response.statusCode = 404; // 요청된 URL이 정의되지 않은 경우 404 오류를 응답합니다.
        response.end("<h1>Page not found</h1>");
    }
}

const server = http.createServer(handleRequest);


server.listen(3000);
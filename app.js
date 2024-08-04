const http = require('http');


//request, response는 node.js에 의해 할당됨.
function handleRequest(request, response)
{
    response.statusCode = 200; //브라우저에 요청이 성공했는지 여부를 알리는 간단한 방법 200은 성공을 의미 cf) 404
    response.end("<h1>Hello client</h1>"); //응답 준비를 끝냄. 보낼 HTML요소를 보냄
}
const server = http.createServer(handleRequest);


server.listen(3000);
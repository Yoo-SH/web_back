function greetUser(geetingPrefix, user = 'Yoo-SH') //비선택적 매개변수, 선택적 매개변수 (비선택적 매개변수가 , 선택적 매개변수보다 앞에 있어야함.)
{
    console.log(geetingPrefix +' '+ user);
}


greetUser('hello');


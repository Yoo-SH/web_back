interface AddFn{
    (a: number, b: number): number;
}

let add_1: AddFn;

add_1 = (n1: number, n2: number) => {
    return n1 + n2; 
}


console.log(add_1(2, 3));
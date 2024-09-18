interface Greetable {
    name: string;

    greet(phrase: string): void;
}


let person_id : Greetable;

person_id = {
    name: "Max",
    greet(phrase: string) {
        console .log(phrase + " " + this.name);
    }
};

person_id.greet("Hello there - I am");

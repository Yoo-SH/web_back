"use strict";
class Person {
    constructor(n) {
        this.name = n;
    }
    greet(phrase) {
        console.log(phrase + " " + this.name);
    }
}
const person = new Person("Max");
person.greet("Hello there - I am");
//# sourceMappingURL=class_and_interface.js.map
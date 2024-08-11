//객체... 
const job = {
    title : "developer",
    location : "new york",
    salary : "5000"
}


//클래스 도면(블루프린트)를 이용하여 객체를 손쉽게 찍어냄.
class Job{
    constructor(jobTitle, place, salary) {
        this.title = jobTitle;
        this.place = place;
        this.salary = salary
    }

    describe() {
        console.log(
            `i'm a ${this.title}, I work in ${this.location} and I earn ${this.salary}.`
        )
    }
     
}

const developer = new Job('developer', 'New York', 50000);
const cook = new Job('cook','Mubuch', 35000)


developer.describe();
cook.describe();

//객체& 배열 구조화

const job2 = {title: 'developer', location: 'newYork'};
const {title, location } = job2;
console.log(`${title} and ${location}`); 

const {title: myTitle } = job2;
console.log(`${myTitle}`); 
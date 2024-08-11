const person ={
    age : 26
}

function getAdultYear(p)
{
    person.age -= 18;
    return p.age;
}



console.log(getAdultYear({...person}))
console.log(person)



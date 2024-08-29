

function postIsvalid(title, content){
    return enteredTitle &&
    enteredContent &&
    enteredTitle.trim() !== '' &&
    enteredContent.trim() !== ''
}


module.exports = {
    postIsvalid : postIsvalid
}
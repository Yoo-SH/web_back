const path = require('path');
const fs = require('fs');
const filePath = path.join(__dirname,'..' ,'data', 'restaurants.json');


function getStoredRestaurants(){
    const fileData = fs.readFileSync(filePath);
    const storedRestaurants = JSON.parse(fileData);
    
    return storedRestaurants;
}


function storedRestaurants(storableRestaurants)
{
    fs.writeFileSync(filePath, JSON.stringify(storableRestaurants));
}

/*좌 -다른 파일에서 노출된 함수를 사용할 때 사용할 수 있는 이름  : 우- 다른 파일에서 노출하려면 함수 */
module.exports = {
    getStoredRestaurants : getStoredRestaurants,
    storedRestaurants : storedRestaurants 
}
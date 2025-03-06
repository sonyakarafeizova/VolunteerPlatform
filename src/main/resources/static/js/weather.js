const boxImgA = document.getElementById('box-a-img');
const boxTempA = document.getElementById('box-a-temp');

const API_KEY = "e3a4c0a07d2f420a995115905250603";
const CITY = "Sofia";
const URL = `https://api.weatherapi.com/v1/current.json?key=${API_KEY}&q=${CITY}&aqi=no`;

fetch(URL)
    .then(response => response.json())
    .then(info => {
        boxTempA.innerText = Math.round(info.current.temp_c) + "°C";
        boxImgA.src = "https:" + info.current.condition.icon;
    })
    .catch(error => console.error("Error fetching data:", error));

const xhr = new XMLHttpRequest();

const counter = document.querySelector("#counter")
const button = document.querySelector("#button")
let i = 0;

const evtSource = new EventSource("http://localhost:8080/stats/tasks/new");



// window.onload = function() {
//
//     xhr.open("GET", "http://localhost:8080/stats/test");
//     xhr.send();
//
//     counter.innerHTML = (i).toString()
//     window.setInterval(tick, 500)
// };

// function tick() {
//     i++
//     console.log("tick")
//     xhr.open("GET", "http://localhost:8080/stats/test");
//     xhr.send()
//
//     counter.innerHTML = (i).toString()
// }

function testClick() {
    console.log("click")
    if (button.style.color === "red") {
        button.style.color = "green"
        xhr.open("GET", "http://localhost:8080/data/manipulate/resume");
        xhr.send();
    } else {
        button.style.color = "red"
        xhr.open("GET", "http://localhost:8080/data/manipulate/pause");
        xhr.send();
    }
}

evtSource.onmessage = (event) => {
    const newElement = document.createElement("li");
    const eventList = document.getElementById("list");

    newElement.textContent = `message: ${event.data}`;
    eventList.appendChild(newElement);
};

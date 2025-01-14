const table = document.getElementById('taskTable').getElementsByTagName('tbody')[0];
const xhr = new XMLHttpRequest();
const evtSource = new EventSource("http://localhost:8080/stats/tasks/new");

window.onload = function() {
    xhr.open("GET", "http://localhost:8080/stats/tasks/cached");
    xhr.send();

    xhr.responseType = "json";
    xhr.onload = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const data = xhr.response;
            console.log("cache received")
            console.log(data);
            for (var i = 0; i < data.tasks.length; i ++) {
                console.log("task from cache #" + i)
                const task = data.tasks[i];
                addTaskFromJson(task)
            }
        } else {
            console.log(`Error: ${xhr.status}`);
        }
    };
};

function addTask(taskId, taskNumber, userCreated, priority, completionTime, evaluation, evaluationDifference, user) {
    const newRow = table.insertRow();

    const cellTaskId = newRow.insertCell(0);
    const cellTaskNumber = newRow.insertCell(1);
    const cellUserCreated = newRow.insertCell(2);
    const cellPriority = newRow.insertCell(3);
    const cellCompletionTime = newRow.insertCell(4);
    const cellEvaluation = newRow.insertCell(5);
    const cellEvaluationDifference = newRow.insertCell(6);
    const cellUser = newRow.insertCell(7);

    cellTaskId.textContent = taskId;
    cellTaskNumber.textContent = taskNumber;
    cellUserCreated.textContent = userCreated ? "Yes" : "No";
    cellPriority.textContent = priority;
    cellCompletionTime.textContent = completionTime;
    cellEvaluation.textContent = evaluation;
    cellEvaluationDifference.textContent = evaluationDifference;
    cellUser.textContent = user;
}

function addTaskFromJson(task) {
    const eval = task.evaluation.estimatedCompletionTime
    const evalDiff = task.evaluationDifference.difference
    const userLogin = task.user.login

    addTask(
        task.taskId,
        task.taskNumber,
        task.userCreated,
        task.priority,
        task.completionTime,
        eval,
        evalDiff,
        userLogin
    )
}

evtSource.onmessage = (event) => {
    const task = JSON.parse(event.data)
    console.log(task)
    addTaskFromJson(task)
};
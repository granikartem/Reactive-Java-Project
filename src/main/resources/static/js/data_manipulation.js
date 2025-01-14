document.addEventListener('DOMContentLoaded', async () => {
    const actInput = document.getElementById('actInput');
    const delayInput = document.getElementById('delayInput');
    const actForm = document.getElementById('actForm');
    const delayForm = document.getElementById('delayForm');
    const taskForm = document.getElementById('taskForm');
    const pauseButton = document.getElementById('pauseButton');

    // Загрузка начальных значений
    try {
        const actResponse = await fetch('/data/manipulate/act');
        const actData = await actResponse.json();
        actInput.value = actData.duration;

        const delayResponse = await fetch('/data/manipulate/delay');
        const delayData = await delayResponse.json();
        delayInput.value = delayData;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
    }

    // Обработка отправки формы ACT
    actForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
            await fetch('/data/manipulate/act', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({"duration": actInput.value}),
            });
        } catch (error) {
            console.error('Ошибка при обновлении среднего времени выполнения:', error);
        }
    });

    // Обработка отправки формы Delay
    delayForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
            await fetch('/data/manipulate/delay', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({"delay": parseInt(delayInput.value)}),
            });
        } catch (error) {
            console.error('Ошибка при обновлении задержки:', error);
        }
    });

    taskForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const taskDto = {
            priority: document.getElementById('priority').value,
            completionTime: `PT${document.getElementById('completionTime').value}M`,
            evaluation: `PT${document.getElementById('evaluation').value}M`,
            userName: document.getElementById('userName').value,
            description: document.getElementById('description').value
        };

        const addTasksDto = {
            amount: parseInt(document.getElementById('amount').value),
            taskSpecification: taskDto
        };

        fetch('/data/manipulate/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(addTasksDto)
        })
            .then(response => response.json())
            .then(data => console.log('Success:', data))
            .catch((error) => console.error('Error:', error));
    });

    pauseButton.addEventListener('click', function (e) {
        e.preventDefault();

        console.log("click")
        if (pauseButton.style.color === "red") {
            pauseButton.style.color = "green"
            fetch('/data/manipulate/resume', {
                    method: 'GET'
                }
            );
        } else {
            pauseButton.style.color = "red"
            fetch('/data/manipulate/pause', {
                    method: 'GET'
                }
            );
        }
    });
});
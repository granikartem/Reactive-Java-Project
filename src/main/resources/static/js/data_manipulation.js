document.addEventListener('DOMContentLoaded', async () => {
    const actInput = document.getElementById('actInput');
    const delayInput = document.getElementById('delayInput');
    const actForm = document.getElementById('actForm');
    const delayForm = document.getElementById('delayForm');

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
                body: JSON.stringify({ "duration": actInput.value }),
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
                body: JSON.stringify({ "delay": parseInt(delayInput.value)}),
            });
        } catch (error) {
            console.error('Ошибка при обновлении задержки:', error);
        }
    });
});
function formatDuration(duration) {
    if (duration !== undefined) {
        const isNegative = duration < 0;
        duration = Math.abs(duration);
        const hours = Math.floor(duration / 3600);
        const minutes = Math.floor((duration % 3600) / 60);
        const seconds = duration % 60;
        return `${isNegative ? '-' : ''}${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    } else {
        const isNegative = false;
        const hours = 0;
        const minutes = 0;
        const seconds = 0;
        return `${isNegative ? '-' : ''}${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    }
}

function renderStatistics(statistics, title) {
    const container = document.getElementById('statistics');
    let html = `<h1>${title}</h1>`;
    html += '<table>';
    html += '<tr><th>Параметр</th><th>Значение</th></tr>';
    html += `<tr><td>Количество задач</td><td>${statistics.taskCounter}</td></tr>`;
    html += `<tr><td>Процент выполненных вовремя</td><td>${(statistics.doneOnTimePercentage * 100).toFixed(2)}%</td></tr>`;
    html += `<tr><td>Среднее время выполнения</td><td>${formatDuration(statistics.averageCompletionTime)}</td></tr>`;
    html += '<tr><td>Среднее время выполнения по статусам</td><td>';
    html += '<table>';
    const statusCompletionTimes = statistics.averageStatusCompletionTime;
    if (statusCompletionTimes != undefined) {
        for (const [status, time] of Object.entries(statusCompletionTimes)) {
            html += `<tr><td>${status}</td><td>${formatDuration(time)}</td></tr>`;
        }
    }
    html += '</table></td></tr>';
    html += `<tr><td>Среднее отклонение оценки</td><td>${formatDuration(statistics.averageEvaluationDifference)}</td></tr>`;
    html += '<tr><td>Среднее отклонение оценки по статусам</td><td>';
    html += '<table>';
    const statusEvaluationTimes = statistics.averageStatusEvaluationDifference;
    if (statusEvaluationTimes != undefined) {
        for (const [status, time] of Object.entries(statistics.averageStatusEvaluationDifference)) {
            html += `<tr><td>${status}</td><td>${formatDuration(time)}</td></tr>`;
        }
    }
    html += '</table></td></tr>';
    html += '</table>';
    container.innerHTML = html;
}

function fetchAndRenderStatistics(url, title, eventSource) {
    fetch(url)
        .then(response => response.json())
        .then(data => renderStatistics(data, title))
        .catch(error => console.error('Ошибка:', error));

    eventSource.onmessage = function(event) {
        const statistics = JSON.parse(event.data);
        console.log(event.data)
        renderStatistics(statistics, title)
    };
}

function init() {
    const path = window.location.pathname;

    if (path.endsWith('/stats')) {
        const eventSource = new EventSource(path + '/global')
        fetchAndRenderStatistics('/stats/old/global', 'Общая статистика', eventSource);
    } else if (path.match(/\/stats\/users\/[^/]+$/)) {
        const eventSource = new EventSource(path)
        const userName = path.split('/').pop();
        fetchAndRenderStatistics(`/stats/old/users/${userName}`, `Статистика пользователя ${userName}`, eventSource);
    } else if (path.match(/\/stats\/groups\/[^/]+$/)) {
        const eventSource = new EventSource(path)
        const groupName = path.split('/').pop();
        fetchAndRenderStatistics(`/stats/old/groups/${groupName}`, `Статистика группы ${groupName}`, eventSource);
    } else {
        document.getElementById('statistics').innerHTML = '<h1>Статистика не найдена</h1>';
    }
}

document.addEventListener('DOMContentLoaded', init);

function receive(msg, channel, event) {
    let data = JSON.parse(event.data);
    appendLine(data.from + ': ' + data.content);
}

function appendLine(line) {
    let textarea = document.getElementById('messageContainer');
    textarea.value += '\n' + line;
}

const postCommentElement = document.getElementById("postComment");
postCommentElement.addEventListener('click', createComment);

function createComment() {

    const causeId = document.getElementById('causeId').value;
    const messageElement = document.getElementById('message');
    const message = messageElement.value;

    console.log(causeId, message);


    if (!causeId || !message) {
        console.error('CauseId or Message is missing');
        return;
    }

    fetch("/api/comments", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ causeId, message })
    })
        .then(res => res.json())
        .then(data => {
            const id = data.id;
            const content = data.content;
            const authorName = data.fullName;

            console.log('Received ID:', id);
            console.log('Received Content:', content);
            console.log('Received Author Name:', authorName);


            const h4Element = document.createElement('h4');
            h4Element.appendChild(document.createTextNode(content));

            const labelElement = document.createElement('label');
            labelElement.appendChild(document.createTextNode(authorName));

            const formElement = document.createElement('form');
            formElement.setAttribute('method', 'POST');
            formElement.setAttribute('action', `/comments/delete/${causeId}/${id}`);

            const buttonElement = document.createElement('button');
            buttonElement.setAttribute('type', 'submit');
            buttonElement.classList.add('btn');
            buttonElement.classList.add('btn-danger');
            buttonElement.appendChild(document.createTextNode('Delete'));

            formElement.appendChild(buttonElement);

            const divElement = document.createElement('div');
            divElement.classList.add('form-group');

            divElement.appendChild(h4Element);
            divElement.appendChild(labelElement);
            divElement.appendChild(formElement);

            const commentsSectionElement = document.getElementById('comments');
            commentsSectionElement.append(divElement);


            messageElement.value = '';
        })
        .catch(error => {
            console.error('Error posting comment:', error);
        });
}

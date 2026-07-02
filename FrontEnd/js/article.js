const id = new URLSearchParams(location.search).get("id");

fetch(`http://localhost:8083/rest/articles/commande/${id}`)
.then(res => res.json())
.then(data => {
    let html = "";
    data.forEach(a => {
        html += `<div class="card">${a.typeVetement} - ${a.matiere}</div>`;
    });
    document.getElementById("list").innerHTML = html;
});

function add() {
    fetch("http://localhost:8083/rest/articles/create", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            typeVetement: document.getElementById("type").value,
            matiere: document.getElementById("matiere").value,
            commande: { id: id }
        })
    })
    .then(() => location.reload());
}

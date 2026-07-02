const user = JSON.parse(localStorage.getItem("user"));
if (!user) location.href = "login.html";

fetch(`http://localhost:8083/rest/commandes/user/${user.id}`)
.then(res => res.json())
.then(data => {
    let html = "";
    data.forEach(c => {
        html += `
            <div class="card">
                <b>Commande #${c.id}</b><br>
                Statut : ${c.statut}<br>
                Prix : ${c.prixTotal} DH<br>
                <button onclick="articles(${c.id})">Articles</button>
            </div>
        `;
    });
    document.getElementById("commandes").innerHTML = html;
});

function goCommande() {
    location.href = "commande.html";
}

function articles(id) {
    location.href = "articles.html?id=" + id;
}

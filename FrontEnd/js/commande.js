async function ajouterCommande() {

    const clientId = sessionStorage.getItem("pressin_id");

    const dto = {
        typeLivraison: "EN_MAGASIN"
    };

    await fetch(`http://localhost:8083/rest/commandes/create/${clientId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dto)
    });

    alert("Commande ajoutée !");
}
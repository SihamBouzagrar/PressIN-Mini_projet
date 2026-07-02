const AUTH_BASE = "http://localhost:8083";
let selectedRole = "ROLE_CLIENT";

function selectRole(element) {
    document.querySelectorAll('.role-tab').forEach(tab => {
        tab.classList.remove('active');
    });
    element.classList.add('active');
    selectedRole = element.getAttribute('data-role');
}

async function login() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const errorEl = document.getElementById("login-error");
    errorEl.textContent = "";

    if (!email || !password) {
        errorEl.textContent = "Veuillez remplir tous les champs.";
        return;
    }

    try {
        const res = await fetch(`${AUTH_BASE}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, passWord: password })
        });

        if (!res.ok) {
            const msg = await res.text();
            console.error("Erreur backend:", msg);
            errorEl.textContent = "Email ou mot de passe incorrect.";
            return;
        }

        const user = await res.json();
        console.log("USER REÇU:", user); // ← voir ce que retourne le backend

        // ✅ role est déjà "ROLE_ADMIN" / "ROLE_CLIENT" / "ROLE_LIVREUR"
        const role = user.role || "";

        if (!role) {
            errorEl.textContent = "Rôle utilisateur introuvable.";
            return;
        }

        // ✅ stockage avec le vrai rôle
        sessionStorage.setItem("pressin_role", role);
        sessionStorage.setItem("pressin_email", user.email);
        sessionStorage.setItem("pressin_name", user.firstname || "");
        sessionStorage.setItem("pressin_id", user.id);

        // ✅ comparaison avec ROLE_XXXX
        if (role === "ROLE_ADMIN") {
            window.location.href = "admin.html";
        } else if (role === "ROLE_CLIENT") {
            window.location.href = "client.html";
        } else if (role === "ROLE_LIVREUR") {
            window.location.href = "livreurs.html";
        } else {
            errorEl.textContent = "Rôle inconnu : " + role;
        }

    } catch (e) {
        console.error("Erreur réseau:", e);
        errorEl.textContent = "Impossible de contacter le serveur.";
    }
}

async function handleRegister(event) {
    event.preventDefault();

    const user = {
        firstname: document.getElementById("register-prenom").value,
        lastname: document.getElementById("register-nom").value,
        telephone: document.getElementById("register-telephone").value,
        email: document.getElementById("register-email").value,
        adresse: document.getElementById("register-adresse").value,
        passWord: document.getElementById("register-password").value,
        role: document.getElementById("register-role").value
    };

    // sécurité front
    if (user.role === "ROLE_ADMIN") {
        alert("Interdit");
        return;
    }

    try {
        const response = await fetch("http://localhost:8083/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        });

        if (!response.ok) {
            document.getElementById("error-register").innerText = await response.text();
            return;
        }

        alert("Compte créé !");
        window.location.href = "index.html";

    } catch (e) {
        console.error(e);
    }
}
function logout() {
    sessionStorage.clear();
    window.location.href = "login.html";
}
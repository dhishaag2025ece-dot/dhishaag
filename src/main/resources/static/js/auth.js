const API_URL = "/api";

/* =========================
   LOGIN
========================= */

const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const role = document.getElementById("role").value;
        const message = document.getElementById("message");

        try {
            const response = await fetch(API_URL + "/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                    role: role
                })
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || "Login failed");
            }

            localStorage.setItem("employeeId", data.id);
            localStorage.setItem("employeeName", data.name);
            localStorage.setItem("employeeRole", data.role);
            localStorage.setItem("employeeEmail", data.email);

            if (data.role === "ADMIN") {
                window.location.href = "admin.html";
            } else {
                window.location.href = "dashboard.html";
            }

        } catch (error) {
            message.textContent = error.message;
            message.style.color = "red";
        }
    });
}


/* =========================
   REGISTER
========================= */

const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const message = document.getElementById("message");

        try {
            const response = await fetch(API_URL + "/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: password
                })
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || "Registration failed");
            }

            message.textContent = "Registration successful! Redirecting to login...";
            message.style.color = "green";

            setTimeout(function () {
                window.location.href = "index.html";
            }, 1000);

        } catch (error) {
            message.textContent = error.message;
            message.style.color = "red";
        }
    });
}
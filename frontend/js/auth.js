const API_BASE_URL = "http://localhost:8080/api";

function showAlert(message, type = "danger") {
    const alertBox = document.getElementById("alertBox");

    if (alertBox) {
        alertBox.innerHTML = `
            <div class="alert alert-${type}" role="alert">
                ${escapeHtml(message)}
            </div>
        `;
    }
}

function escapeHtml(value) {
    const div = document.createElement("div");
    div.textContent = value ?? "";
    return div.innerHTML;
}

function getToken() {
    return localStorage.getItem("taskflow_token");
}

function saveAuth(data) {
    if (!data || !data.token) {
        throw new Error("Authentication token was not received from server.");
    }

    localStorage.setItem("taskflow_token", data.token);

    localStorage.setItem(
        "taskflow_user",
        JSON.stringify({
            id: data.userId,
            name: data.name,
            email: data.email,
            role: data.role
        })
    );
}

async function parseResponse(response) {
    const text = await response.text();

    if (!text) {
        return {};
    }

    try {
        return JSON.parse(text);
    } catch {
        return {
            message: text
        };
    }
}

document.addEventListener("DOMContentLoaded", () => {

    // If already logged in, go to dashboard
    if (
        getToken() &&
        (
            location.pathname.endsWith("index.html") ||
            location.pathname.endsWith("register.html") ||
            location.pathname.endsWith("/")
        )
    ) {
        location.href = "dashboard.html";
        return;
    }

    // =========================
    // LOGIN
    // =========================

    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            const loginBtn = document.getElementById("loginBtn");

            const emailInput = document.getElementById("email");
            const passwordInput = document.getElementById("password");

            const email = emailInput ? emailInput.value.trim() : "";
            const password = passwordInput ? passwordInput.value : "";

            if (!email || !password) {
                showAlert("Please enter email and password.");
                return;
            }

            loginBtn.disabled = true;
            loginBtn.textContent = "Signing in...";

            try {
                const response = await fetch(
                    `${API_BASE_URL}/auth/login`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            email: email,
                            password: password
                        })
                    }
                );

                const data = await parseResponse(response);

                if (!response.ok) {
                    throw new Error(
                        data.message ||
                        data.error ||
                        "Login failed."
                    );
                }

                saveAuth(data);

                location.href = "dashboard.html";

            } catch (error) {
                showAlert(error.message);
            } finally {
                loginBtn.disabled = false;
                loginBtn.textContent = "Sign In";
            }
        });
    }


    // =========================
    // REGISTER
    // =========================

    const registerForm = document.getElementById("registerForm");

    if (registerForm) {
        registerForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            const registerBtn = document.getElementById("registerBtn");

            // Get form fields explicitly
            const nameInput = document.getElementById("name");
            const emailInput = document.getElementById("email");
            const passwordInput = document.getElementById("password");

            const name = nameInput ? nameInput.value.trim() : "";
            const email = emailInput ? emailInput.value.trim() : "";
            const password = passwordInput ? passwordInput.value : "";

            // Frontend validation
            if (!name) {
                showAlert("Please enter your full name.");
                return;
            }

            if (!email) {
                showAlert("Please enter your email address.");
                return;
            }

            if (!password) {
                showAlert("Please enter your password.");
                return;
            }

            if (password.length < 8) {
                showAlert("Password must be at least 8 characters.");
                return;
            }

            registerBtn.disabled = true;
            registerBtn.textContent = "Creating account...";

            try {
                const response = await fetch(
                    `${API_BASE_URL}/auth/register`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            name: name,
                            email: email,
                            password: password
                        })
                    }
                );

                const data = await parseResponse(response);

                if (!response.ok) {
                    throw new Error(
                        data.message ||
                        data.error ||
                        "Registration failed."
                    );
                }

                // Save JWT and user information
                saveAuth(data);

                // Go to dashboard
                location.href = "dashboard.html";

            } catch (error) {
                showAlert(error.message);
            } finally {
                registerBtn.disabled = false;
                registerBtn.textContent = "Create Account";
            }
        });
    }
});
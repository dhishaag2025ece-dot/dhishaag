const API_URL = "/api";

const addRoomForm =
    document.getElementById("addRoomForm");


if (addRoomForm) {

    addRoomForm.addEventListener("submit",
        async function (event) {

            event.preventDefault();

            const adminId =
                localStorage.getItem("employeeId");

            const roomName =
                document.getElementById("roomName").value;

            const capacity =
                document.getElementById("capacity").value;

            const amenities =
                document.getElementById("amenities").value;

            const message =
                document.getElementById("message");

            if (!adminId) {
                message.textContent =
                    "Admin login required.";
                message.style.color = "red";
                return;
            }

            try {

                const response = await fetch(
                    API_URL + "/rooms",
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "X-Admin-Id": adminId
                        },
                        body: JSON.stringify({
                            name: roomName,
                            capacity: Number(capacity),
                            amenities: amenities
                        })
                    }
                );

                const data = await response.json();

                if (!response.ok) {
                    throw new Error(
                        data.error || "Unable to add room"
                    );
                }

                message.textContent =
                    "Room added successfully!";

                message.style.color = "green";

                addRoomForm.reset();

            } catch (error) {

                message.textContent =
                    error.message;

                message.style.color = "red";
            }
        });
}


/* =========================
   LOAD USERS
========================= */

async function loadUsers() {

    const container =
        document.getElementById("usersContainer");

    if (!container) {
        return;
    }

    const adminId =
        localStorage.getItem("employeeId");

    if (!adminId) {
        container.innerHTML =
            "<p>Admin login required.</p>";
        return;
    }

    try {

        const response = await fetch(
            API_URL + "/employees",
            {
                headers: {
                    "X-Admin-Id": adminId
                }
            }
        );

        const users = await response.json();

        if (!response.ok) {
            throw new Error(
                users.error || "Unable to load users"
            );
        }

        container.innerHTML = "";

        if (users.length === 0) {
            container.innerHTML =
                "<p>No users found.</p>";
            return;
        }

        users.forEach(function (user) {

            const card =
                document.createElement("div");

            card.className = "booking-card";

            card.innerHTML = `
                <p><strong>Name:</strong> ${user.name}</p>
                <p><strong>Email:</strong> ${user.email}</p>
                <p><strong>Role:</strong> ${user.role}</p>
            `;

            container.appendChild(card);
        });

    } catch (error) {

        container.innerHTML =
            `<p style="color:red;">${error.message}</p>`;
    }
}

loadUsers();
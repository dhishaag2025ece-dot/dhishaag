const API_URL = "/api";

const bookingForm = document.getElementById("bookingForm");
const roomSelect = document.getElementById("roomId");

async function loadRoomsForBooking() {

    if (!roomSelect) {
        return;
    }

    try {
        const response = await fetch(API_URL + "/rooms");

        if (!response.ok) {
            throw new Error("Unable to load rooms");
        }

        const rooms = await response.json();

        rooms.forEach(function (room) {

            const option = document.createElement("option");

            option.value = room.id;
            option.textContent =
                `${room.name} - Capacity ${room.capacity}`;

            roomSelect.appendChild(option);
        });

        const selectedRoomId =
            localStorage.getItem("selectedRoomId");

        if (selectedRoomId) {
            roomSelect.value = selectedRoomId;
        }

    } catch (error) {
        document.getElementById("message").textContent =
            error.message;
    }
}


if (bookingForm) {

    bookingForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const employeeId =
            localStorage.getItem("employeeId");

        if (!employeeId) {
            alert("Please login first.");
            window.location.href = "index.html";
            return;
        }

        const roomId =
            document.getElementById("roomId").value;

        const date =
            document.getElementById("date").value;

        const startTime =
            document.getElementById("startTime").value;

        const endTime =
            document.getElementById("endTime").value;

        const message =
            document.getElementById("message");

        try {

            const response = await fetch(
                API_URL + "/bookings",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        roomId: Number(roomId),
                        employeeId: Number(employeeId),
                        date: date,
                        startTime: startTime,
                        endTime: endTime
                    })
                }
            );

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || "Booking failed");
            }

            sessionStorage.setItem(
                "lastBooking",
                JSON.stringify(data)
            );

            window.location.href =
                "confirmation.html";

        } catch (error) {

            message.textContent =
                error.message;

            message.style.color = "red";
        }
    });
}

loadRoomsForBooking();
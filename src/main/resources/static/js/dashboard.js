const API_URL = "/api";

async function loadRooms() {

    const roomsContainer = document.getElementById("roomsContainer");

    if (!roomsContainer) {
        return;
    }

    try {
        const response = await fetch(API_URL + "/rooms");

        if (!response.ok) {
            throw new Error("Unable to load rooms");
        }

        const rooms = await response.json();

        roomsContainer.innerHTML = "";

        if (rooms.length === 0) {
            roomsContainer.innerHTML = "<p>No rooms available.</p>";
            return;
        }

        rooms.forEach(function (room) {

            const card = document.createElement("div");
            card.className = "room-card";

            const availabilityClass =
                room.available ? "available" : "unavailable";

            const availabilityText =
                room.available ? "Available" : "Currently Booked";

            card.innerHTML = `
                <h3>${room.name}</h3>
                <p><strong>Capacity:</strong> ${room.capacity}</p>
                <p><strong>Amenities:</strong> ${room.amenities || "None"}</p>
                <p class="${availabilityClass}">
                    ${availabilityText}
                </p>
                <button onclick="bookRoom(${room.id})">
                    Book This Room
                </button>
            `;

            roomsContainer.appendChild(card);
        });

    } catch (error) {
        roomsContainer.innerHTML =
            `<p style="color:red;">${error.message}</p>`;
    }
}

function bookRoom(roomId) {
    localStorage.setItem("selectedRoomId", roomId);
    window.location.href = "booking.html";
}

loadRooms();
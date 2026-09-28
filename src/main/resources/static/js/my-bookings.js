const API_URL = "/api";

async function loadMyBookings() {


const container = document.getElementById("bookingsContainer");

if (!container) {
    return;
}

const employeeId = localStorage.getItem("employeeId");

if (!employeeId) {
    container.innerHTML = `
    <p style="color:red;">
        Please login again.
</p>
    `;
    return;
}

try {

    const response = await fetch(
        API_URL + "/bookings/employee/" + employeeId
    );

    const data = await response.json();

    if (!response.ok) {
        throw new Error(
            data.error || "Unable to load bookings"
        );
    }

    container.innerHTML = "";

    if (!Array.isArray(data) || data.length === 0) {

        container.innerHTML = `
    <p>No bookings found.</p>
    `;

        return;
    }

    data.forEach(function (booking) {

        const card = document.createElement("div");

        card.className = "booking-card";

        const roomName =
            booking.room
                ? booking.room.name
                : "Room";

        let buttons = "";

        if (booking.status === "CONFIRMED") {

            buttons = `
    <button onclick="cancelBooking(${booking.id})">
        Cancel Booking
    </button>

    ${
        booking.checkedIn
            ? ""
            : `
                            <button onclick="checkIn(${booking.id})">
                                Check In
                            </button>
                          `
    }
    `;
        }

        card.innerHTML = `
    <h3>${roomName}</h3>

    <p>
        <strong>Date:</strong>
        ${booking.bookingDate}
    </p>

    <p>
        <strong>Time:</strong>
        ${booking.startTime}
        -
        ${booking.endTime}
    </p>

    <p>
        <strong>Status:</strong>
        ${booking.status}
    </p>

    <p>
        <strong>Checked In:</strong>
        ${booking.checkedIn ? "Yes" : "No"}
    </p>

    ${buttons}
    `;

        container.appendChild(card);
    });

} catch (error) {

    console.error("My Bookings Error:", error);

    container.innerHTML = `
    <p style="color:red;">
        Unable to load bookings.
</p>
    `;
}
```

}

async function cancelBooking(id) {

    ```
if (!confirm("Are you sure you want to cancel this booking?")) {
    return;
}

try {

    const response = await fetch(
        API_URL + "/bookings/" + id + "/cancel",
        {
            method: "PUT"
        }
    );

    const data = await response.json();

    if (!response.ok) {
        throw new Error(
            data.error || "Cancellation failed"
        );
    }

    alert("Booking cancelled successfully.");

    loadMyBookings();

} catch (error) {

    alert(error.message);
}
```

}

async function checkIn(id) {

    ```
try {

    const response = await fetch(
        API_URL + "/bookings/" + id + "/check-in",
        {
            method: "PUT"
        }
    );

    const data = await response.json();

    if (!response.ok) {
        throw new Error(
            data.error || "Check-in failed"
        );
    }

    alert("Check-in successful.");

    loadMyBookings();

} catch (error) {

    alert(error.message);
}


}

loadMyBookings();

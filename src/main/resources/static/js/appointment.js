const APPOINTMENT_API =
    "http://localhost:8080/api/appointments";

async function createAppointment(data) {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(APPOINTMENT_API, {

            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8",
                "Authorization":
                    "Bearer " + token
            },

            body: JSON.stringify(data)
        });

    if (!response.ok) {

        throw new Error(
            await response.text()
        );
    }

    return response.json();
}
async function getAppointmentById(id) {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            `${APPOINTMENT_API}/${id}`,
            {
                headers: {
                    Authorization:
                        "Bearer " + token
                }
            }
        );

    if (!response.ok) {

        throw new Error(
            "Không lấy được lịch khám"
        );
    }

    return await response.json();
}

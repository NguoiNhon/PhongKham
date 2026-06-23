const DOCTOR_API = `${API_BASE}/appointments`;

/* =========================
   GET MY APPOINTMENTS
========================= */

async function getDoctorAppointments() {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            `${DOCTOR_API}/doctor/me`,
            {
                headers: {
                    Authorization:
                        `Bearer ${token}`
                }
            }
        );

    if (!response.ok) {

        throw new Error(
            "Không load được lịch khám"
        );
    }

    return await response.json();
}

/* =========================
   UPDATE MEDICAL STATUS
========================= */

async function updateMedicalStatus(
    appointmentId,
    medicalStatus
) {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            `${DOCTOR_API}/${appointmentId}/medical-status`,
            {
                method: "PUT",

                headers: {
                    "Content-Type":
                        "application/json; charset=UTF-8",

                    Authorization:
                        `Bearer ${token}`
                },

                body: JSON.stringify({
                    medicalStatus
                })
            }
        );

    if (!response.ok) {

        const msg =
            await response.text();

        throw new Error(msg);
    }

    return await response.text();
}
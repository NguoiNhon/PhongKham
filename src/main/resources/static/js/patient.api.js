async function getMyProfilePatient() {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            `${API_BASE}/patients/me`,
            {
                headers: {
                    Authorization:
                        `Bearer ${token}`
                }
            }
        );

    if (!response.ok) {

        throw new Error(
            "Không lấy được thông tin bệnh nhân"
        );
    }

    return await response.json();
}

async function getMyAppointments() {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            `${API_BASE}/appointments/patient/me`,
            {
                headers: {
                    Authorization:
                        `Bearer ${token}`
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

async function getPaymentByAppointmentId(
    appointmentId
) {

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            `${API_BASE}/payment/appointment/${appointmentId}`,
            {
                headers: {
                    Authorization:
                        `Bearer ${token}`
                }
            }
        );

    if (!response.ok) {

        throw new Error(
            "Không lấy được hoá đơn"
        );
    }

    return await response.json();
}
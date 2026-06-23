async function getAllAppointments() {

    return await api(
        "/appointments"
    );
}
async function updateMedicalStatusApi(
    id,
    medicalStatus
) {

    return await api(
        `/appointments/${id}/medical-status`,
        {
            method: "PUT",

            body: JSON.stringify({
                medicalStatus
            })
        }
    );
}
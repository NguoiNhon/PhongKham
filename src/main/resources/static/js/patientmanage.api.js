async function getPatients() {

    return await api(
        "/patients"
    );
}

async function createPatient(data) {

    return await api(
        "/patients",
        {
            method: "POST",

            body: JSON.stringify(
                data
            )
        }
    );
}

async function updatePatient(
    id,
    data
) {

    return await api(
        `/patients/${id}`,
        {
            method: "PUT",

            body: JSON.stringify(
                data
            )
        }
    );
}

async function deletePatient(id) {

    return await api(
        `/patients/${id}`,
        {
            method: "DELETE"
        }
    );
}
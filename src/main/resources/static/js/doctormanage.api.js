async function getDoctors() {
    return await api("/doctors");
}

async function getDoctorById(id) {
    return await api(`/doctors/${id}`);
}

async function createDoctor(data) {
    return await api("/doctors", {
        method: "POST",
        body: data // 🔥 KHÔNG stringify
    });
}

async function updateDoctor(id, data) {
    return await api(`/doctors/${id}`, {
        method: "PUT",
        body: data
    });
}

async function getDepartments() {
    return await api("/departments");
}
async function deleteDoctor(id) {

    const res = await fetch(`/api/doctors/${id}`, {
        method: "DELETE"
    });

    // 🔥 Nếu lỗi → đọc message từ backend
    if (!res.ok) {

        let message = "Xóa thất bại";

        try {
            // nếu backend trả text
            message = await res.text();
        } catch (e) {}

        throw new Error(message);
    }
}
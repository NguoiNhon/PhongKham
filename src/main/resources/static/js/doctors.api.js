async function loadDepartments() {
    return await api("/departments");
}

async function getDoctors(departmentId = null) {

    let url = "/doctors";

    if (departmentId && departmentId !== "all") {
        url += `?departmentId=${departmentId}`;
    }

    return await api(url);
}
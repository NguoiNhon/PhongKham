async function getDepartments() {
    return await api("/departments");
}

async function createDepartment(data) {

    return await api(
        "/departments",
        {
            method: "POST",

            body: JSON.stringify(data)
        }
    );
}

async function createRule(data) {

    return await api(
        "/time-slot-rules",
        {
            method: "POST",

            body: JSON.stringify(data)
        }
    );
}
async function updateDepartment(id, data) {

    return await api(
        `/departments/${id}`,
        {
            method: "PUT",

            body: JSON.stringify(data)
        }
    );
}

async function deleteDepartment(id) {

    return await api(
        `/departments/${id}`,
        {
            method: "DELETE"
        }
    );
}

async function getRuleByDepartmentId(
    departmentId
) {

    return await api(
        `/time-slot-rules/department/${departmentId}`
    );
}

async function updateRule(id, data) {

    return await api(
        `/time-slot-rules/${id}`,
        {
            method: "PUT",

            body: JSON.stringify(data)
        }
    );
}
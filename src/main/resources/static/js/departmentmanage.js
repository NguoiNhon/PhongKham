document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const ok =
            await requireRole("ADMIN");

        if (!ok) {
            return;
        }

        renderDepartments();
    }
);

let editingDepartmentId = null;

let isEditMode = false;

let editingRuleId = null;

let departmentList = [];
async function renderDepartments() {

    departmentList =
        await getDepartments();

    const list =
        departmentList;

    $("#content").innerHTML = `

    <input
        type="text"
        id="searchDepartment"
        placeholder="Tìm khoa..."
        onkeyup="searchDepartments()"
        style="margin-bottom:10px;padding:8px;width:300px"
    />

    <table class="table">

        <thead>

            <tr>

                <th>ID</th>

                <th>Tên khoa</th>

                <th>Mô tả</th>

                <th>Thao tác</th>

            </tr>

        </thead>

        <tbody>

            ${list.map(d => `

                <tr>

                    <td>${d.id}</td>

                    <td>${d.name}</td>

                    <td>${d.description || ""}</td>

                    <td style="display:flex;gap:8px">

                        <button 
                            class="btn btn-primary"
                            onclick="openEditDepartmentModal(${d.id})"
                        >
                            Sửa
                        </button>

                        <button 
                            class="btn"
                            onclick="handleDeleteDepartment(${d.id})"
                        >
                            Xóa
                        </button>

                    </td>

                </tr>

            `).join("")}

        </tbody>

    </table>
    `;
}

function openCreateDepartmentModal() {

    isEditMode = false;

    editingDepartmentId = null;

    $("#modalTitle").innerText =
        "Thêm khoa";

    $("#saveDepartmentBtn").innerText =
        "Tạo khoa";

    $("#departmentName").value = "";

    $("#departmentDescription").value = "";

    $("#departmentModal").style.display =
        "flex";
}

function closeDepartmentModal() {

    editingDepartmentId = null;

    editingRuleId = null;

    isEditMode = false;

    $("#departmentName").value = "";

    $("#departmentDescription").value = "";

    $("#departmentModal").style.display =
        "none";
}

async function handleCreateDepartment() {

    try {

        const departmentData = {

            name:
                $("#departmentName").value,

            description:
                $("#departmentDescription").value
        };

        const ruleData = {

            departmentId:
                editingDepartmentId,

            slotDuration:
                Number($("#slotDuration").value),

            workingStart:
                $("#workingStart").value,

            workingEnd:
                $("#workingEnd").value,

            lunchStart:
                $("#lunchStart").value,

            lunchEnd:
                $("#lunchEnd").value,

            maxPatientsPerDay:
                Number($("#maxPatientsPerDay").value)
        };

        // ======================
        // UPDATE
        // ======================

        if (isEditMode) {

            // update department
            await updateDepartment(
                editingDepartmentId,
                departmentData
            );

            // update rule
            await updateRule(
                editingRuleId,
                ruleData
            );

            toast(
                "Cập nhật khoa thành công"
            );
        }

        // ======================
        // CREATE
        // ======================

        else {

            const department =
                await createDepartment(
                    departmentData
                );

            ruleData.departmentId =
                department.id;

            await createRule(ruleData);

            toast(
                "Tạo khoa thành công"
            );
        }

        closeDepartmentModal();

        await renderDepartments();

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}
async function openEditDepartmentModal(id) {

    try {

        const department =
            departmentList.find(
                d => d.id === id
            );

        if (!department) {
            return;
        }

        // lấy rule
        const rule =
            await getRuleByDepartmentId(id);

        isEditMode = true;

        editingDepartmentId = id;

        editingRuleId = rule.id;

        $("#modalTitle").innerText =
            "Cập nhật khoa";

        $("#saveDepartmentBtn").innerText =
            "Lưu thay đổi";

        // department
        $("#departmentName").value =
            department.name;

        $("#departmentDescription").value =
            department.description || "";

        // rule
        $("#slotDuration").value =
            rule.slotDuration || 30;

        $("#workingStart").value =
            rule.workingStart || "08:00";

        $("#workingEnd").value =
            rule.workingEnd || "17:00";

        $("#lunchStart").value =
            rule.lunchStart || "12:00";

        $("#lunchEnd").value =
            rule.lunchEnd || "13:00";

        $("#maxPatientsPerDay").value =
            rule.maxPatientsPerDay || 20;

        $("#departmentModal").style.display =
            "flex";

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}
async function handleDeleteDepartment(id) {

    const ok =
        confirm("Bạn có chắc muốn xóa khoa này?");

    if (!ok) {
        return;
    }

    try {

        await deleteDepartment(id);

        toast("Xóa khoa thành công");

        renderDepartments();

    } catch (err) {

        toast(
            err.message,
            "error"
        );
    }
}
function searchDepartments() {

    const keyword =
        $("#searchDepartment")
            .value
            .toLowerCase();

    const rows =
        document.querySelectorAll("tbody tr");

    rows.forEach(row => {

        const text =
            row.innerText.toLowerCase();

        row.style.display =
            text.includes(keyword)
                ? ""
                : "none";
    });
}
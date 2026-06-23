let editingDoctorId = null;

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const ok =
            await requireRole("ADMIN");

        if (!ok) {
            return;
        }

        await loadDepartments();

        renderDoctors();
    }
);

async function loadDepartments() {

    const departments =
        await getDepartments();

    $("#departmentId").innerHTML =
        departments.map(d => `

            <option value="${d.id}">
                ${d.name}
            </option>

        `).join("");
}

async function renderDoctors() {

    try {

        const list =
            await getDoctors();

        $("#content").innerHTML = `

        <input
            type="text"
            id="searchDoctor"
            placeholder="Tìm bác sĩ..."
            onkeyup="searchDoctors()"
            style="margin-bottom:10px;padding:8px;width:300px"
        />

        <table class="table">

            <thead>

                <tr>

                    <th>ID</th>
                    <th>Tên</th>
                    <th>SĐT</th>
                    <th>Giới tính</th>
                    <th>Ngày sinh</th>
                    <th>Kinh nghiệm</th>
                    <th>Khoa</th>
                    <th></th>

                </tr>

            </thead>

            <tbody>

                ${list.map(d => `

                    <tr>

                        <td>${d.id}</td>
                        <td>${d.fullName}</td>
                        <td>${d.phoneNumber || ""}</td>
                        <td>${d.gender || ""}</td>
                        <td>${d.dateOfBirth || ""}</td>
                        <td>${d.yearsOfExperience || 0} năm</td>
                        <td>${d.departmentName}</td>

                        <td>

                            <button
                                class="btn btn-primary"
                                onclick="openEditDoctorModal(${d.id})"
                            >
                                Sửa
                            </button>

                            <button
                                class="btn btn-danger"
                                onclick="handleDeleteDoctor(${d.id})"
                            >
                                Xóa
                            </button>

                        </td>

                    </tr>

                `).join("")}

            </tbody>

        </table>
        `;

    } catch (err) {

        toast(
            err.message,
            "error"
        );
    }
}

function openCreateDoctorModal() {

    editingDoctorId = null;

    $("#modalTitle").textContent =
        "Thêm bác sĩ";

    [
        "fullName",
        "phone",
        "experienceYears",
        "dateOfBirth"
    ].forEach(id => {

        $("#" + id).value = "";
    });
    $("#gender").value = "";

    $("#doctorModal").style.display =
        "flex";
}

async function openEditDoctorModal(id) {

    try {
        editingDoctorId = id;

        const d = await getDoctorById(id);

        // đảm bảo đã load department
        await loadDepartments();

        $("#modalTitle").textContent = "Cập nhật bác sĩ";

        $("#fullName").value = d.fullName;
        $("#phone").value = d.phoneNumber;

        // FIX CHÍNH Ở ĐÂY
        $("#departmentId").value = d.department?.id;

        $("#gender").value = d.gender;
        $("#experienceYears").value = d.yearsOfExperience;
        $("#dateOfBirth").value = d.dateOfBirth;

        $("#doctorModal").style.display = "flex";

    } catch (err) {
        toast(err.message, "error");
    }
}

function closeDoctorModal() {

    $("#doctorModal").style.display =
        "none";
}

async function handleSaveDoctor() {

    try {

        // 🔥 object data
        const data = {
            fullName: $("#fullName").value,
            phoneNumber: $("#phone").value,
            gender: $("#gender").value,
            dateOfBirth: $("#dateOfBirth").value,
            yearsOfExperience: Number($("#experienceYears").value),
            departmentId: Number($("#departmentId").value)
        };
        // 🔥 VALIDATE Ở ĐÂY
        if (
            !data.fullName ||
            !data.phoneNumber ||
            !data.gender ||
            !data.dateOfBirth ||
            !data.yearsOfExperience ||
            !data.departmentId
        ) {
            toast("Vui lòng điền đầy đủ thông tin", "error");
            return;
        }

        // ép kiểu sau khi validate
        data.yearsOfExperience = Number(data.yearsOfExperience);
        data.departmentId = Number(data.departmentId);


        // 🔥 multipart/form-data
        const formData = new FormData();

        // backend đang nhận @RequestPart("data")
        formData.append(
            "data",
            new Blob(
                [JSON.stringify(data)],
                {
                    type: "application/json"
                }
            )
        );

        // backend đang nhận @RequestPart("file")
        const file =
            $("#imageFile").files[0];

        if (file) {
            formData.append(
                "file",
                file
            );
        }

        // UPDATE
        if (editingDoctorId) {

            await updateDoctor(
                editingDoctorId,
                formData
            );

            toast(
                "Cập nhật thành công"
            );

        }

        // CREATE
        else {

            await createDoctor(
                formData
            );

            toast(
                "Tạo bác sĩ thành công"
            );
        }

        closeDoctorModal();

        renderDoctors();

    } catch (err) {

        toast(
            err.message,
            "error"
        );
    }
}
function searchDoctors() {

    const keyword =
        $("#searchDoctor")
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
async function handleDeleteDoctor(id) {

    if (!confirm("Bạn có chắc muốn xoá bác sĩ này?")) {
        return;
    }

    try {

        await deleteDoctor(id); // gọi API

        toast("Xóa thành công");

        renderDoctors(); // reload lại list

    } catch (err) {

        toast(err.message || "Bác sĩ đã phát sinh lịch khám, không thể xoá bác sĩ", "error");
    }
}
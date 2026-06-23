document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const user =
            Auth.current();

        if (
            !user ||
            ![
                "ADMIN",
                "RECEPTIONIST"
            ].includes(user.role)
        ) {

            location.href =
                "index.html";

            return;
        }

        renderPatients();
    }
);

let patientList = [];

let editingPatientId = null;

let isEditMode = false;

/* =========================
   RENDER
========================= */

async function renderPatients() {

    try {

        patientList =
            await getPatients();

        $("#content").innerHTML = `


            <input
                type="text"
                id="searchPatient"
                placeholder="Tìm bệnh nhân..."
                onkeyup="searchPatients()"
                style="margin-bottom:10px;padding:8px;width:300px"
            />

            <table class="table">

                <thead>

                    <tr>

                        <th>ID</th>

                        <th>Họ tên</th>

                        <th>SĐT</th>

                        <th>Email</th>

                        <th>Giới tính</th>

                        <th></th>

                    </tr>

                </thead>

                <tbody>

                    ${
                        patientList.length

                        ? patientList.map(p => `

                            <tr>

                                <td>
                                    ${p.id}
                                </td>

                                <td>
                                    ${p.fullName}
                                </td>

                                <td>
                                    ${p.phone || ""}
                                </td>

                                <td>
                                    ${p.email || ""}
                                </td>

                                <td>
                                    ${p.gender || ""}
                                </td>

                                <td
                                    style="
                                        display:flex;
                                        gap:8px;
                                    "
                                >

                                    <button
                                        class="
                                            btn
                                            btn-primary
                                        "

                                        onclick="
                                            openEditModal(
                                                ${p.id}
                                            )
                                        "
                                    >

                                        Sửa

                                    </button>
                                </td>

                            </tr>

                        `).join("")

                        : `

                            <tr>

                                <td
                                    colspan="6"

                                    class="
                                        text-center
                                        text-muted
                                    "

                                    style="
                                        padding:24px;
                                    "
                                >

                                    Chưa có bệnh nhân

                                </td>

                            </tr>

                        `
                    }

                </tbody>

            </table>

        `;

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}

/* =========================
   CREATE
========================= */

function openCreateModal() {

    isEditMode = false;

    editingPatientId = null;

    $("#modalTitle").innerText =
        "Thêm bệnh nhân";

    $("#password").parentElement
        .style.display = "block";

    $("#username").disabled = false;

    clearForm();

    $("#patientModal").style.display =
        "flex";
}

/* =========================
   EDIT
========================= */

function openEditModal(id) {


    const patient =
        patientList.find(
            p => p.id === id
        );

    if (!patient) {
        return;
    }

    isEditMode = true;

    editingPatientId = id;

    $("#modalTitle").innerText =
        "Cập nhật bệnh nhân";

    // edit không đổi password
    $("#password").parentElement
        .style.display = "none";

    $("#username").disabled = true;

    $("#username").value =
        patient.username || "";

    $("#email").value =
        patient.email || "";

    $("#fullName").value =
        patient.fullName || "";

    $("#phone").value =
        patient.phone || "";

    $("#gender").value =
        patient.gender || "MALE";

    $("#dob").value =
        patient.dateOfBirth || "";

    $("#patientModal").style.display =
        "flex";
}

/* =========================
   SAVE
========================= */

async function handleSavePatient() {

    try {

        const data = {

            username:
                $("#username").value,

            password:
                $("#password").value,

            email:
                $("#email").value,

            fullName:
                $("#fullName").value,

            phone:
                $("#phone").value,

            gender:
                $("#gender").value,

            dateOfBirth:
                $("#dob").value,

        };
        /* =========================
           VALIDATE (CHẶN FE)
        ========================= */

        if (
            !data.username ||
            (!isEditMode && !data.password) || // create mới cần password
            !data.email ||
            !data.fullName ||
            !data.phone ||
            !data.gender ||
            !data.dateOfBirth
        ) {
            toast("Vui lòng nhập đầy đủ thông tin", "error");
            return;
        }

        if (isEditMode) {

            await updatePatient(
                editingPatientId,
                data
            );

            toast(
                "Cập nhật bệnh nhân thành công"
            );

        } else {

            await createPatient(
                data
            );

            toast(
                "Thêm bệnh nhân thành công"
            );
        }

        closeModal();

        renderPatients();

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}

/* =========================
   CLOSE
========================= */

function closeModal() {

    $("#patientModal").style.display =
        "none";

    clearForm();
}

/* =========================
   CLEAR
========================= */

function clearForm() {

    $("#username").value = "";

    $("#password").value = "";

    $("#email").value = "";

    $("#fullName").value = "";

    $("#phone").value = "";

    $("#gender").value = "MALE";

    $("#dob").value = "";
}
function searchPatients() {

    const keyword =
        $("#searchPatient")
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
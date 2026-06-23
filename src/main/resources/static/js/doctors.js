let doctors = [];
let departments = [];

const q = () => $("#q");
const dept = () => $("#dept");

async function initDoctorsPage() {

    try {

        departments =
            await loadDepartments();

        doctors =
            await getDoctors();

        renderDepartments();

        bindEvents();

        renderDoctors();

    } catch (err) {

        console.error(err);

        toast(
            "Lỗi tải dữ liệu",
            "error"
        );
    }
}

function renderDepartments() {

    dept().innerHTML = `

        <option value="all">
            Tất cả khoa
        </option>

        ${
            departments.map(d => `

                <option value="${d.id}">
                    ${esc(d.name)}
                </option>

            `).join("")
        }
    `;
}

function bindEvents() {

    q().addEventListener(
        "input",
        renderDoctors
    );

    dept().addEventListener(
        "change",
        async () => {

            try {

                doctors =
                    await getDoctors(
                        dept().value
                    );

                renderDoctors();

            } catch (err) {

                console.error(err);

                toast(
                    "Lỗi tải bác sĩ",
                    "error"
                );
            }
        }
    );
}

function renderDoctors() {

    const keyword =
        q().value
            .toLowerCase();

    const list =
        doctors.filter(d =>

            d.fullName
                .toLowerCase()
                .includes(keyword)
        );

    $("#list").innerHTML =

        list.length

        ? list.map(d => `

            <div
                class="doctor-card"
                onclick="openDoctor(${d.id})"
            >

                <div class="doctor-image-wrap">

                    <img
                        class="doctor-image"
                        src="http://localhost:8080${d.imageUrl}"
                        alt="${esc(d.fullName)}"
                    >

                </div>

                <div class="info">

                    <h3>
                        ${esc(d.fullName)}
                    </h3>

                    <div class="doctor-department">

                        🩺 ${esc(d.departmentName)}

                    </div>

                    <div class="doctor-exp">

                        ${d.yearsOfExperience || 0}
                        năm kinh nghiệm

                    </div>

                </div>

            </div>

        `).join("")

        :

        `
        <p class="text-muted">
            Không tìm thấy bác sĩ phù hợp.
        </p>
        `;
}

function openDoctor(id) {

    const d =
        doctors.find(
            x => x.id === id
        );

    if (!d) return;

    $("#modalBody").innerHTML = `

        <div class="doctor-modal">

            <img
                class="doctor-modal-image"
                src="http://localhost:8080${d.imageUrl}"
                alt="${esc(d.fullName)}"
            >

            <h2>
                ${esc(d.fullName)}
            </h2>

            <p>

                <b>Chuyên khoa:</b>

                ${esc(d.departmentName)}

            </p>

            <p>

                <b>Giới tính:</b>

                ${d.gender === "MALE"
                    ? "Nam"
                    : "Nữ"}

            </p>

            <p>

                <b>Kinh nghiệm:</b>

                ${d.yearsOfExperience || 0}
                năm

            </p>

            <p>

                <b>Email:</b>

                ${esc(d.email || "")}

            </p>

            <p>

                <b>Số điện thoại:</b>

                ${esc(d.phoneNumber || "")}

            </p>

            <div class="actions">

                <button
                    class="btn btn-outline"
                    onclick="closeModal()"
                >

                    Đóng

                </button>

                <a
                    href="booking.html"
                    class="btn btn-primary"
                >

                    Đặt lịch khám

                </a>

            </div>

        </div>
    `;

    $("#modal")
        .classList.add("open");
}

function closeModal() {

    $("#modal")
        .classList.remove("open");
}

document.addEventListener(
    "DOMContentLoaded",
    initDoctorsPage
);
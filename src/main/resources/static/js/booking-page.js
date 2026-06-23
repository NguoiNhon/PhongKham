const SERVICE_FEE = 200000;

let departments = [];
let doctors = [];

const stepNames = [
    "Thông tin",
    "Sức khoẻ",
    "Chọn ngày",
    "Thanh toán"
];

let step = 0;

let st = {
    name: "",
    phone: "",
    email: "",
    healthInfo: "",
    department: "",
    doctor: "",
    date: "",
    assigned: null,
    paid: false
};

/* ===== Restore VNPay Response ===== */

const url = new URL(location.href);
const vnpResp =
    url.searchParams.get("vnp_ResponseCode");

if (vnpResp) {
    const saved =
        sessionStorage.getItem(
            "pending_booking"
        );

    if (saved) {
        const data = JSON.parse(saved);

        st = { ...st, ...data };

        step = 3;

        if (vnpResp === "00") {
            st.paid = true;

            setTimeout(() => {
                toast(
                    "VNPay xác nhận thanh toán thành công!"
                );
            }, 300);
        } else {
            setTimeout(() => {
                toast(
                    "Giao dịch VNPay thất bại",
                    "error"
                );
            }, 300);
        }

        sessionStorage.removeItem(
            "pending_booking"
        );
    }
}


async function loadAvailableDoctors() {

    console.log("department =", st.department);
    console.log("date =", st.date);

    if (!st.department || !st.date) {
        doctors = [];
        return;
    }

    doctors = await api(
        `/doctors/available?departmentId=${st.department}&date=${st.date}`
    );

    console.log("doctors =", doctors);
}

/* ===== Render Steps ===== */

function renderSteps() {
    $("#steps").innerHTML =
        stepNames.map((name, index) => `
            <li class="step ${
                index < step
                    ? "done"
                    : index === step
                    ? "active"
                    : ""
            }">
                <div class="num">
                    ${index < step ? "✓" : index + 1}
                </div>
                Bước ${index + 1}: ${name}
            </li>
        `).join("");
}

/* ===== Render Panel ===== */

function renderPanel() {
    const total = SERVICE_FEE;

    if (step === 0) {
        $("#panel").innerHTML = `
            <h3>Thông tin cá nhân</h3>

            <div class="row">
                <div>
                    <label>Họ và tên *</label>
                    <input id="f_name"
                           disabled
                           value="${esc(st.name)}" />
                </div>

                <div class="row row-2">
                    <div>
                        <label>SĐT *</label>
                        <input id="f_phone"
                               disabled
                               value="${esc(st.phone)}" />
                    </div>

                    <div>
                        <label>Email</label>
                        <input type="email"
                               id="f_email"
                               disabled
                               value="${esc(st.email)}" />
                    </div>
                </div>
            </div>
        `;
    }

    else if (step === 1) {
        $("#panel").innerHTML = `
            <h3>Tình trạng sức khoẻ</h3>

            <div class="mt-4">
                <label>Khoa muốn khám *</label>

                <select id="f_dept">
                    <option value="">
                        -- Chọn khoa --
                    </option>

                    ${departments.map(d => `
                        <option value="${d.id}"
                            ${
                                String(st.department) === String(d.id)
                                    ? "selected"
                                    : ""
                            }>
                            ${esc(d.name)}
                        </option>
                    `).join("")}
                </select>
            </div>

            <div class="mt-4">
                <label>Mô tả triệu chứng *</label>

                <textarea id="f_health"
                          placeholder="Ví dụ: đau đầu kéo dài 3 ngày, sốt, ho...">${esc(st.healthInfo)}</textarea>
            </div>
        `;
    }

    else if (step === 2) {

    $("#panel").innerHTML = `
        <h3>Chọn ngày và bác sĩ</h3>

        <div class="mt-4">
            <label>Ngày khám *</label>

            <input type="date"
                   id="f_date"
                   value="${esc(st.date)}"
                   min="${new Date().toISOString().split("T")[0]}" />
        </div>

        <div class="mt-4">
            <label>Bác sĩ *</label>

            <select id="f_doctor">
                <option value="">
                    -- Chọn bác sĩ --
                </option>

                ${doctors.map(d => `
                    <option value="${d.id}"
                        ${
                            String(st.doctor) === String(d.id)
                                ? "selected"
                                : ""
                        }>
                        ${esc(d.fullName)}
                    </option>
                `).join("")}
            </select>
        </div>
    `;
}

    else if (step === 3) {
        $("#panel").innerHTML = `
            <h3>Thanh toán qua VNPay</h3>

            <div class="summary">
                <div class="line">
                    <span>Tổng phí dịch vụ:</span>
                    <span>${fmt(total)}đ</span>
                </div>

                <div class="line total">
                    <span>Số tiền cần thanh toán (100%):</span>
                    <span>${fmt(total)}đ</span>
                </div>
            </div>

            <div class="notice mt-4">
                <b>⚠ Thanh toán 100% trước khi đặt lịch</b><br>
                Lịch khám chỉ được xác nhận sau khi
                cổng VNPay báo thanh toán thành công.
            </div>

            <div class="card mt-4">
                <h4 style="font-weight:600;margin-bottom:6px">
                    Tóm tắt
                </h4>

                <p>
                    Bệnh nhân:
                    ${esc(st.name)}
                    •
                    ${esc(st.phone)}
                </p>

                <p>
                    Ngày khám:
                    ${esc(st.date)}
                </p>
            </div>
        `;
    }

    /* Wire Inputs */

    if (step === 1) {
        $("#f_dept").onchange = e => {
            st.department = e.target.value;
            updateButtons();
        };

        $("#f_health").oninput = e => {
            st.healthInfo = e.target.value;
            updateButtons();
        };
    }

    if (step === 2) {

        const dateInput = $("#f_date");

        // Nếu đã chọn ngày từ trước thì load luôn
        if (st.date && doctors.length === 0) {

            loadAvailableDoctors()
                .then(() => {
                    renderPanel();
                });
        }

        dateInput.onchange = async e => {

            st.date = e.target.value;

            st.doctor = "";

            await loadAvailableDoctors();

            renderPanel();

            updateButtons();
        };

        const doctorSelect =
            $("#f_doctor");

        if (doctorSelect) {

            doctorSelect.onchange = e => {

                st.doctor =
                    e.target.value;

                updateButtons();
            };
        }
    }
}

/* ===== Validation ===== */

function canNext() {
    if (step === 0) {
        return st.name && st.phone;
    }

    if (step === 1) {
        return st.department &&
               st.healthInfo.trim();
    }

    if (step === 2) {
        return st.date &&
            st.doctor;
    }

    return true;
}

/* ===== Buttons ===== */

function updateButtons() {
    $("#backBtn").disabled = step === 0;

    const next = $("#nextBtn");

    next.disabled = !canNext();

    next.textContent =
        step < 3
            ? "Tiếp tục →"
            : "Xác nhận đặt lịch";
}

$("#backBtn").onclick = () => {
    if (step > 0) {
        step--;
        renderSteps();
        renderPanel();
        updateButtons();
    }
};

$("#nextBtn").onclick = async () => {
    if (!canNext()) return;

    if (step < 3) {
        step++;
        renderSteps();
        renderPanel();
        updateButtons();
        return;
    }

    try {
        const payload = {
            departmentId: Number(st.department),
            doctorId: Number(st.doctor),
            appointmentDate: st.date,
            description: st.healthInfo
        };

        const result =
            await createAppointment(payload);

        if (!result || !result.paymentUrl) {
            toast(
                "Không còn lịch trống, vui lòng chọn ngày khác",
                "error"
            );
            return;
        }

        toast("Đang chuyển sang VNPay...");

        document
            .getElementById("paymentLoading")
            .classList.remove("hidden");

        $("#nextBtn").disabled = true;
        $("#nextBtn").textContent =
            "Đang chờ thanh toán...";

        const popup = window.open(
            result.paymentUrl,
            "_blank"
        );

        const timer = setInterval(async () => {
            try {
                const appointment =
                    await getAppointmentById(
                        result.appointmentId
                    );

                if (
                    appointment.appointmentStatus ===
                    "CONFIRMED"
                ) {
                    clearInterval(timer);

                    if (
                        popup &&
                        !popup.closed
                    ) {
                        popup.close();
                    }

                    sessionStorage.setItem(
                        "booking_success",
                        "1"
                    );

                    window.location.href =
                        "patients.html";
                }

                if (
                    popup &&
                    popup.closed &&
                    appointment.appointmentStatus !==
                        "CONFIRMED"
                ) {
                    clearInterval(timer);

                    document
                        .getElementById(
                            "paymentLoading"
                        )
                        .classList.add("hidden");

                    $("#nextBtn").disabled = false;
                    $("#nextBtn").textContent =
                        "Xác nhận đặt lịch";

                    toast(
                        "Thanh toán chưa hoàn tất",
                        "error"
                    );
                }
            } catch (err) {
                console.error(err);
            }
        }, 2000);
    } catch (err) {
        alert(err.message);
    }
};

/* ===== Init ===== */

async function init() {
    try {
        departments = await loadDepartments();

        const patient =
            await getMyProfilePatient();

        st.name = patient.fullName || "";
        st.phone = patient.phone || "";

        const account =
            await getMyProfileAccount();

        st.email = account.email || "";

        renderSteps();
        renderPanel();
        updateButtons();
    } catch (err) {
        console.error(err);
        toast(err.message, "error");
    }
}
/* ===== Auth Check ===== */

document.addEventListener(
    "DOMContentLoaded",
    () => {

        if (!requireRole("PATIENT")) {
            return;
        }

        init();
    }
);
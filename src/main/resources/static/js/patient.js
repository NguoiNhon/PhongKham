let tab = "appts";

/* =========================
   CHANGE TAB
========================= */

function setTab(t) {

    tab = t;

    $("#tab1").classList.toggle(
        "active",
        t === "appts"
    );

    render();
}

/* =========================
   RENDER PAGE
========================= */

async function render() {

    if (tab !== "appts") {
        return;
    }

    try {

        const list =
            (await getMyAppointments())

            .filter(a => {

                const now =
                    new Date();

                const apptTime =
                    new Date(
                        `${a.appointmentDate}T${a.startTime}`
                    );

                return apptTime > now;
            })

            .filter(a =>
                a.appointmentStatus !==
                "CANCELLED"
            );

        $("#content").innerHTML = `

        <input
            type="text"
            id="searchAppointment"
            placeholder="Tìm lịch khám..."
            onkeyup="searchAppointments()"
            style="margin-bottom:10px;padding:8px;width:300px"
        />

        <table class="table">

            <thead>
                <tr>
                    <th>Ngày/giờ</th>
                    <th>Khoa</th>
                    <th>Trạng thái</th>
                    <th>Khám</th>
                    <th>Huỷ lịch</th>
                    <th>Hoá đơn</th>
                </tr>
            </thead>

            <tbody>

                ${
                    list.length

                    ? list.map(a => `
                        <tr>

                            <td>
                                ${a.appointmentDate}
                                ${a.startTime}
                            </td>

                            <td>
                                ${a.departmentName}
                            </td>

                            <td>
                                <span class="badge ${
                                    a.appointmentStatus === "CONFIRMED"
                                        ? "success"
                                        : a.appointmentStatus === "CANCELLED"
                                        ? "danger"
                                        : ""
                                }">

                                    ${a.appointmentStatus}

                                </span>
                            </td>

                            <td>
                                <span class="badge ${
                                    a.medicalStatus === "DONE"
                                        ? "success"
                                        : ""
                                }">

                                    ${a.medicalStatus}

                                </span>
                            </td>

                            <td>

                                ${
                                    a.appointmentStatus !== "CANCELLED"
                                    &&
                                    a.medicalStatus !== "DONE"

                                    ? `
                                    <button
                                        class="btn btn-destructive"
                                        style="
                                            padding:4px 10px;
                                            font-size:12px
                                        "
                                        onclick="
                                            cancelAppt(
                                                ${a.appointmentId}
                                            )
                                        "
                                    >
                                        Huỷ
                                    </button>
                                    `
                                    : ""
                                }

                            </td>

                            <td>

                                <button
                                    class="btn"
                                    style="
                                        padding:4px 10px;
                                        font-size:12px
                                    "
                                    onclick="
                                        viewInvoice(
                                            ${a.appointmentId}
                                        )
                                    "
                                >
                                    Xem hoá đơn
                                </button>

                            </td>

                        </tr>
                    `).join("")

                    : `
                    <tr>

                        <td colspan="6"
                            class="
                                text-center
                                text-muted
                            "
                            style="padding:24px">

                            Chưa có lịch khám.

                            <a href="booking.html"
                               class="text-primary">

                                Đặt lịch ngay
                            </a>

                        </td>

                    </tr>
                    `
                }

            </tbody>

        </table>
        `;

    } catch (err) {

        console.error(err);
    }
}

/* =========================
   CANCEL APPOINTMENT
========================= */

async function cancelAppt(id) {

    if (!confirm("Huỷ lịch khám này?")) {
        return;
    }

    try {

        const token =
            localStorage.getItem("token");

        const response =
            await fetch(
                `${API_BASE}/appointments/${id}/cancel`,
                {
                    method: "PUT",

                    headers: {
                        Authorization:
                            `Bearer ${token}`
                    }
                }
            );

        if (response.ok) {

            toast("Đã huỷ");

            render();

        } else {

            toast(
                "Huỷ thất bại",
                "error"
            );
        }

    } catch (err) {

        console.error(err);

        toast(
            "Lỗi server",
            "error"
        );
    }
}

/* =========================
   VIEW INVOICE
========================= */

async function viewInvoice(
    appointmentId
) {

    try {

        const inv =
            await getPaymentByAppointmentId(
                appointmentId
            );

        $("#invoiceBox").innerHTML = `
        <div class="card"
             style="
                padding:24px;
                border-radius:12px;
                margin-top:24px;
             ">

            <div style="
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:20px;
            ">

                <h3 style="margin:0">
                    Hoá đơn thanh toán
                </h3>

                <button
                    class="btn btn-outline"
                    onclick="closeInvoice()">

                    Đóng
                </button>

            </div>

            <table class="table">

                <tbody>

                    <tr>
                        <td><b>Mã hoá đơn</b></td>
                        <td>${inv.transactionRef}</td>
                    </tr>

                    <tr>
                        <td><b>Mã lịch khám</b></td>
                        <td>#${inv.appointmentId}</td>
                    </tr>

                    <tr>
                        <td><b>Số tiền</b></td>

                        <td>
                            ${Number(inv.amount)
                                .toLocaleString("vi-VN")}đ
                        </td>
                    </tr>

                    <tr>

                        <td><b>Trạng thái</b></td>

                        <td>
                            <span class="badge ${
                                inv.status === "SUCCESS"
                                    ? "success"
                                    : inv.status === "FAILED"
                                    ? "danger"
                                    : "warn"
                            }">

                                ${inv.status}

                            </span>
                        </td>

                    </tr>

                    <tr>
                        <td><b>Nội dung</b></td>
                        <td>${inv.orderInfo}</td>
                    </tr>

                    <tr>

                        <td><b>Ngày tạo</b></td>

                        <td>
                            ${new Date(inv.createdAt)
                                .toLocaleString("vi-VN")}
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>
        `;

        $("#invoiceBox")
            .scrollIntoView({
                behavior: "smooth"
            });

    } catch (err) {

        console.error(err);

        $("#invoiceBox").innerHTML = `
        <div class="card"
             style="
                padding:20px;
                margin-top:24px;
             ">

            <p class="text-danger">
                Không tìm thấy hoá đơn
            </p>

        </div>
        `;
    }
}

/* =========================
   CLOSE INVOICE
========================= */

function closeInvoice() {

    $("#invoiceBox").innerHTML = "";
}

/* =========================
   INIT
========================= */

document.addEventListener(
    "DOMContentLoaded",
    () => {

        if (!requireRole("PATIENT")) {
            return;
        }

        render();
    }
);
function searchAppointments() {

    const keyword =
        $("#searchAppointment")
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
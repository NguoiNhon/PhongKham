/* =========================
   ROLE CHECK
========================= */

document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const user =
            Auth.current();

        if (
            !user ||
            ![
                "DOCTOR",
                "RECEPTIONIST"
            ].includes(user.role)
        ) {

            location.href =
                "index.html";

            return;
        }

        render();
    }
);

/* =========================
   RENDER
========================= */

async function render() {

    try {

        const list =
            (await getDoctorAppointments())

            .filter(a =>
                a.appointmentStatus === "CONFIRMED"
            );

        $("#content").innerHTML = `
        <table class="table">

            <thead>

                <tr>

                    <th>Bệnh nhân</th>

                    <th>Ngày khám</th>

                    <th>Giờ</th>

                    <th>Trạng thái lịch</th>

                    <th>Tình trạng khám</th>

                </tr>

            </thead>

            <tbody>

                ${
                    list.length

                    ? list.map(a => `

                        <tr>

                            <td>
                                ${a.patientName}
                            </td>

                            <td>
                                ${a.appointmentDate}
                            </td>

                            <td>
                                ${a.startTime}
                            </td>

                            <td>

                                <span class="
                                    badge

                                    ${
                                        a.appointmentStatus ===
                                        "CONFIRMED"

                                        ? "success"

                                        : a.appointmentStatus ===
                                        "CANCELLED"

                                        ? "danger"

                                        : "warn"
                                    }
                                ">

                                    ${a.appointmentStatus}

                                </span>

                            </td>

                            <td>

                                <span class="
                                    badge

                                    ${
                                        a.medicalStatus === "DONE"
                                        ? "success"

                                        : a.medicalStatus ===
                                        "IN_PROGRESS"

                                        ? "warn"

                                        : ""
                                    }
                                ">

                                    ${a.medicalStatus}

                                </span>

                            </td>

                        </tr>

                    `).join("")

                    : `
                    <tr>

                        <td
                            colspan="5"

                            class="
                                text-center
                                text-muted
                            "

                            style="padding:24px"
                        >

                            Chưa có lịch khám

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
            err.message || "Lỗi load lịch khám",
            "error"
        );
    }
}
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

        renderAppointments();
    }
);

async function renderAppointments() {

    try {

        const list =
            await getAllAppointments();

            
        const today =
        new Date()
            .toISOString()
            .split("T")[0];
        $("#content").innerHTML = `

        <input
            type="text"
            id="searchInput"
            placeholder="Tìm bệnh nhân, bác sĩ..."
            onkeyup="searchAppointments()"
            style="margin-bottom:10px;padding:8px;width:300px"
        />
        <table class="table">

            <thead>

                <tr>

                    <th>ID</th>

                    <th>Bệnh nhân</th>

                    <th>Bác sĩ</th>

                    <th>Khoa</th>

                    <th>Ngày khám</th>

                    <th>Giờ</th>

                    <th>Trạng thái lịch</th>

                    <th>Tình trạng khám</th>

                </tr>

            </thead>

            <tbody>

                ${
                    list.length

                    ? list.map(a => {

                        const canUpdate =
                            a.appointmentStatus === "CONFIRMED"
                            &&
                            a.appointmentDate === today;

                        return `

                        <tr>

                            <td>
                                ${a.appointmentId}
                            </td>

                            <td>
                                ${a.patientName}
                            </td>

                            <td>
                                ${a.doctorName}
                            </td>

                            <td>
                                ${a.departmentName}
                            </td>

                            <td>
                                ${a.appointmentDate}
                            </td>

                            <td>
                                ${a.startTime}
                                -
                                ${a.endTime}
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

                                <select

                                        ${!canUpdate ? "disabled" : ""}

                                        onchange="
                                            handleUpdateMedicalStatus(
                                                ${a.appointmentId},
                                                this.value
                                            )
                                        "
                                    >

                                    <option
                                        value="NOT_EXAMINED"
                                        ${
                                            a.medicalStatus === "NOT_EXAMINED"
                                            ? "selected"
                                            : ""
                                        }
                                    >
                                        Chưa khám
                                    </option>

                                    <option
                                        value="IN_PROGRESS"
                                        ${
                                            a.medicalStatus === "IN_PROGRESS"
                                            ? "selected"
                                            : ""
                                        }
                                    >
                                        Đang khám
                                    </option>

                                    <option
                                        value="DONE"
                                        ${
                                            a.medicalStatus === "DONE"
                                            ? "selected"
                                            : ""
                                        }
                                    >
                                        Đã khám xong
                                    </option>

                                </select>

                            </td>

                        </tr>

                    `;
                    }).join("")

                    : `

                    <tr>

                        <td
                            colspan="8"

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
            err.message ||
            "Lỗi tải lịch khám",
            "error"
        );
    }
}
async function handleUpdateMedicalStatus(
    id,
    medicalStatus
) {

    try {

        await updateMedicalStatusApi(
            id,
            medicalStatus
        );

        toast(
            "Cập nhật tình trạng khám thành công"
        );
        await renderAppointments();

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}
function searchAppointments() {

    const keyword =
        document
            .getElementById("searchInput")
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
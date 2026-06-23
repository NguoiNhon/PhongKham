document.addEventListener(
    "DOMContentLoaded",
    async () => {

        const ok =
            await requireRole("ADMIN");

        if (!ok) {
            return;
        }

        renderAccounts();
    }
);

async function renderAccounts() {

    try {

        const list =
            await getAccounts();

        $("#content").innerHTML = `

        <input
            type="text"
            id="searchAccount"
            placeholder="Tìm tài khoản..."
            onkeyup="searchAccounts()"
            style="margin-bottom:10px;padding:8px;width:300px"
        />

        <table class="table">

            <thead>

                <tr>

                    <th>ID</th>

                    <th>Username</th>

                    <th>Email</th>

                    <th>Role</th>

                    <th>Status</th>

                    <th>Thao tác</th>

                </tr>

            </thead>

            <tbody>

                ${list.map(a => `

                    <tr>

                        <td>
                            ${a.id}
                        </td>

                        <td>
                            ${a.username}
                        </td>

                        <td>
                            ${a.email}
                        </td>

                        <td>
                            ${a.role}
                        </td>

                        <td>

                            <span class="
                                badge

                                ${
                                    a.status === "ACTIVE"
                                    ? "success"

                                    : a.status === "INACTIVE"
                                    ? "warn"

                                    : "danger"
                                }
                            ">

                                ${a.status}

                            </span>

                        </td>

                        <td>

                            ${
                                a.status === "ACTIVE"

                                ? `
                                    <button
                                        class="
                                            btn
                                            btn-destructive
                                        "

                                        onclick="
                                            handleDeactivate(
                                                ${a.id}
                                            )
                                        "
                                    >
                                        Khóa
                                    </button>
                                `

                                : `
                                    <button
                                        class="
                                            btn
                                            btn-primary
                                        "

                                        onclick="
                                            handleActivate(
                                                ${a.id}
                                            )
                                        "
                                    >
                                        Mở khóa
                                    </button>
                                `
                            }

                        </td>

                    </tr>

                `).join("")}

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
async function handleDeactivate(id) {

    const confirmDelete =
        confirm(
            "Khóa tài khoản này?"
        );

    if (!confirmDelete) {
        return;
    }

    try {

        await deactivateAccount(id);

        toast(
            "Khóa tài khoản thành công"
        );

        renderAccounts();

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}
async function handleActivate(id) {

    try {

        await activateAccount(id);

        toast(
            "Đã mở khóa tài khoản"
        );

        renderAccounts();

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}
function searchAccounts() {

    const keyword =
        $("#searchAccount")
            .value
            .toLowerCase();

    const rows =
        document.querySelectorAll("tbody tr");

    rows.forEach(row => {

        const text =
            row.innerText
                .toLowerCase();

        row.style.display =
            text.includes(keyword)
                ? ""
                : "none";
    });
}
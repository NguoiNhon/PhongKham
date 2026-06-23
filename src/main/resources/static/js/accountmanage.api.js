const ACCOUNT_API =
    "http://localhost:8080/api/admin/accounts";

function authHeader() {

    return {
        "Content-Type": "application/json; charset=UTF-8",

        "Authorization":
            "Bearer " +
            localStorage.getItem("token")
    };
}

async function getAccounts() {

    const res = await fetch(
        ACCOUNT_API,
        {
            headers: authHeader()
        }
    );

    if (!res.ok) {

        throw new Error(
            "Load accounts failed"
        );
    }

    return res.json();
}
async function deactivateAccount(id) {

    const res = await fetch(
        `${ACCOUNT_API}/${id}`,
        {
            method: "DELETE",

            headers: authHeader()
        }
    );

    if (!res.ok) {

        throw new Error(
            "Khóa tài khoản thất bại"
        );
    }

    return res.text();
}
async function activateAccount(id) {

    const res = await fetch(
        `${ACCOUNT_API}/${id}/activate`,
        {
            method: "PUT",

            headers: authHeader()
        }
    );

    if (!res.ok) {

        throw new Error(
            "Mở khóa thất bại"
        );
    }

    return res.text();
}
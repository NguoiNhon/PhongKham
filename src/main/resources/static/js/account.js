async function getMyProfileAccount() {

    const token =
        localStorage.getItem("token");
    const response = await fetch(
        `${API_BASE}/accounts/me`,
        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!response.ok) {

        throw new Error(
            "Không lấy được thông tin tài khoản"
        );
    }

    return await response.json();
}
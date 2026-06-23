

let mode = "login";

/* =========================
   CHANGE MODE
========================= */

function setMode(m) {

    mode = m;

    $("#title").textContent =
        m === "login"
            ? "Đăng nhập"
            : "Đăng ký";

    $("#submitBtn").textContent =
        m === "login"
            ? "Đăng nhập"
            : "Đăng ký";

    $("#nameWrap").style.display =
        m === "register"
            ? "block"
            : "none";

    $("#emailWrap").style.display =
        m === "register"
            ? "block"
            : "none";

    $("#dobWrap").style.display =
        m === "register"
            ? "block"
            : "none";

    $("#genderWrap").style.display =
        m === "register"
            ? "block"
            : "none";

    $("#phoneWrap").style.display =
        m === "register"
            ? "block"
            : "none";

    $("#link1").style.display =
        m === "login"
            ? "block"
            : "none";

    $("#link2").style.display =
        m === "register"
            ? "block"
            : "none";
}

/* =========================
   SUBMIT FORM
========================= */

async function submitForm(e) {

    e.preventDefault();

    try {

        if (mode === "login") {

            const username =
                $("#username").value;

            const password =
                $("#password").value;

            const data =
                await login(username, password);

            localStorage.setItem(
                "token",
                data.token
            );

            // lấy user mới nhất từ backend
            await Auth.fetchMe();

            alert("Đăng nhập thành công");

            window.location.href =
                "index.html";

        } else {

            const payload = {

                username:
                    $("#username").value,

                email:
                    $("#email").value,

                password:
                    $("#password").value,

                fullName:
                    $("#name").value,

                dateOfBirth:
                    $("#dateOfBirth").value,

                gender:
                    $("#gender").value,

                phone:
                    $("#phone").value
            };

            const data =
                await register(payload);

            alert(
                "Đăng ký thành công. Vui lòng kiểm tra email OTP."
            );

            $("#form").style.display =
                "none";

            $(".lg-switch").style.display =
                "none";

            $("#otpSection").style.display =
                "block";

            $("#verifyEmail").value =
                $("#email").value;

            console.log(data);
        }

    } catch (err) {

        alert(err.message);
    }

    return false;
}

/* =========================
   VERIFY EMAIL OTP
========================= */

async function verifyEmailOtp() {

    try {

        const email =
            $("#verifyEmail").value;

        const otp =
            $("#verifyOtp").value;

        const result =
            await verifyEmail(email, otp);

        alert(result);

        setMode("login");

        $("#form").style.display =
            "block";

        $(".lg-switch").style.display =
            "block";

        $("#otpSection").style.display =
            "none";

        $("#verifyOtp").value = "";

    } catch (err) {

        alert(err.message);
    }
}

/* =========================
   RESEND OTP
========================= */

async function resendOtp() {

    try {

        const email =
            $("#verifyEmail").value;

        const result =
            await resendVerifyOtp(email);

        alert(result);

    } catch (err) {

        alert(err.message);
    }
}

/* =========================
   LOGIN API
========================= */

async function login(username, password) {

    const response = await fetch(
        `${API_BASE}/auth/login`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },

            body: JSON.stringify({
                username,
                password
            })
        }
    );

    if (!response.ok) {
        throw new Error(await response.text());
    }

    return response.json();
}

/* =========================
   REGISTER API
========================= */

async function register(data) {

    const response = await fetch(
        `${API_BASE}/accounts/register`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },

            body: JSON.stringify(data)
        }
    );

    if (!response.ok) {
        throw new Error(await response.text());
    }

    return response.json();
}

/* =========================
   FORGOT PASSWORD
========================= */

async function forgotPassword(email) {

    const response = await fetch(
        `${API_BASE}/auth/forgot-password`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },

            body: JSON.stringify({
                email
            })
        }
    );

    const text = await response.text();

    if (!response.ok) {
        throw new Error(text);
    }

    return text;
}

/* =========================
   RESET PASSWORD
========================= */

async function resetPassword(
    email,
    otp,
    newPassword
) {

    const response = await fetch(
        `${API_BASE}/auth/reset-password`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },

            body: JSON.stringify({
                email,
                otp,
                newPassword
            })
        }
    );

    const text = await response.text();

    if (!response.ok) {
        throw new Error(text);
    }

    return text;
}

/* =========================
   VERIFY EMAIL
========================= */

async function verifyEmail(email, otp) {

    const response = await fetch(
        `${API_BASE}/accounts/verify-email`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },

            body: JSON.stringify({
                email,
                otp
            })
        }
    );

    const text = await response.text();

    if (!response.ok) {
        throw new Error(text);
    }

    return text;
}

/* =========================
   RESEND VERIFY OTP
========================= */

async function resendVerifyOtp(email) {

    const response = await fetch(
        `${API_BASE}/accounts/resend-verify-otp`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },

            body: JSON.stringify({
                email
            })
        }
    );

    const text = await response.text();

    if (!response.ok) {
        throw new Error(text);
    }

    return text;
}
/* =========================
   FORGOT PASSWORD PAGE
========================= */

async function sendOtp() {

    const email =
        document.getElementById("email").value;

    try {

        const result =
            await forgotPassword(email);

        alert(result);

        // Ẩn form email
        document.getElementById(
            "emailSection"
        ).style.display = "none";

        // Hiện form reset password
        document.getElementById(
            "resetSection"
        ).style.display = "block";

    } catch (err) {

        alert(err.message);
    }
}

async function handleResetPassword() {

    const email =
        document.getElementById("email").value;

    const otp =
        document.getElementById("otp").value;

    const newPassword =
        document.getElementById("newPassword").value;

    try {

        const result =
            await resetPassword(
                email,
                otp,
                newPassword
            );

        alert(result);

        window.location.href =
            "auth.html";

    } catch (err) {

        alert(err.message);
    }
}
function requireRole(role) {

    const raw =
        localStorage.getItem("currentUser");

    if (!raw) {

        location.href = "auth.html";
        return false;
    }

    const user = JSON.parse(raw);

    if (user.role !== role) {

        alert("Bạn không có quyền truy cập");

        location.href = "index.html";

        return false;
    }

    return true;
}
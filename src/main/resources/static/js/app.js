const API_BASE = "http://localhost:8080/api";

/* ===== Helpers ===== */

const $ = (s, p = document) =>
    p.querySelector(s);

const $$ = (s, p = document) =>
    [...p.querySelectorAll(s)];

const fmt = (n) =>
    Number(n).toLocaleString("vi-VN");

const esc = (s) =>
    String(s ?? "").replace(
        /[&<>"']/g,
        c => ({
            "&":"&amp;",
            "<":"&lt;",
            ">":"&gt;",
            '"':"&quot;",
            "'":"&#39;"
        }[c])
    );

function toast(msg, type = "success") {

    let st = $(".toast-stack");

    if (!st) {

        st = document.createElement("div");

        st.className = "toast-stack";

        document.body.appendChild(st);
    }

    const t = document.createElement("div");

    t.className = `toast ${type}`;

    t.textContent = msg;

    st.appendChild(t);

    setTimeout(() => t.remove(), 3500);
}

/* ===== API ===== */

async function api(url, options = {}) {

    const token =
        localStorage.getItem("token");

    const headers = {
        ...(options.headers || {})
    };

    // 🔥 chỉ set JSON nếu KHÔNG phải FormData
    if (!(options.body instanceof FormData)) {

        headers["Content-Type"] =
            "application/json; charset=UTF-8";
    }

    if (token) {
        headers.Authorization =
            `Bearer ${token}`;
    }

    const response = await fetch(
        `${API_BASE}${url}`,
        {
            ...options,
            headers
        }
    );

    const contentType =
        response.headers.get("content-type");

    let data;

    if (
        contentType &&
        contentType.includes("application/json")
    ) {

        data = await response.json();

    } else {

        data = await response.text();
    }

    if (!response.ok) {

        console.error("API ERROR:", data);

        throw new Error(
            data.message ||
            data.error ||
            data.details ||
            data ||
            "API Error"
        );
    }

    return data;
    }

/* ===== Auth ===== */

const Auth = {

    token() {

        return localStorage.getItem("token");
    },

    isLoggedIn() {

        return !!this.token();
    },

    logout() {

        localStorage.removeItem("token");

        localStorage.removeItem("currentUser");

        location.href = "auth.html";
    },

    current() {

        const raw =
            localStorage.getItem("currentUser");

        return raw
            ? JSON.parse(raw)
            : null;
    },

    hasRole(...roles) {

        const u = this.current();

        return !!u && roles.includes(u.role);
    },

    async fetchMe() {

        const data =
            await api("/accounts/me");

        localStorage.setItem(
            "currentUser",
            JSON.stringify(data)
        );

        return data;
    }
};


/* ===== Appointments API ===== */

async function cancelAppointment(id) {

    return await api(
        `/appointments/${id}/cancel`,
        {
            method: "PUT"
        }
    );
}

/* ===== Header / Footer ===== */

function renderHeader(active = "") {

    const u = Auth.current();

    // menu public
    const navItems = [

        ["index.html", "Trang chủ"],
        ["about.html", "Giới thiệu"],
        ["organization.html", "Cơ cấu tổ chức"],
        ["doctors.html", "Đội ngũ bác sĩ"]
    ];

    // menu PATIENT
    if (u?.role === "PATIENT") {

        navItems.push(
            ["booking.html", "Đặt lịch khám"],
            ["patients.html", "Dành cho bệnh nhân"]
        );
    }

    // menu DOCTOR
    if (u?.role === "DOCTOR") {

        navItems.push(
            ["doctor.html", "Xem lịch khám"]
        );
    }

    // menu ADMIN
    if (u?.role === "ADMIN") {

        navItems.push(
            ["departmentmanage.html", "Quản lý chuyên khoa"],
            ["accountmanage.html", "Quản lý tài khoản"],
            ["doctormanage.html", "Quản lý bác sĩ"],
            ["appointmentmanage.html", "Quản lý lịch khám"],
            ["receptionist.html", "Dashboard thống kê"],
            ["patientmanage.html", "Quản lý bệnh nhân"]
        );
    }

     if (u?.role === "RECEPTIONIST") {

        navItems.push(

            ["receptionist.html", "Dashboard thống kê"],

            ["appointmentmanage.html", "Quản lý lịch khám"],

            ["patientmanage.html", "Quản lý bệnh nhân"]
        );
    }

    const nav = navItems.map(([h, l]) => `

        <a href="${h}"
           class="${active === h ? 'active' : ''}">
            ${l}
        </a>

    `).join("");

    const userArea = u
        ? `
            <div class="header-user-area">

                <div class="notification-wrapper">

                    <button id="notification-btn">
                        🔔
                        <span id="notification-badge"
                            class="hidden">
                        </span>
                    </button>

                    <div id="notification-dropdown"
                        class="hidden">

                        <div class="notification-title">
                            Thông báo
                        </div>

                        <div id="notification-list">
                            <div class="notification-empty">
                                Chưa có thông báo
                            </div>
                        </div>

                    </div>

                </div>

                <span style="margin-right:8px">
                    Xin chào, ${esc(u.username)}
                </span>

                <a href="#"
                onclick="Auth.logout();return false;">
                    Đăng xuất
                </a>

            </div>
        `
        : `
            <a href="auth.html">
                Đăng nhập / Đăng ký
            </a>
        `;

    document.body.insertAdjacentHTML(
        "afterbegin",
        `

        <header>

            <div class="topbar">

                <div class="topbar-inner">

                    <span>
                        📞 Tổng đài:
                        <b>0243.858.7155</b>
                    </span>

                    <div>
                        ${userArea}
                    </div>

                </div>

            </div>

            <div class="brand-row">

                <div class="brand-inner">

                    <a href="index.html"
                       class="brand">

                        <div>

                            <div class="t1">
                                TRUNG TÂM Y TẾ
                                QUẬN THANH XUÂN
                            </div>

                            <div class="t2">
                                Tận tâm – Chất lượng
                            </div>

                        </div>

                    </a>

                </div>

            </div>

            <nav class="mainnav">

                <div class="mainnav-inner">
                    ${nav}
                </div>

            </nav>

        </header>

        `
    );
}

function renderFooter() {

    document.body.insertAdjacentHTML(
        "beforeend",
        `
        <footer>

            <div class="copyright">
                © 2026 Trung tâm Y tế Quận Thanh Xuân
            </div>

        </footer>
        `
    );
}

/* UI chatbot */

document.body.insertAdjacentHTML(
    "beforeend",
    `
    <!-- Nút mở chatbot -->
    <button id="chatbot-toggle">
        🩺
    </button>

    <!-- Khung chatbot -->
    <div id="chatbot-container" class="hidden">

        <div id="chatbot-header">

            <span>🩺 Chat hỗ trợ</span>

            <button id="chatbot-close">
                ✖
            </button>

        </div>

        <div id="chatbot-messages"></div>

        <div id="chatbot-input-area">

            <input
                id="chatbot-input"
                type="text"
                placeholder="Nhập câu hỏi..."
            >

            <button id="chatbot-send">
                Gửi
            </button>

        </div>

    </div>
    `
);

/* Elements */

const chatbotContainer =
    document.getElementById(
        "chatbot-container"
    );

const toggleBtn =
    document.getElementById(
        "chatbot-toggle"
    );

const closeBtn =
    document.getElementById(
        "chatbot-close"
    );

const input =
    document.getElementById(
        "chatbot-input"
    );

const sendBtn =
    document.getElementById(
        "chatbot-send"
    );

/* Toggle */

toggleBtn.addEventListener(
    "click",
    () => {

        chatbotContainer.classList.toggle(
            "hidden"
        );
    }
);

closeBtn.addEventListener(
    "click",
    () => {

        chatbotContainer.classList.add(
            "hidden"
        );
    }
);

/* Send message */

sendBtn.addEventListener(
    "click",
    sendMessage
);

input.addEventListener(
    "keypress",
    e => {

        if (e.key === "Enter") {
            sendMessage();
        }
    }
);

async function sendMessage() {

    const text = input.value.trim();

    if (!text) return;

    addMessage(text, "user");

    input.value = "";

    try {

        const data = await api(
            "/chat",
            {
                method: "POST",

                body: JSON.stringify({
                    message: text
                })
            }
        );

        console.log(data);

        addMessage(
            data.reply || "Không có phản hồi",
            "bot"
        );

    } catch (err) {

        console.error(err);

        addMessage(
            "Lỗi gọi AI",
            "bot"
        );
    }
}


function addMessage(text, type) {

    const box =
        document.getElementById(
            "chatbot-messages"
        );

    const div =
        document.createElement("div");

    div.className =
        `chat-message ${type}`;

    // render HTML
    div.innerHTML = text;

    box.appendChild(div);

    box.scrollTop =
        box.scrollHeight;
}
/* ===== Notifications ===== */

async function loadNotifications() {

    if (!Auth.isLoggedIn()) {
        return;
    }

    try {

        const notifications =
            await api("/notifications/me");

        renderNotifications(
            notifications
        );

    } catch (err) {

        console.error(err);
    }
}

function renderNotifications(
    notifications
) {

    const list =
        $("#notification-list");

    const badge =
        $("#notification-badge");

    if (!list || !badge) {
        return;
    }

    if (!notifications.length) {

        list.innerHTML = `
            <div class="notification-empty">
                Chưa có thông báo
            </div>
        `;

        badge.classList.add(
            "hidden"
        );

        return;
    }

    const unreadCount =
        notifications.filter(
            n => !n.read
        ).length;

    if (unreadCount > 0) {

        badge.classList.remove(
            "hidden"
        );

        badge.textContent =
            unreadCount;

    } else {

        badge.classList.add(
            "hidden"
        );
    }

    list.innerHTML =
        notifications.map(n => `

            <div class="
                notification-item
                ${!n.read ? "unread" : ""}
            ">

                <div class="
                    notification-message
                ">
                    ${esc(n.message)}
                </div>

                <div class="
                    notification-time
                ">
                    ${
                        new Date(
                            n.createdAt
                        ).toLocaleString(
                            "vi-VN"
                        )
                    }
                </div>

            </div>

        `).join("");
}
document.addEventListener(
    "click",
    e => {

        const dropdown =
            $("#notification-dropdown");

        const btn =
            $("#notification-btn");

        if (!dropdown || !btn) {
            return;
        }

        if (
            btn.contains(e.target)
        ) {

            dropdown.classList.toggle(
                "hidden"
            );

        } else {

            dropdown.classList.add(
                "hidden"
            );
        }
    }
);
document.addEventListener(
    "DOMContentLoaded",
    () => {

        loadNotifications();

        // auto refresh 30s
        setInterval(
            loadNotifications,
            30000
        );
    }
);
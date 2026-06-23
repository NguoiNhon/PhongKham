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

        loadDashboard();
    }
);

async function loadDashboard() {

    try {

        const appointments =
            await api(
                "/appointments"
            );

        $("#totalAppointments")
            .innerText =
            appointments.length;

        $("#pendingAppointments")
            .innerText =
            appointments.filter(
                a =>
                    a.medicalStatus ===
                    "NOT_EXAMINED"
            ).length;

        $("#checkedInAppointments")
            .innerText =
            appointments.filter(
                a =>
                    a.medicalStatus ===
                    "IN_PROGRESS"
            ).length;

        $("#completedAppointments")
            .innerText =
            appointments.filter(
                a =>
                    a.medicalStatus ===
                    "DONE"
            ).length;

    } catch (err) {

        console.error(err);

        toast(
            err.message,
            "error"
        );
    }
}
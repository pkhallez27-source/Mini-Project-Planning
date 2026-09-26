window.addEventListener("DOMContentLoaded", function () {
    const btnLogout = document.querySelector("#btn-logout");
    btnLogout.addEventListener("click", function () {
        const confirm = window.confirm("로그아웃 하시겠습니까?");
        if (confirm) {
            location.href = "/admin/logout";
        }
    });
});
$(document).ready(function() {
    document.getElementById("userNameInput1").value = window.sessionStorage.getItem("username");
    document.getElementById("userNameInput2").value = window.sessionStorage.getItem("username");
    document.getElementById(
        "dummyUsernameForm5"
    ).value = window.sessionStorage.getItem("username");
    document.getElementById(
        "dummyUsernameForm4"
    ).value = window.sessionStorage.getItem("username");
});

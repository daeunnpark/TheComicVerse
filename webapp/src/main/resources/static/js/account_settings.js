$(document).ready(function() {
    console.log(window.localStorage.getItem("username"));
    document.getElementById("usernameField").value=window.localStorage.getItem("username");
});
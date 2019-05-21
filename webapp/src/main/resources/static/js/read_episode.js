$(document).ready(function () {

  console.log(window.sessionStorage.getItem("username"))

  document.getElementById(
      "dummyUsernameForm1"
  ).value = window.sessionStorage.getItem("username");
  document.getElementById(
      "dummyUsernameForm2"
  ).value = window.sessionStorage.getItem("username");
    document.getElementById(
        "dummyUsernameForm3"
    ).value = window.sessionStorage.getItem("username");
    document.getElementById(
        "dummyUsernameForm5"
    ).value = window.sessionStorage.getItem("username");
    document.getElementById(
        "dummyUsernameForm8"
    ).value = window.sessionStorage.getItem("username");

    document.getElementById("userNameInput1").value = window.sessionStorage.getItem("username");
    document.getElementById("userNameInput2").value = window.sessionStorage.getItem("username");
    document.getElementById("userNameInput3").value = window.sessionStorage.getItem("username");
    document.getElementById("userNameInput4").value = window.sessionStorage.getItem("username");
});

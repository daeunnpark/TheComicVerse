var currentUsername;
function updateUsername(newUsername) {
  currentUsername = newUsername;
  login(currentUsername);
}

function login(currentUsername) {
  window.localStorage.setItem("username", currentUsername);
}

function getUsername() {
  console.log(window.localStorage.getItem("username"));
  return window.localStorage.getItem("username");
}

function checkUsername() {
  document.getElementById("temp0").innerHTML = getUsername();
}
/*
function hideLogin(){
    $("#loginBody").collapse;
}
$("#submitLogin").submit(function(e) {
    login($("#username").value);
    hideLogin();
*/
/*
    var value = document.getElementById("secret_word").value;
    if (is_valid(value)) {
        computerWord = value;
        set_secret_word_UI(value);
        $("#secretWordModal").modal("hide");
    }
    return false;
    */
//});

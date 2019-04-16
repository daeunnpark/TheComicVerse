var currentUsername;
function loginSuccessful(newUsername) {
  currentUsername = newUsername;
  login(currentUsername);
  hideLogin();
}

function login(currentUsername) {
  window.localStorage.setItem("username", currentUsername);
}

function getUsername() {
  return window.localStorage.getItem("username");
}

function hideLogin() {
  $("#loginSection").hide();
}

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

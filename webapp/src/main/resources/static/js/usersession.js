var currentUsername = null;
function loginSuccessful(newUsername) {
  currentUsername = newUsername;
  login(currentUsername);

  //hideLogin();
}

function login(currentUsername) {
  window.sessionStorage.setItem("username", currentUsername);
}

function getUsername() {
  return sessionStorage.getItem("username");
}

function hideLogin() {
  //sessionStorage.removeItem("username");
  //$("#loginSection").hide();
  //$("#loggedInSection").hide();
}
/*

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

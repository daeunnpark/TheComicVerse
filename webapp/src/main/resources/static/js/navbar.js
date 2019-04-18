// Signout
$("#signoutBtn").click(function() {
  window.sessionStorage.removeItem("username");
  updateLoginUI();
});

// Check username and Update LoginUI
function updateLoginUI() {
  console.log(window.sessionStorage.getItem("username"));
  if (window.sessionStorage.getItem("username") == null) {
    $("#loginSection").show();
    $("#loggedInSection").hide();
  } else {
    $("#loginSection").hide();
    $("#loggedInSection").show();
  }
}

$("#signoutBtn").click(function() {
  window.sessionStorage.removeItem("username");
  updateLoginUI();
});

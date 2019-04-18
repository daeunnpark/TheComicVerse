jQuery(document).ready(function() {
  $("#userNotLoggedInModal").hide();
  console.log(window.sessionStorage.getItem("username"));

  if (window.sessionStorage.getItem("username") == null) {
    $("#userNotLoggedInModal").show();
  }
});

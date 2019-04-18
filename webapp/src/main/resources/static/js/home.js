// Search dropdown box
$(".dropdown-menu a").click(function() {
  // Update dropdown UI
  $(this)
    .parents(".input-group-prepend")
    .find(".btn")
    .html($(this).text());

  // Update drop box value
  $(this)
    .parents(".input-group-prepend")
    .find(".btn")
    .val($(this).data("value"));

  // Update searchOption
  $("#searchOption").val($(this).data("value"));
});

jQuery(document).ready(function() {
  console.log("this");
  console.log(window.sessionStorage.getItem("username"));

  if (window.sessionStorage.getItem("username") == null) {
    console.log("here");
    $("#loginSection").show();
    $("#loggedInSection").hide();

    //document.getElementById("loginSection").style.display = "block"
    //document.getElementById("loggedInSection").style.display = "none"
  } else {
    $("#loginSection").hide();
    $("#loggedInSection").show();
    // document.getElementById("loginSection").style.display = "none"
    //document.getElementById("loggedInSection").style.display = "block"
  }
  window.sessionStorage.removeItem("username");
  console.log(window.sessionStorage.getItem("username"));
});

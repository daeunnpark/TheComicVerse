function getUsername() {
  return window.localStorage.getItem("username");
}

function checkUsername1(){
  document.getElementById("temp1").innerHTML = getUsername()
}


// Search dropdown box
$(".dropdown-menu a").click(function() {
  // Update dropdown UI
  $(this)
    .parents(".dropdown")
    .find(".btn")
    .html($(this).text());

  // Update drop box value
  $(this)
    .parents(".dropdown")
    .find(".btn")
    .val($(this).data("value"));

  // Update searchOption
  $("#searchOption").val($(this).data("value"));
});


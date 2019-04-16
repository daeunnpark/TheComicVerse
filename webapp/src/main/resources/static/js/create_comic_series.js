// Category dropdown box
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

  $("#categories").val($(this).data("value"));
});

/*

function getUsername() {
  return window.localStorage.getItem("username");
}

function checkUsername1(){
  document.getElementById("temp1").innerHTML = getUsername()
}



$("#submitCreateSeries").submit(function (event) {

  var url = "/series/create_series";

  $.ajax({
    type: "POST",
    url: url,
    data: $("#submitCreateSeries").serialize(),
    success: function(data){
      alert(data);
    }
  });
  return false;
});
*/

function getUsername() {
  return window.localStorage.getItem("username");
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

  // Update dropdown
  $("#categorydropdownMenuButton").val($(this).data("value"));
});

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


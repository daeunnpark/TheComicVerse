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

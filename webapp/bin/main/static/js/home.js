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

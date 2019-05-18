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

  // Update Series name to be added
  // $("#searchOption").val($(this).data("value"));
});


$( "#save_png" ).click(function( event ) {
  this.href = canvas.toDataURL("image/png;base64");
  document.getElementById("endingScene").value = this.href;
  document.getElementById("AddDerivedEpi").submit();
});

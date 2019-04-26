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

document.getElementById("addNewPage").addEventListener("click", function(ev) {
  var div = document.getElementById("panel"),
    clone = div.cloneNode(true); // true means clone all childNodes and all event handlers
  clone.id = "new_panel";

  document.getElementById("panelTable").appendChild(clone);
});

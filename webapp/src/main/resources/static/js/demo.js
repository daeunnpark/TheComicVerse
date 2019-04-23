(function() {
  var mouseFrom = {},
    mouseTo = {},
    drawType = null,
    canvasObjectIndex = 0,
    textbox = null;
  var drawWidth = 10;
  var color = "#E34F51";
  var drawingObject = null;
  var moveCount = 1;
  var doDrawing = false;

  var canvas = new fabric.Canvas("c", {
    isDrawingMode: true,
    skipTargetFind: true,
    selectable: false,
    selection: false
  });

  window.canvas = canvas;
  window.zoom = window.zoom ? window.zoom : 1;

  canvas.freeDrawingBrush.color = color;
  canvas.freeDrawingBrush.width = drawWidth;

  canvas.on("mouse:down", function(options) {
    var xy = transformMouse(options.e.offsetX, options.e.offsetY);
    mouseFrom.x = xy.x;
    mouseFrom.y = xy.y;
    doDrawing = true;
  });
  canvas.on("mouse:up", function(options) {
    var xy = transformMouse(options.e.offsetX, options.e.offsetY);
    mouseTo.x = xy.x;
    mouseTo.y = xy.y;
    // drawing();
    drawingObject = null;
    moveCount = 1;
    doDrawing = false;
  });
  canvas.on("mouse:move", function(options) {
    if (moveCount % 2 && !doDrawing) {
      return;
    }
    moveCount++;
    var xy = transformMouse(options.e.offsetX, options.e.offsetY);
    mouseTo.x = xy.x;
    mouseTo.y = xy.y;
    drawing();
  });

  canvas.on("selection:created", function(e) {
    if (e.target._objects) {
      var etCount = e.target._objects.length;
      for (var etindex = 0; etindex < etCount; etindex++) {
        canvas.remove(e.target._objects[etindex]);
      }
    } else {
      canvas.remove(e.target);
    }
    canvas.discardActiveObject();
  });

  function transformMouse(mouseX, mouseY) {
    return { x: mouseX / window.zoom, y: mouseY / window.zoom };
  }

  jQuery("#toolsul")
    .find("li")
    .on("click", function() {
      jQuery("#toolsul")
        .find("li>i")
        .each(function() {
          jQuery(this).attr("class", jQuery(this).attr("data-default"));
        });
      jQuery(this)
        .addClass("active")
        .siblings()
        .removeClass("active");
      jQuery(this)
        .find("i")
        .attr(
          "class",
          jQuery(this)
            .find("i")
            .attr("class")
            .replace("black", "select")
        );
      drawType = jQuery(this).attr("data-type");
      canvas.isDrawingMode = false;
      if (textbox) {
        textbox.exitEditing();
        textbox = null;
      }
      if (drawType == "pen") {
        canvas.isDrawingMode = true;
      } else if (drawType == "remove") {
        canvas.selection = true;
        canvas.skipTargetFind = false;
        canvas.selectable = true;
      } else {
        canvas.skipTargetFind = true;
        canvas.selection = false;
      }
    });

  function drawing() {
    if (drawingObject) {
      canvas.remove(drawingObject);
    }
    var canvasObject = null;
    switch (drawType) {
      case "arrow":
        canvasObject = new fabric.Path(
          drawArrow(mouseFrom.x, mouseFrom.y, mouseTo.x, mouseTo.y, 30, 30),
          {
            stroke: color,
            fill: "rgba(255,255,255,0)",
            strokeWidth: drawWidth
          }
        );
        break;
      case "line":
        canvasObject = new fabric.Line(
          [mouseFrom.x, mouseFrom.y, mouseTo.x, mouseTo.y],
          {
            stroke: color,
            strokeWidth: drawWidth
          }
        );
        break;
      case "dottedline":
        canvasObject = new fabric.Line(
          [mouseFrom.x, mouseFrom.y, mouseTo.x, mouseTo.y],
          {
            strokeDashArray: [3, 1],
            stroke: color,
            strokeWidth: drawWidth
          }
        );
        break;
      case "circle":
        var left = mouseFrom.x,
          top = mouseFrom.y;
        var radius =
          Math.sqrt(
            (mouseTo.x - left) * (mouseTo.x - left) +
              (mouseTo.y - top) * (mouseTo.y - top)
          ) / 2;
        canvasObject = new fabric.Circle({
          left: left,
          top: top,
          stroke: color,
          fill: "rgba(255, 255, 255, 0)",
          radius: radius,
          strokeWidth: drawWidth
        });
        break;
      case "ellipse":
        var left = mouseFrom.x,
          top = mouseFrom.y;
        var radius =
          Math.sqrt(
            (mouseTo.x - left) * (mouseTo.x - left) +
              (mouseTo.y - top) * (mouseTo.y - top)
          ) / 2;
        canvasObject = new fabric.Ellipse({
          left: left,
          top: top,
          stroke: color,
          fill: "rgba(255, 255, 255, 0)",
          originX: "center",
          originY: "center",
          rx: Math.abs(left - mouseTo.x),
          ry: Math.abs(top - mouseTo.y),
          strokeWidth: drawWidth
        });
        break;
      case "square":
        break;
      case "rectangle":
        var path =
          "M " +
          mouseFrom.x +
          " " +
          mouseFrom.y +
          " L " +
          mouseTo.x +
          " " +
          mouseFrom.y +
          " L " +
          mouseTo.x +
          " " +
          mouseTo.y +
          " L " +
          mouseFrom.x +
          " " +
          mouseTo.y +
          " L " +
          mouseFrom.x +
          " " +
          mouseFrom.y +
          " z";
        canvasObject = new fabric.Path(path, {
          left: left,
          top: top,
          stroke: color,
          strokeWidth: drawWidth,
          fill: "rgba(255, 255, 255, 0)"
        });
        break;
      case "rightangle":
        var path =
          "M " +
          mouseFrom.x +
          " " +
          mouseFrom.y +
          " L " +
          mouseFrom.x +
          " " +
          mouseTo.y +
          " L " +
          mouseTo.x +
          " " +
          mouseTo.y +
          " z";
        canvasObject = new fabric.Path(path, {
          left: left,
          top: top,
          stroke: color,
          strokeWidth: drawWidth,
          fill: "rgba(255, 255, 255, 0)"
        });
        break;
      case "equilateral":
        var height = mouseTo.y - mouseFrom.y;
        canvasObject = new fabric.Triangle({
          top: mouseFrom.y,
          left: mouseFrom.x,
          width: Math.sqrt(Math.pow(height, 2) + Math.pow(height / 2.0, 2)),
          height: height,
          stroke: color,
          strokeWidth: drawWidth,
          fill: "rgba(255,255,255,0)"
        });
        break;
      case "isosceles":
        break;
      case "text":
        textbox = new fabric.Textbox("", {
          left: mouseFrom.x - 60,
          top: mouseFrom.y - 20,
          width: 150,
          fontSize: 18,
          borderColor: "#2c2c2c",
          fill: color,
          hasControls: false
        });
        canvas.add(textbox);
        textbox.enterEditing();
        textbox.hiddenTextarea.focus();
        break;
      case "remove":
        break;
      default:
        break;
    }
    if (canvasObject) {
      // canvasObject.index = getCanvasObjectIndex();
      canvas.add(canvasObject); //.setActiveObject(canvasObject)
      drawingObject = canvasObject;
    }
  }

  function drawArrow(fromX, fromY, toX, toY, theta, headlen) {
    theta = typeof theta != "undefined" ? theta : 30;
    headlen = typeof theta != "undefined" ? headlen : 10;
    var angle = (Math.atan2(fromY - toY, fromX - toX) * 180) / Math.PI,
      angle1 = ((angle + theta) * Math.PI) / 180,
      angle2 = ((angle - theta) * Math.PI) / 180,
      topX = headlen * Math.cos(angle1),
      topY = headlen * Math.sin(angle1),
      botX = headlen * Math.cos(angle2),
      botY = headlen * Math.sin(angle2);
    var arrowX = fromX - topX,
      arrowY = fromY - topY;
    var path = " M " + fromX + " " + fromY;
    path += " L " + toX + " " + toY;
    arrowX = toX + topX;
    arrowY = toY + topY;
    path += " M " + arrowX + " " + arrowY;
    path += " L " + toX + " " + toY;
    arrowX = toX + botX;
    arrowY = toY + botY;
    path += " L " + arrowX + " " + arrowY;
    return path;
  }

  function getCanvasObjectIndex() {
    return canvasObjectIndex++;
  }
})();
function encodeImageFileAsURL(inputID,outputID) {
    var file = document.getElementById(inputID).files[0];
    var reader = new FileReader();
    reader.onloadend = function() {
        console.log('RESULT', reader.result);
        document.getElementById(outputID).value=reader.result;
    };
    reader.readAsDataURL(file);
}


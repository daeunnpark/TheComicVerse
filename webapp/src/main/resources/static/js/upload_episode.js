function encodeImageFileAsURL() {
    var file = document.getElementById("fileEpisodeThumbnailInput").files[0];
    var reader = new FileReader();
    reader.onloadend = function() {
        console.log('RESULT', reader.result);
        document.getElementById("fileEpisodeByteData").value=reader.result;
    };
    reader.readAsDataURL(file);
}


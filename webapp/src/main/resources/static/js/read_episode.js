$(document).ready(function() {
    if ([[${likes}]]) {
        document.getElementById("likeButton").value = "Unlike";
    }
    document.getElementById(
        "userNameInput1"
    ).value = window.sessionStorage.getItem("username");
    document.getElementById(
        "userNameInput2"
    ).value = window.sessionStorage.getItem("username");
}

function submitLike(){
    if ([[${likes}]]) {
        document.getElementById('submitLike').submit();
    }
    else{
        document.getElementById('submitUnlike').submit();
    }

}


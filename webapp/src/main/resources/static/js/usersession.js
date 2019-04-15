var currentUsername;
function updateUsername(newUsername) {
    currentUsername = newUsername;
    login(currentUsername);
}

function login(currentUsername){
    window.localStorage.setItem("username",currentUsername);
}

function getUsername() {
    return window.localStorage.getItem("username");
}

function checkUsername(){
    document.getElementById("temp0").innerHTML = getUsername();
}
/*
function hideLogin(){
    $("#loginBody").hide();
}
$("#submitLogin").submit(function(e) {
    login($("#username").value);
    hideLogin();
*/
    /*
    var value = document.getElementById("secret_word").value;
    if (is_valid(value)) {
        computerWord = value;
        set_secret_word_UI(value);
        $("#secretWordModal").modal("hide");
    }
    return false;
    */
//});

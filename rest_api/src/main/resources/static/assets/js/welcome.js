//var base_api_url = "http://192.168.160.63:8080";
var base_api_url = "http://localhost:8080";

window.onload = function() {
    let token = localStorage.getItem("token");
    $("#welcome_title").html("Welcome "+localStorage.getItem("username")+"!");
};
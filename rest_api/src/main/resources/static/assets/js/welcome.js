var base_api_url = "http://" + window.location.host;

window.onload = function() {
    let token = localStorage.getItem("token");
    $("#welcome_title").html("Welcome "+localStorage.getItem("username")+"!");
};

var base_api_url = "http://localhost:8080";

window.onload = function() {
    
    $.ajax({
        url: base_api_url+"/freelancer/info",
        type: "GET",
        success: function(data, status, xhr) {
            $("#welcome_title").html("Welcome "+data.body.username+"!");
        },
        error: function(data, status, xhr) {
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
}
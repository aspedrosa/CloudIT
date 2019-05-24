var base_api_url = "http://localhost:8080";

function sign_up_tab() {
    var form = document.getElementById("signin_signup_form");
    document.getElementById("sign_up_link").classList.add("active");
    document.getElementById("sign_in_link").classList.remove("active");
    form.innerHTML = '<div class="form-group" style="text-align: left;"> <label for="name">Name:</label> <input type="text" class="form-control" id="name"> </div> <div class="form-group" style="text-align: left;"> <label for="username">Username:</label> <input type="text" class="form-control" id="username"> </div> <div class="form-group" style="text-align: left;"> <label for="email">Email address:</label> <input type="email" class="form-control" id="email"> </div> <div class="form-group" style="text-align: left;"> <label for="pwd">Password:</label> <input type="password" class="form-control" id="pwd"> </div> <button style="width: 100%; background-color: #3793f7; border: 0px; color: white; font-size: 20px;" type="button" class="btn btn-secondary">Sign Up</button>';
}

function sign_in_tab() {
    var form = document.getElementById("signin_signup_form");
    document.getElementById("sign_up_link").classList.remove("active");
    document.getElementById("sign_in_link").classList.add("active");
    form.innerHTML = '<div class="form-group" style="text-align: left;"> <label for="username">Username:</label> <input type="username" class="form-control" id="username"> </div> <div class="form-group" style="text-align: left;"> <label for="pwd">Password:</label> <input type="password" class="form-control" id="pwd"> </div> <button style="width: 100%; background-color: #3793f7; border: 0px; color: white; font-size: 20px;" type="button" class="btn btn-secondary">Sign In</button>'; 
}

function sign_in() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("pwd").value;
    $.ajax({
        url: base_api_url+"/login",
        type: "GET",
        username: "a",
        password: "a",
        //xhrFields: {
        //    withCredentials: true
        //},
        //headers: {"Authorization":"Basic YTph", 
        //        "Accept":"*/*"},
        crossDomain:true,
        //data: {"username": "\""+username+"\"", "password": "\""+password+"\""},
        success: function(data, status, xhr) {
            console.log("success: "+data+":"+status+":"+xhr);
        },
        error: function(data, status, xhr) {
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
}

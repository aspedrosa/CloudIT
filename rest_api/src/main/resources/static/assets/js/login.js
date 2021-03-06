var base_api_url = "http://" + window.location.host;

function sign_up_tab() {
    var form = document.getElementById("signin_signup_form");
    document.getElementById("sign_up_link").classList.add("active");
    document.getElementById("sign_in_link").classList.remove("active");
    form.innerHTML = '<div class="form-group" style="text-align: left;"> <label for="name">Name:</label> <input type="text" class="form-control" id="name"> </div> <div class="form-group" style="text-align: left;"> <label for="username">Username:</label> <input type="text" class="form-control" id="username"> </div> <div class="form-group" style="text-align: left;"> <label for="email">Email address:</label> <input type="email" class="form-control" id="email"> </div> <div class="form-group" style="text-align: left;"> <label for="pwd">Password:</label> <input type="password" class="form-control" id="pwd"> </div> <div class="form-group" style="text-align: left;"> <label for="type">User Type:</label> <input type="type" class="form-control" id="type"> </div> <div class="row justify-content-center"><div class="col-md-6"><button style="width: 100%; background-color: #3793f7; border: 0px" type="button" onclick="sign_up()" class="btn btn-secondary" id="signUpButton">Sign Up</button></div></div>';
}

function sign_in_tab() {
    var form = document.getElementById("signin_signup_form");
    document.getElementById("sign_up_link").classList.remove("active");
    document.getElementById("sign_in_link").classList.add("active");
    form.innerHTML = '<div class="form-group" style="text-align: left;"> <label for="username">Username:</label> <input type="username" class="form-control" id="username"> </div> <div class="form-group" style="text-align: left;"> <label for="pwd">Password:</label> <input type="password" class="form-control" id="pwd"> </div> <div class="row justify-content-center"><div class="col-md-6"><button style="width: 100%; background-color: #3793f7; border: 0px" type="button" onclick="sign_in()" class="btn btn-secondary" id="submit_button">Sign In</button></div></div>';
}

function sign_in() {
    // server sign in
    $("#invalid_credentials_message").remove();
    var username = document.getElementById("username").value;
    var password = document.getElementById("pwd").value;
    $.ajax({
        url: base_api_url+"/login",
        type: "GET",
        headers: {
            "Authorization": "Basic " + btoa(username + ":" + password),
            'X-Requested-With': 'XMLHttpRequest'
        },
        xhrFields: { withCredentials: true },   
        crossDomain:true,
        success: function(data, status, xhr) {
            console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
            if(status!=="success"){
                $("#submit_button").before('<div id="invalid_credentials_message" class="alert alert-danger">\
                    <strong>Error!</strong> Invalid Credentials.\
                </div>');
                alert(JSON.stringify(data));
            }else{
                localStorage.setItem("username", username);
                localStorage.setItem("type", data.data.type);
                window.location.href= base_api_url+"/welcomePage";
            }
        },
        error: function(data, status, xhr) {
            $("#submit_button").before('<div id="invalid_credentials_message" class="alert alert-danger">\
                <strong>Error!</strong> Invalid Credentials.\
            </div>');
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
    
    // developer sign in
    /*
    localStorage.setItem("username", "user-dev");
    localStorage.setItem("token", btoa("user-dev:pwd-dev"));
    window.location.href= base_api_url+"/welcome";
     */
}

function sign_up() {
    var self=this;
    $("#invalid_credentials_message").remove();
    var name = document.getElementById("name").value;
    var username = document.getElementById("username").value;
    var email = document.getElementById("email").value;
    var type = document.getElementById("type").value;
    var password = document.getElementById("pwd").value;
    let data={};
    if(name!==""){
        data["name"]=name;
    }
    if(username!==""){
        data["username"]=username;
    }
    if(email!==""){
        data["email"]=email;
    }
    if(type!==""){
        data["type"]=type;
    }
    if(password!==""){
        data["password"]=password;
    }
    $.ajax({
        url: base_api_url+"/register",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json",
        crossDomain:true,
        success: function(data, status, xhr) {
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                sign_in();
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
        } 
    });
}

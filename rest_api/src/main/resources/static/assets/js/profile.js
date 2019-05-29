var base_api_url = "http://" + window.location.host;

var profile = {};

window.onload = function() {
    profile["type"] = localStorage.getItem("type");
    profile["username"] = localStorage.getItem("username");
    document.getElementById("type").innerHTML = profile["type"];
    document.getElementById("username").innerHTML = profile["username"];
};

$("#profile_form").submit(function(event) {
    event.preventDefault();

    // get form content

    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var description = document.getElementById("description").value;
    var current_password = document.getElementById("cur_pwd").value;
    var new_password = document.getElementById("new_pwd").value;
    var new_password_confirmation = document.getElementById("new_pwd_conf").value;

    // verify fields

    let data={};

    if(name!==""){
        data["name"]=name;
    } else {
        // warning toast
    }

    if(email!==""){
        data["email"]=email;
    } else {
        // warning toast
    }

    if(description!==""){
        data["description"]=description;
    }

    if(current_password!==""){
        //data["current_password"]=current_password;
        if(new_password!=="" && new_password===new_password_confirmation) {
            data["new_password"]=new_password;
        } else {
            // warning toast
        }
    }

    // send http request

    console.log(data);
    $.ajax({
        url: base_api_url+"/profile", 
        type: "PUT",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json",
        crossDomain:true,
        headers: {
            "Authorization": "Basic " + btoa(profile["username"] + ":" + current_password),
            'X-Requested-With': 'XMLHttpRequest'
        },
        success: function(data, status, xhr) {
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
                //localStorage.setItem("profile") = profile;
                window.location.href= base_api_url+"/profilePage";
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
        } 
    });
});

$("#clear_btn").click(function(event){
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("description").value = "";
    document.getElementById("cur_pwd").value = "";
    document.getElementById("new_pwd").value = "";
    document.getElementById("new_pwd_conf").value = "";
    
});


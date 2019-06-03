var base_api_url = "http://" + window.location.host;

var profile = {
    type: "",
    username: "",
    name: "",
    email: "",
    interestedAreas: ""
};

window.onload = function() {
    profile["type"] = localStorage.getItem("type");
    profile["username"] = localStorage.getItem("username");
    document.getElementById("type").innerHTML = profile["type"];
    document.getElementById("username").innerHTML = profile["username"];
    
    $.ajax({
        url: base_api_url + "/profile",
        type: "GET",
        crossDomain:true,
        success: function(data, status, xhr) {
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
                data = data.data;
                document.getElementById("name").value = data.name;
                document.getElementById("email").value = data.email;
                console.log(data.interestedAreas);
                for(var i=0; i<data.interestedAreas.length; i++) {
                    $("#interestedAreas").tagsinput("add",data.interestedAreas[i]["area"]);
                }
                profile["name"] = data.name;
                profile["email"] = data.email;
                profile["interestedAreas"] = data.interestedAreas;
                console.log(profile)
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
        } 
    });
};

$("#update_btn").click(function(event) {
    // get form content

    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var interestedAreas = $("#interestedAreas").tagsinput("items");
    var current_password = document.getElementById("cur_pwd").value;
    var newPassword = document.getElementById("new_pwd").value;
    var newPassword_confirmation = document.getElementById("new_pwd_conf").value;

    // verify fields

    let data={};

    data["username"] = localStorage.getItem("username");

    if(name!==profile["name"]) {
        if(name!=""){
            data["name"]=name;
        } else {
            // warning toast
        }
    }

    if(email!==profile["email"]) {
        if(email!=""){
            data["email"]=email;
        } else {
            // warning toast
        }
    }

    if(JSON.stringify(interestedAreas.sort()) != JSON.stringify(profile.interestedAreas.sort())) {
        data["interestedAreas"]=interestedAreas;
    }

    if(current_password!=""){
        data["password"]=current_password;
        if(newPassword!="" && newPassword==newPassword_confirmation) {
            data["newPassword"]=newPassword;
        } else {
            // warning toast
        }
    }

    console.log(profile);
    console.log(data);

    // send http request

    if(Object.keys(data).length>1) {
        $.ajax({
            url: base_api_url+"/profile",
            type: "PUT",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json",
            crossDomain:true,
            success: function(data, status, xhr) {
                if(status!=="success"){
                    alert(JSON.stringify(data));
                }else{
                    console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
                    window.location.href= base_api_url+"/profilePage";
                }
            },
            error: function(data, status, xhr) {
                alert(JSON.stringify(data));
            } 
        });
    }
});

$("#clear_btn").click(function(event){
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("interestedAreas").value = "";
    document.getElementById("cur_pwd").value = "";
    document.getElementById("new_pwd").value = "";
    document.getElementById("new_pwd_conf").value = "";
    
});


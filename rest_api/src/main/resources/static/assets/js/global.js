var base_api_url = "http://" + window.location.host;

$(document).ready(function(){
    var secureEndpoints = ["/profilePage", "/messagesPage", "/jobsPage", "/welcomePage"]
    let path = window.location.pathname;
    
    //CHECK IF SIGNED IN
    if(localStorage.getItem("username")===null || localStorage.getItem("username")===null || document.cookie===null){
        if(secureEndpoints.includes(path)){
            window.location.href= base_api_url+"/loginPage";
        }
    }
    
    //LOAD NAVBAR
    $('#navBarID').load('assets/components/navbar.html', function(){
        $('#navBarList').ready(function(){
            $('#navBarList a').each(function() {
                if(localStorage.getItem("username")===null || localStorage.getItem("username")===null || document.cookie===null){
                    if(secureEndpoints.includes($(this).attr("href"))){
                        $(this).css("display", "none");
                    }
                }
                if($(this).attr("href")===path){
                    $(this).addClass("active");
                }
            });
        })
    });
    
    //LOAD FOOTER
    $('#footerID').load('assets/components/footer.html');
    
    
})

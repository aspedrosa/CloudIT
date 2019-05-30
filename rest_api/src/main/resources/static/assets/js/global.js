var base_api_url = "http://" + window.location.host;

$(document).ready(function(){
    //CHECK IF SIGNED IN
    console.log(document.cookie)
    $.ajax({
            url: base_api_url+"/joboffer/self",
            type: "GET",
            crossDomain:true,
            success: function(data, status, xhr) {},
            error: function(data, status, xhr) {
                window.location.href= base_api_url+"/loginPage";
            }
        });  
    
    //LOAD NAVBAR
    let path = window.location.pathname;
    $('#navBarID').load('assets/components/navbar.html', function(){
        $('#navBarList').ready(function(){
            $('#navBarList li a').each(function() {
                if($(this).attr("href")===path){
                    $(this).parent().addClass("active");
                }
            });
        })
    });
    
    
})

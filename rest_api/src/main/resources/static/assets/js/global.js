var base_api_url = "http://" + window.location.host;

function viewModel() {
    var self=this;
    self.messages=ko.observableArray([])
    
    self.refreshMessages = function(msgs){
        self.messages.removeAll();
        console.log("aqui")
        console.log(msgs)
        for(let m in msgs){
            self.messages.push(msgs[m]);
        }
    }
    
};
var viewModel = new viewModel()


$(document).ready(function(){
    var secureEndpoints = ["/profilePage", "/messagesPage", "/jobsPage", "/welcomePage"]
    let path = window.location.pathname;
    
    //CHECK IF SIGNED IN
    if(localStorage.getItem("username")===null || localStorage.getItem("username")===null || document.cookie===null){
        if(secureEndpoints.includes(path)){
            window.location.href= base_api_url+"/loginPage";
        }
    }else{
        
        var socket = new SockJS(base_api_url+'/secured/messageCenter');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/secured/queue/'+localStorage.getItem("username"), function (greeting) {
                var body = JSON.parse(greeting.body)
                console.log(body);
                if("message" in body){
                    $.notify("From: "+body.origin+"\nMessage: "+body.message.substring(0, 10)+"...", "info");
                }else{
                    var allMsgs=body.messages
                    var processedMsgs=[]
                    var index=0;
                    for(let m in allMsgs){
                        let time=allMsgs[m].date;
                        
                        n =  new Date(time);
                        hr = n.getHours();
                        mt = n.getMinutes()
                        allMsgs[m].date = hr + ":" + mt;
                        
                        allMsgs[m].index=index%2;
                        index+=1;
                        
                        processedMsgs.push(allMsgs[m])
                        viewModel.refreshMessages(processedMsgs)
                    }
                }
            });
        });
        
        sendMessage=function sendMessage(msg,destin){
            stompClient.send("/secured/message", {}, JSON.stringify({'message': msg, "destination":destin}));
        }
        
        getAllMessages=function getAllMessages(destin){
            stompClient.send("/secured/allMessages", {}, JSON.stringify({"destination":destin}));
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

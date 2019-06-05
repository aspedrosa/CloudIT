var base_api_url = "http://" + window.location.host;

function viewModel() {
    var self=this;
    self.messages=ko.observableArray([])
    self.contacts=ko.observableArray([])
    
    self.origin=localStorage.getItem("username")
    
    self.refreshMessages = function(msgs){
        self.messages.removeAll();

        for(let m in msgs){
            if(msgs[m].origin===self.origin){
                msgs[m]["class"]=0
            }else{
                msgs[m]["class"]=1
            }
            self.messages.push(msgs[m]);
        }
    }
    
    self.refreshContacts = function(contacts){
        self.contacts.removeAll();

        for(let c in contacts){
            self.contacts.push(contacts[c]);
        }
    }
    
    self.addMsg =  function(msg){
        msg["class"]=0
        self.messages.push(msg);
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
            getContacts()
            stompClient.subscribe('/secured/queue/'+localStorage.getItem("username"), function (greeting) {
                var body = JSON.parse(greeting.body)
                console.log(body);
                if("message" in body){
                    $.notify("From: "+body.origin+"\nMessage: "+body.message.substring(0, 10)+"...", "info");
                }else if("messages" in body){
                    var allMsgs=body.messages
                    var processedMsgs=[]
                    for(let m in allMsgs){
                        let time=allMsgs[m].date;
                        
                        n =  new Date(time);
                        hr = n.getHours();
                        mt = n.getMinutes()
                        allMsgs[m].date = hr + ":" + mt;
                        
                        processedMsgs.push(allMsgs[m])
                        viewModel.refreshMessages(processedMsgs)
                    }
                }else if("contacts" in body){
                    viewModel.refreshContacts(body["contacts"])
                }
            });
        });
        
        sendMessage=function (msg,destin){
            var m = {'message': msg, "destination":destin};
            stompClient.send("/secured/message", {}, JSON.stringify(m));
            m["origin"]=localStorage.getItem("username");
            
            n =  new Date();
            hr = n.getHours();
            mt = n.getMinutes()
            m.date = hr + ":" + mt;
            
            viewModel.addMsg(m);
        }
        
        getAllMessages=function (destin){
            stompClient.send("/secured/allMessages", {}, JSON.stringify({"destination":destin}));
        }
        
        getContacts=function (){
            stompClient.send("/secured/contacts", {});
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

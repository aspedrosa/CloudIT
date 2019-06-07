var base_api_url = "http://" + window.location.host;

function viewModel() {
    var self=this;
    self.messages=ko.observableArray([])
    self.myOffers=ko.observableArray([])
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

    self.refreshOffers = function(){
        $.ajax({
        url: base_api_url+"/joboffer/self",
        type: "GET",
        crossDomain:true,
        success: function(data, status, xhr) {
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                //console.log(data)
                self.myOffers.removeAll();
                for(let index in data.data){
                    self.myOffers.push(data.data[index]);
                }
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
        });  
    }
    
    self.refreshContacts = function(contacts){
        self.contacts.removeAll();
        for(let c in contacts){
            self.contacts.push(contacts[c]);
        }
        
        var url = new URL(window.location.href);
        var addName = url.searchParams.get("addName");
        var addUsername = url.searchParams.get("addUsername");

        if(addName!==null && addUsername!==null){
            let control=true
            for(let u in self.contacts.peek()){
                if(self.contacts.peek()[u][0]===addUsername){
                    control=false
                }
            }
            if(control){
                self.contacts.push([addUsername, addName]);
            }
        }
    }
    
    self.addContact =  function(contact){
        self.contacts.push(contact);
    }
    
    self.addMsg =  function(msg){
        if(msg.origin===self.origin){
            msg["class"]=0
        }else{
            msg["class"]=1
        }
        self.messages.push(msg);
        $('#class-room-msgs').scrollTop($('#class-room-msgs')[0].scrollHeight);
    }
    
};
var viewModel = new viewModel();


$(document).ready(function(){
    var secureEndpoints = ["/profilePage", "/messagesPage", "/jobsPage", "/welcomePage"]
    let path = window.location.pathname;
    
    connected=false;
    
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
            connected=true
            stompClient.subscribe('/secured/queue/'+localStorage.getItem("username"), function (greeting) {
                var body = JSON.parse(greeting.body)
                console.log(body);
                if("message" in body){
                    
                    n =  new Date(body.date);
                    hr = n.getHours();
                    mt = n.getMinutes()
                    body.date = hr + ":" + mt;
                    
                    viewModel.addMsg(body)
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

        updateAutomaticMessage=function (id, msg, event, offerID, worker){
            stompClient.send("/secured/updateMessage", {}, JSON.stringify([id,msg, event, offerID, worker]));
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
                    if(secureEndpoints.includes($(this).attr("href")) || $(this).attr("href")=="/loginPage"){
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
    
    console.log(localStorage);
})

function logout() {
    localStorage.clear();
    $.ajax({
        url: base_api_url+"/logout",
        type: "GET",
        contentType: "application/json",
        crossDomain:true,
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
        } 
    });
}
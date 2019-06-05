window.onload = function(){
    resizeMessages();
    
    n =  new Date();
    y = n.getFullYear();
    m = n.getMonth() + 1;
    d = n.getDate();
    document.getElementById("date").innerHTML = d + "/" + m + "/" + y;
    
    var msg = document.getElementById("msgText");

    msg.addEventListener("keyup", function(event) {
      if (event.keyCode === 13) {
        sendMsg()
      }
    });
    
    ko.applyBindings(viewModel);
}

$(window).resize(resizeMessages);

function resizeMessages() {
    $("#class-room-msgs").css("height", ($("#chat-room-msg-center").css("height").split("px")[0]-135)+"px");
}


function sendMsg(){
    var msg = $("#msgText").val()
    $("#msgText").val("")
    console.log(msg)
    var msgDestin = $("#msgDestination").text()
    if(msg!==""){
        sendMessage(msg, msgDestin, true)
    }
    
    $('#class-room-msgs').scrollTop($('#class-room-msgs')[0].scrollHeight);
}

async function loadMessages(contact){
    getAllMessages(contact[0])
    $("#msgDestinName").text(contact[1])
    $("#msgDestination").text(contact[0])
    await new Promise(resolve => setTimeout(resolve, 300));
    $('#class-room-msgs').scrollTop($('#class-room-msgs')[0].scrollHeight);
}

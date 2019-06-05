var base_api_url = "http://" + window.location.host;

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
  
  viewModel.refreshOffers();
  ko.applyBindings(viewModel);
}

$(window).resize(resizeMessages);

function resizeMessages() {
  $("#class-room-msgs").css("height", ($("#chat-room-msg-center").css("height").split("px")[0]-135)+"px");
}

function sendMsg(){
    var msg = $("#msgText").val();
    $("#msgText").val("");
    var msgDestin = $("#msgDestination").text();
    if(msg!==""){
        sendMessage(msg, msgDestin, true);
    }
    
    $('#class-room-msgs').scrollTop($('#class-room-msgs')[0].scrollHeight);
}

async function loadMessages(contact){
    getAllMessages(contact[0]);
    $("#msgDestinName").text(contact[1]);
    $("#msgDestination").text(contact[0]);
    await new Promise(resolve => setTimeout(resolve, 300));
    $('#class-room-msgs').scrollTop($('#class-room-msgs')[0].scrollHeight);
}

function sendMsgWithOffer(offer) {
  //console.log(offer);
  var msg = ">>> automatic-message | job-offer: _"+offer.id + "_" + offer.title + "_ <<<";
  var msgDestin = $("#msgDestination").text();
  sendMessage(msg, msgDestin, true);
}

function formatMessage(id, msg,origin) {
  if(msg.startsWith(">>> automatic-message | ")) {
    var msgArr = msg.split("_");
    var retval;
    if(localStorage.getItem("username") != origin) { 
      retval = "<h3>You sent offer '" + msgArr[2] + "'</h3>";
    } else {
      retval = "<h3>'" + msgArr[2] + "'</h3>"
              +"<button class='btn btn-primary' onclick='acceptOfferMsg(" + id + "," + msgArr[2] + ")'>" + "Accept" + "</button>"
              +"<button class='btn btn-danger'  onclick='denyOfferMsg(" + id + "," + msgArr[2] + ")'  style='margin-left: 10px;'>" + "Deny" + "</button>";
    }
    return retval;
  }
  return msg;
}

function acceptOfferMsg(id, offerTitle) {
  var fMsg = "<h3>" + offerTitle + "</h3><h3 style='color:royalblue'> Accepted</h3>";
  updateAutomaticMessage(id, fMsg);
}

function denyOfferMsg(id, offerTitle) {
  var fMsg = "<h3>" + offerTitle + "</h3><h3 style='color:red'> Denied</h3>";
  updateAutomaticMessage(id, fMsg);
}
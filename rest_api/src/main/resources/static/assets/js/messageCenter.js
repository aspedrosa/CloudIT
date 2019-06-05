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
  console.log(msg);
  var msgDestin = $("#msgDestination").text();
  if(msg!==""){
      sendMessage(msg, msgDestin, true);
      new Promise(resolve => setTimeout(resolve, 1000));
      getAllMessages(msgDestin);
  }
}

function sendMsgWithOffer(offer) {
  //console.log(offer);
  var msg = ">>> automatic-message | job-offer: _"+offer.id + "_" + offer.title + "_ | msg-author: _" + localStorage.getItem("username") + "_ <<<";
  console.log(msg);
  var msgDestin = $("#msgDestination").text();
  sendMessage(msg, msgDestin, true);
  new Promise(resolve => setTimeout(resolve, 1000));
  getAllMessages(msgDestin);
}

function formatMessage(msg) {
  if(msg.startsWith(">>> automatic-message | ")) {
    //var msgArr = msg.split("_");
    console.log("automatic message");
    return "<h1>" + msg + "</h1>";
  }
  return msg;
}
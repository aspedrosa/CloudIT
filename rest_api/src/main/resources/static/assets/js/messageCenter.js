window.onload = resizeMessages();

$(window).resize(resizeMessages);

function resizeMessages() {
    $("#class-room-msgs").css("height", ($("#chat-room-msg-center").css("height").split("px")[0]-135)+"px");
}
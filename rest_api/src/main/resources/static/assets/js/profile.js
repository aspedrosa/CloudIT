$("#profile_form").submit(function(event){
    console.log("Submitted");
    event.preventDefault();
});

$("#clear_btn").click(function(event){
    console.log("Clear");
    console.log(event);
});
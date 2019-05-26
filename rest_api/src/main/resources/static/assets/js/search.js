//var base_api_url = "http://192.168.160.63:8080";
var base_api_url = "http://localhost:8080";

function getOffers() {
    var self=this;
    self.jobOffers=ko.observableArray([])
    $.ajax({
        url: base_api_url+"/joboffer",
        type: "GET",
        crossDomain:true,
        success: function(data, status, xhr) {
            console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                console.log(data)
                for(let index in data.data){
                    self.jobOffers.push(data.data[index]);
                }
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
    
};
ko.applyBindings(new getOffers());


function showModal(job){
    $("#modalTitle").text(job.title);
    $("#modalArea").text(job.area);
    $("#modalAmount").text(job.amount);
    $("#modalDescription").text(job.description);
    $("#modalDate").text(job.date);
    $("#modalCreator").text(job.creator);
}

$(document).ready(function(e){    
    
    $('.search-panel .dropdown-menu').find('a').click(function(e) {
            e.preventDefault();
            var param = $(this).attr("href").replace("#","");
            var concept = $(this).text();
            $('.search-panel span#search_concept').text(concept);
            $('.input-group #search_param').val(param);
    });
});

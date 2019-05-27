//var base_api_url = "http://192.168.160.63:8080";
var base_api_url = "http://localhost:8080";
function getOffers() {
    var self=this;
    self.myOffers=ko.observableArray([])
    self.acceptedOffers=ko.observableArray([])
    
    self.refresh = function(){
        $.ajax({
            url: base_api_url+"/joboffer/self",
            type: "GET",
            crossDomain:true,
            success: function(data, status, xhr) {
                console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
                if(status!=="success"){
                    alert(JSON.stringify(data));
                }else{
                    console.log(data)
                    self.myOffers.removeAll()
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
                    self.acceptedOffers.removeAll()
                    for(let index in data.data){
                        self.acceptedOffers.push(data.data[index]);
                    }
                }
            },
            error: function(data, status, xhr) {
                alert(JSON.stringify(data));
                console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
            }
        });
    }
    
};

var offers = new getOffers()
offers.refresh()

ko.applyBindings(offers);


function showModal(job){
    $("#modalTitle").text(job.title);
    $("#modalArea").text(job.area);
    $("#modalAmount").text(job.amount);
    $("#modalDescription").text(job.description);
    $("#modalDate").text(job.date);
    $("#modalCreator").text(job.creator);
}

function createOffer(){
    
    var title = document.getElementById("offerTitle").value;
    var description = document.getElementById("offerDescription").value;
    var area = document.getElementById("offerArea").value;
    var amount = document.getElementById("offerAmount").value;
    var date = document.getElementById("offerDate").value;
    
    let data={};
    data["title"]=title;
    data["description"]=description;
    data["area"]=area;
    data["amount"]=amount;
    data["date"]=date;
    
    $.ajax({
        url: base_api_url+"/joboffer",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json",
        crossDomain:true,
        success: function(data, status, xhr) {
            console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                console.log(data)
                offers.refresh()
                $('#createModal').modal('hide');
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
}

function deleteOffer(data){
    event.stopPropagation()
    $.ajax({
        url: base_api_url+"/joboffer/id/"+data.id,
        type: "DELETE",
        crossDomain:true,
        success: function(data, status, xhr) {
            console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                console.log(data)
                offers.refresh()
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
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

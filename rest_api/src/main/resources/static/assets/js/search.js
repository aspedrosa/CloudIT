var base_api_url = "http://" + window.location.host;

function getOffers() {
    var self=this;
    self.jobOffersColumn1=ko.observableArray([])
    self.jobOffersColumn2=ko.observableArray([])
    
    self.refresh = function(){
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
                    self.jobOffersColumn1.removeAll()
                    self.jobOffersColumn2.removeAll()
                    for(let index in data.data){
                        if(index%2===0){
                            self.jobOffersColumn1.push(data.data[index]);
                        }else{
                            self.jobOffersColumn2.push(data.data[index]);
                        }
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
    $("#modalCreatorName").text(job.creator.name);
    $("#modalCreatorUsername").text(job.creator.username);
    $("#modalCreatorEmail").text(job.creator.email);
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

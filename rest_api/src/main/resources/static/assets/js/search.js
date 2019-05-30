var base_api_url = "http://" + window.location.host;

function getOffers() {
    var self=this;
    self.jobOffers=ko.observableArray([])
    
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
                    self.jobOffers.removeAll()
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
    }
    
    self.search = function() {
        var query = $("#query").val();
        var isTitle = $("#isTitle").is(":checked");
        var isArea = $("#isArea").is(":checked");
        var fromAmount = $("#fromAmount").val();
        if (fromAmount == "") fromAmount = -1.0;
        var toAmount = $("#toAmount").val();
        if (toAmount == "") toAmount = -1.0;
        var fromDate = $("#fromDate").val();
        var toDate = $("#toDate").val();
        $.ajax({
            url: base_api_url+"/joboffer/advancedSearch",
            type: "POST",
            data: JSON.stringify({"query": query, "title":isTitle, "area":isArea, 
                   "fromAmount":fromAmount, "toAmount":toAmount, 
                   "fromDate":fromDate, "toDate":toDate}),
            dataType: "json",
            contentType: "application/json",
            crossDomain:true,
            success: function(data, status, xhr) {
                console.log("success: "+data + ", "+status+", "+JSON.stringify(xhr));
                if(status!=="success"){
                    alert(JSON.stringify(data));
                }else{
                    console.log(data)
                    self.jobOffers.removeAll()
                    for(let index in data.data){
                        console.log(data.data[index]);
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
};

var offers = new getOffers()
offers.refresh()

ko.applyBindings(offers);

function advancedSearch() {
    offers.search();
}

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

function changeFilterMenu() {
    if ($("#filters_menu").css("display") === "none")
        $("#filters_menu").css("display", "block");
    else
        $("#filters_menu").css("display", "none");
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

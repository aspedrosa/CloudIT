var base_api_url = "http://" + window.location.host;
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
            url: base_api_url+"/joboffer/accepted",
            type: "GET",
            crossDomain:true,
            success: function(data, status, xhr) {
                if(status!=="success"){
                    alert(JSON.stringify(data));
                }else{
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

var currentModalId = null;
function showModal(job, event){
    if(event==="accept"){
        $("#edit_save_btn").text("Finish")
    }else{
        $("#edit_save_btn").text("Edit")
    }
    
    currentModalId = job.id;
    $("#modalTitleH3").text(job.title);
    $("#modalTitle").text(job.title);
    $("#modalArea").val(job.area);
    $("#modalAmount").val(job.amount);
    $("#modalDescription").val(job.description);
    $("#modalDate").val(job.date);
    $("#modalCreatorName").val(job.creator.name);
    $("#modalCreatorUsername").val(job.creator.username);
    $("#modalCreatorEmail").val(job.creator.email);
}

function contact(){
    var name =$("#modalCreatorName").val();
    var username = $("#modalCreatorUsername").val();
    window.location.href= base_api_url+"/messagesPage?"+"addName="+name+"&addUsername="+username;
}

function createOffer(){
    
    var title = document.getElementById("offerTitle").value;
    var description = document.getElementById("offerDescription").value;
    var area = document.getElementById("offerArea").value;
    var amount = document.getElementById("offerAmount").value;
    var date = document.getElementById("offerDate").value;
    var type;
    
    if(localStorage.getItem("type")==="Employer"){
        type = "Offer"
    }else{
        type = "Proposal"
    }
    
    let data={};
    if(title!==""){
        data["title"]=title;
    }
    if(description!==""){
        data["description"]=description;
    }
    if(area!==""){
        data["area"]=area;
    }
    if(amount!==""){
        data["amount"]=amount;
    }
    if(date!==""){
        data["date"]=date;
    }
    data["type"] = type;
    
    $.ajax({
        url: base_api_url+"/joboffer",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json",
        crossDomain:true,
        success: function(data, status, xhr) {
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
                offers.refresh();
                $('#createModal').modal('hide');
            }
        },
        error: function(data, status, xhr) {
            alert(JSON.stringify(data));
            console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
        }
    });
}

function enableEdit_orSave() {
    if($("#edit_save_btn").text()!=="Finish"){
        if($("#edit_save_btn").text()=="Edit") {
            console.log("Edit Mode");
            $("#edit_save_btn").text("Save");

            $("#titleEdit").prop('hidden', false);
            document.getElementById("modalTitle").value = document.getElementById("modalTitleH3").textContent;
            $("#modalTitleH3").text("Edit Job");
            $("#modalDescription").prop('disabled', false);
            $("#modalArea").prop('disabled', false);
            $("#modalDate").prop('disabled', false);
            $("#modalAmount").prop('disabled', false);

        } else {
            console.log("View Mode");

            var title = document.getElementById("modalTitle").value;
            var description = document.getElementById("modalDescription").value;
            var area = document.getElementById("modalArea").value;
            var amount = document.getElementById("modalAmount").value;
            var date = document.getElementById("modalDate").value;

            if(title=="" || description=="" || area=="" || amount=="" || date==""){
                // warning toast
                return;
            }
            $("#edit_save_btn").text("Edit");

            let data={};
            data["title"]=title;
            data["description"]=description;
            data["area"]=area;
            data["amount"]=amount;
            data["date"]=date;

            console.log("Data to be sent:");
            console.log(data);

            $.ajax({
                url: base_api_url+"/joboffer/edit/" + currentModalId,
                type: "PUT",
                data: JSON.stringify(data),
                dataType: "json",
                contentType: "application/json",
                crossDomain:true,
                success: function(data, status, xhr) {
                    if(status!=="success"){
                        alert(JSON.stringify(data));
                    }else{
                        console.log("Data received:");
                        console.log(data);

                        offers.refresh();
                        //$('#offerModal').modal('hide');

                        $("#modalTitleH3").text(title);
                        $("#titleEdit").prop('hidden', true);
                        $("#modalDescription").prop('disabled', true);
                        $("#modalArea").prop('disabled', true);
                        $("#modalDate").prop('disabled', true);
                        $("#modalAmount").prop('disabled', true);

                    }
                },
                error: function(data, status, xhr) {
                    alert(JSON.stringify(data));
                    console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
                }
            });
        }
    }else{
        $.ajax({
            url: base_api_url+"/joboffer/finish/" + currentModalId,
            type: "GET",
            crossDomain:true,
            success: function(data, status, xhr) {
                if(status!=="success"){
                    alert(JSON.stringify(data));
                }else{
                    offers.refresh()
                }
            },
            error: function(data, status, xhr) {
                alert(JSON.stringify(data));
                console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
            }
        });
    }
}

function deleteOffer(data){
    event.stopPropagation()
    $.ajax({
        url: base_api_url+"/joboffer/id/"+data.id,
        type: "DELETE",
        crossDomain:true,
        success: function(data, status, xhr) {
            if(status!=="success"){
                alert(JSON.stringify(data));
            }else{
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
    
    if(localStorage.getItem("type")==="Employer"){
        $("#acceptedOffersOrProposals").text("Accepted Proposals")
    }else{
        $("#createJobButton").text("Create a new Job Proposal");
        $("#offerOrProposal").text("My Proposals")
    }
    
});

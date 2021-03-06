var base_api_url = "http://" + window.location.host;
var job_search_path = "/joboffer/advancedSearchProposal";
var all_jobs = localStorage.getItem("type")==="Employer" ? "/joboffer/proposal" : "/joboffer";

function AppViewModel() {
    var self=this;
    self.jobOffers=ko.observableArray([]);
    self.users=ko.observableArray([]);
    self.searchBy = ko.observable("jobs");

    self.refresh = function(){
        $.ajax({
            url: base_api_url+all_jobs,
            type: "GET",
            crossDomain:true,
            success: function(data, status, xhr) {
                if(status!=="success"){
                    alert(JSON.stringify(data));
                }else{
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
    };

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
            url: base_api_url+job_search_path,
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
                alert("No job found!");
                console.log("error: "+JSON.stringify(data)+":"+status+":"+xhr);
            }
        });
    };

    //jobs of the clicked user on user search
    self.userModalJobs = ko.observableArray([]);
    //user type of the clicked user on user search
    self.userModalUserType = ko.observable("");
    self.searchUsers = function() {
        let name =  $("#searchUserName").val().trim();
        let interestedAreas =  $("#searchUserInterestedAreas").tagsinput("items");
        let userType =  $("#searchUserType").val();

        let requestParams = {};

        if (name.length > 0) {
            requestParams["name"] = name;
        }
        if (interestedAreas.length > 0) {
            requestParams["areas"] = interestedAreas;
        }
        if (userType != "null") {
            requestParams["userType"] = userType;
        }

        $.ajax({
            url: base_api_url + "/profile/search",
            type: "POST",
            data: JSON.stringify(requestParams),
            dataType: "json",
            contentType: "application/json",
            crossDomain: true,
            success: function (data, status, xhr) {
                self.users.removeAll();
                for (let index in data.data) {
                    self.users.push(data.data[index]);
                }
            },
            error: function(data, status, xhr) {
                console.log(data.responseText);
                console.log(status);

                alert(data.responseJSON.message);
            }
        });
    }
}

var appViewModel = new AppViewModel();
appViewModel.refresh();
appViewModel.searchUsers();

ko.applyBindings(appViewModel);

function showOfferModal(job){
    //$("#m1o").modal();
    $("#offerModalTitle").text(job.title);
    $("#offerModalId").text(job.id);
    $("#offerModalArea").text(job.area);
    $("#offerModalAmount").text(job.amount);
    $("#offerModalDescription").text(job.description);
    $("#offerModalDate").text(job.date);
    $("#offerModalCreatorName").text(job.creator.name);
    $("#offerModalCreatorUsername").text(job.creator.username);
    $("#offerModalCreatorEmail").text(job.creator.email);
}

/**
 * Updates information on variables for the clicked user
 */
function fillSearchUserModalTable(user) {
    console.log(user)
    name=user.name
    username=user.username
    userType=user.userType
    email=user.email
    jobs=user.jobOffers
    $("#userModalTitle").text(name);
    console.log(username)
    $("#userModalUsername").text(username);
    $("#userModalEmail").text(email);

    appViewModel.userModalUserType(userType);
    appViewModel.userModalJobs.removeAll();
    jobs.forEach(function (job, _) {
        appViewModel.userModalJobs.push(job);
    })
}

function changeFilterMenu() {
    if ($("#filters_menu").css("display") === "none")
        $("#filters_menu").css("display", "block");
    else
        $("#filters_menu").css("display", "none");
}

function contact(username, name){
    window.location.href= base_api_url+"/messagesPage?"+"addName="+name+"&addUsername="+username;
}


$(document).ready(function(e){    
    
    $('.search-panel .dropdown-menu').find('a').click(function(e) {
            e.preventDefault();
            var param = $(this).attr("href").replace("#","");
            var concept = $(this).text();
            $('.search-panel span#search_concept').text(concept);
            $('.input-group #search_param').val(param);
    });
    
    if(localStorage.getItem("type")==="Employer") {        
        $("#offers_label").text("Proposals");
        job_search_path = "/joboffer/advancedSearch";
    }
});

function showInterest(){
    var name =$("#offerModalCreatorName").text();
    var username = $("#offerModalCreatorUsername").text();
    var offerId = $("#offerModalId").text();
    var offerTitle = $("#offerModalTitle").text();
    window.location.href= base_api_url+"/messagesPage?"+"addName="+name+"&addUsername="+username+"&offerTitle="+offerTitle+"&offerId="+offerId;
}
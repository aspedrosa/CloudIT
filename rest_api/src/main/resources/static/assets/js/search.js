var base_api_url = "http://" + window.location.host;

function AppViewModel() {
    var self=this;
    self.jobOffers=ko.observableArray([]);
    self.users=ko.observableArray([]);
    self.searchBy = ko.observable("users");

    self.refresh = function(){
        $.ajax({
            url: base_api_url+"/joboffer",
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

ko.applyBindings(appViewModel);

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

/**
 * Updates information on variables for the clicked user
 */
function fillSearchUserModalTable(name, userType, email, jobs) {
    $("#userModalTitle").text(name);
    $("#userModalEmail").text(email);

    appViewModel.userModalUserType(userType.toLowerCase());
    appViewModel.userModalJobs.removeAll();
    jobs.forEach(function (job, _) {
        appViewModel.userModalJobs.push(job);
    })
}

function changeFilterMenu() {
    console.log();
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

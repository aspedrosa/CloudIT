var base_api_url = "http://" + window.location.host;

function AppViewModel() {
    let self = this;

    self.favourites = ko.observableArray([]);

    self.getFavourites = function() {
        $.ajax({
            url: base_api_url + "/favourite",
            type: "GET",
            contentType: "application/json",
            crossDomain: true,
            success: function (data, status, xhr) {
                console.log(data);
                data.data.forEach(function (job) {
                    self.favourites.push(job);
                });
            },
            error: function (data, status, xhr) {
                console.log(data.responseText);
                console.log(status);

                alert(data.responseJSON.message);
            }
        });
    }
}

var appViewModel = new AppViewModel();
appViewModel.getFavourites();
ko.applyBindings(appViewModel);

window.onload = function() {
    let token = localStorage.getItem("token");
    $("#welcome_title").html("Welcome "+localStorage.getItem("username")+"!");
};

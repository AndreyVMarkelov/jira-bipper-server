AJS.toInit(function () {
    var username = window.location.toString().split("name=")[1];
    if (username == null) {
        username = AJS.$("#up-d-username").text().trim();
    }

    var validationUrl = AJS.contextPath() + "/rest/bipper/1.0/phonenumbers";
    var dataUrl = AJS.contextPath() + "/rest/api/2/user/properties/bipper.phone?username=" + username;

    function populateForm() {
        AJS.$.ajax({
            url: validationUrl + "?userName=" + username,
            dataType: "json"
        }).done(function (data) {
            AJS.$("#up-bipper-phone").text(data.number);
            AJS.$("#update-notification-preferences-phone").val(data.number);
            AJS.$("#error").text("");
        });
    }

    function updateConfig() {
        alert("ded");
        AJS.$.ajax({
            url: validationUrl +  "?userName=" + username,
            type: "POST",
            contentType: "application/json",
            data: "\"" + AJS.$("#update-notification-preferences-phone").val() + "\"",
            processData: false
        }).done(function (config) {
            if (config.message) {
                AJS.$("#error").text(config.message);
                AJS.$("#error").show();
            } else {
                AJS.$("#up-bipper-phone").text(config.number);
                AJS.dialog2("#bipper-user-profile-dialog").hide();
            }
        });
    }

    AJS.$("#bipper-dialog-save").click(function (e) {
        e.preventDefault();
        updateConfig();
    });

    AJS.$("#bipper-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-profile-dialog").hide();
    });

    AJS.$("#edit-bipper-profile-link").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-profile-dialog").show();
    });

    populateForm();
});

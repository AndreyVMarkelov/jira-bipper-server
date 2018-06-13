AJS.toInit(function () {
    function updatePhone() {
        alert(AJS.$("#up-bipper-name").val());
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/bipper/1.0/phonenumbers" +  "?userName=" + AJS.$("#up-bipper-name").val(),
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
        updatePhone();
    });

    AJS.$("#bipper-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-profile-dialog").hide();
    });

    AJS.$("#edit-bipper-profile-link").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-profile-dialog").show();
    });
});

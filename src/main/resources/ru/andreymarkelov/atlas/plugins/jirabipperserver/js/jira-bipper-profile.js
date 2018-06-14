AJS.toInit(function () {
    function updatePhone() {
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/bipper/1.0/phonenumbers",
            type: "PUT",
            dataType : "json",
            contentType: "application/json",
            data: JSON.stringify({key: AJS.$("#up-bipper-key").val(), phone: AJS.$("#up-bipper-new-phone").val()}),
            processData: false,
            success: function () {
                AJS.$("#up-bipper-phone").text(AJS.$("#up-bipper-new-phone").val());
                AJS.dialog2("#bipper-user-profile-dialog").hide();
                AJS.$("#bipper-action-dialog").removeClass("hidden");
            },
            error: function (error) {
                if (error.responseText) {
                    AJS.$("#error").text(error.responseText);
                    AJS.$("#error").show();
                }
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
        AJS.$("#up-bipper-new-phone").val(AJS.$("#up-bipper-phone").text());
        AJS.$("#error").text("");
        AJS.dialog2("#bipper-user-profile-dialog").show();
    });
});

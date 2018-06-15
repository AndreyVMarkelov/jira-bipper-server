AJS.toInit(function () {
    function updateAccountSetup() {
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/bipper/1.0/admin",
            type: "PUT",
            dataType : "json",
            contentType: "application/json",
            data: JSON.stringify({sender: "dd"}),
            processData: false,
            success: function () {
                AJS.dialog2("#bipper-user-admin-dialog").hide();
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
        updateAccountSetup();
    });

    AJS.$("#bipper-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-admin-dialog").hide();
    });

    AJS.$("#edit-bipper-admin-link").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-admin-dialog").show();
    });
});

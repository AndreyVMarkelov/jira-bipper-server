AJS.toInit(function () {
    function setupKeySetup() {
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/bipper/1.0/admin/savekey",
            type: "PUT",
            dataType : "json",
            contentType: "application/json",
            data: JSON.stringify({sender: AJS.$("#up-bipper-setup-sendername").val(), apiKey: AJS.$("#up-bipper-setup-apikey").val()}),
            processData: false,
            success: function () {
                AJS.dialog2("#bipper-admin-setup-dialog").hide();
//                AJS.$("#bipper-admin-setup-dialog").removeClass("hidden");
            },
            error: function (error) {
                if (error.responseText) {
                    AJS.$("#error").text(error.responseText);
                    AJS.$("#error").show();
                }
            }
        });
    }

    function updateAccountSetup1() {
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

    AJS.$("#bipper-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-admin-dialog").hide();
    });

    AJS.$("#edit-bipper-admin-link").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#bipper-user-admin-dialog").show();
    });

    // Setup
    AJS.$("#edit-bipper-admin-key-link").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#bipper-admin-setup-dialog").show();
    });

    AJS.$("#bipper-setup-dialog-save").click(function (e) {
        e.preventDefault();
        setupKeySetup();
    });

    AJS.$("#bipper-setup-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#bipper-admin-setup-dialog").hide();
    });
});

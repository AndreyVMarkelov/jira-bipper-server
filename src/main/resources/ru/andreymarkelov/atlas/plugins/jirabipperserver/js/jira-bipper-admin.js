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
                AJS.$("#senderName-value").text(AJS.$("#up-bipper-setup-sendername").val());
                AJS.$("#apiKey-value").text(AJS.$("#up-bipper-setup-apikey").val());
                AJS.$("#generationTime-value").text("");
                AJS.$("#generationTime-block").hide();
            },
            error: function (error) {
                if (error.responseText) {
                    AJS.$("#setup-error").text(error.responseText);
                    AJS.$("#setup-error").removeClass("hidden");
                }
            }
        });
    }

    function generateKeySetup() {
        AJS.$.ajax({
            url: AJS.contextPath() + "/rest/bipper/1.0/admin/generatekey",
            type: "PUT",
            dataType : "json",
            contentType: "application/json",
            data: JSON.stringify({sender: AJS.$("#up-bipper-generate-sendername").val(), accountId: AJS.$("#up-bipper-generate-accountid").val(), password: AJS.$("#up-bipper-generate-accountpass").val()}),
            processData: false,
            success: function (response) {
                AJS.$("#senderName-value").text(response.sender);
                AJS.$("#apiKey-value").text(response.apiKey);
                AJS.$("#generationTime-value").text(response.generationTime);
                AJS.$("#generationTime-block").show();
                AJS.dialog2("#bipper-admin-generate-dialog").hide();
            },
            error: function (error) {
                if (error.responseText) {
                    AJS.$("#generate-error").text(error.responseText);
                    AJS.$("#generate-error").removeClass("hidden");
                }
            }
        });
    }

    // Setup
    AJS.$("#edit-bipper-admin-key-link").click(function(e) {
        e.preventDefault();
        AJS.$("#up-bipper-setup-sendername").val(AJS.$("#senderName-value").text());
        AJS.$("#up-bipper-setup-apikey").val(AJS.$("#apiKey-value").text());
        AJS.$("#setup-error").addClass("hidden");
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

    // Generate
    AJS.$("#edit-bipper-admin-password-link").click(function(e) {
        e.preventDefault();
        AJS.$("#up-bipper-generate-sendername").val(AJS.$("#senderName-value").text());
        AJS.$("#up-bipper-generate-accountid").val("");
        AJS.$("#up-bipper-generate-accountpass").val("");
        AJS.$("#generate-error").addClass("hidden");
        AJS.dialog2("#bipper-admin-generate-dialog").show();
    });

    AJS.$("#bipper-generate-dialog-save").click(function (e) {
        e.preventDefault();
        generateKeySetup();
    });

    AJS.$("#bipper-generate-dialog-close").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#bipper-admin-generate-dialog").hide();
    });
});

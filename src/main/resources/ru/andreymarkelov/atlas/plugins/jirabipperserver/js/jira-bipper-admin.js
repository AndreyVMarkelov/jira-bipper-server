AJS.toInit(function () {
    function updateAccountSetup() {
        alert("1");
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

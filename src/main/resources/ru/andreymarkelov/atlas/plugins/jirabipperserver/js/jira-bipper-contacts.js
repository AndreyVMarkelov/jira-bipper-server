AJS.toInit(function () {
    var contactsTable = new AJS.RestfulTable({
        autoFocus: false,
        allowReorder: false,
        el: AJS.$("#up-bipper-contacts-table"),
        resources: {
            all: AJS.contextPath() + "/rest/bipper/1.0/contacts/all",
            self: AJS.contextPath() + "/rest/bipper/1.0/contacts/self"
        },
        columns: [
            {
                id: "name",
                header: AJS.I18n.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.admin.contacts.table.user"),
                allowEdit: true,
                allowCreate: true
            },
            {
                id: "phone",
                header: AJS.I18n.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.admin.contacts.table.phone"),
                allowEdit: true,
                allowCreate: true
            }
        ]
    });
});

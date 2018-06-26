AJS.toInit(function () {
    function setupRecipient(selectedValue) {
        switch (selectedValue) {
            case "1":
                AJS.$("#userFieldValue").show();
                AJS.$("#groupFieldValue").hide();
                AJS.$("#userValue").hide();
                AJS.$("#phoneValue").hide();
                break;
            case "2":
                AJS.$("#userFieldValue").hide();
                AJS.$("#groupFieldValue").show();
                AJS.$("#userValue").hide();
                AJS.$("#phoneValue").hide();
                break;
            case "3":
                AJS.$("#userFieldValue").hide();
                AJS.$("#groupFieldValue").hide();
                AJS.$("#userValue").show();
                AJS.$("#phoneValue").hide();
                break;
            case "4":
                AJS.$("#userFieldValue").hide();
                AJS.$("#groupFieldValue").hide();
                AJS.$("#userValue").hide();
                AJS.$("#phoneValue").show();
                break;
        }
    }

    var userField = AJS.$("#userFieldCurrentValue").val();
    var groupField = AJS.$("#groupFieldCurrentValue").val();

    var userFieldSelect = AJS.$("#userFieldValue");
    userFieldSelect.append(AJS.$('<option>', {
        value: '',
        text: 'Please select...'
    }));
    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/bipper/1.0/postfunction/issueusers",
        dataType: "json"
    }).done(function(data) {
        AJS.$.each(data, function(key, value) {
            userFieldSelect.append(AJS.$('<option>', {
                value: value.key,
                text: value.value,
                selected: (value.key === userField)
            }));
        });
    });

    var groupFieldSelect = AJS.$("#groupFieldValue");
    groupFieldSelect.append(AJS.$('<option>', {
        value: '',
        text: 'Please select...'
    }));
    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/bipper/1.0/postfunction/issuegroups",
        dataType: "json"
    }).done(function(data) {
        AJS.$.each(data, function(key, value) {
            groupFieldSelect.append(AJS.$('<option>', {
                value: value.key,
                text: value.value,
                selected: (value.key === groupField)
            }));
        });
    });

    AJS.$("#recipientType").on('change', function () {
        setupRecipient(AJS.$('#recipientType option:selected').val());
    });
    setupRecipient(AJS.$("#recipientType").val());
});

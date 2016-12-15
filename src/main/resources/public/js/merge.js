$(document).ready(function() {
    
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/topicmap",
        dataType: "json",
        contentType: "application/json",
        data: '{"topics":[{"item.identifiers":["TestMerge"]}], "locator":"frederic"}'
    }).then(function(data) {
       $('#tm').append(data);
    });
});
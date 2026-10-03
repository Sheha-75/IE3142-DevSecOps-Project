$(document).ready(function () {
    $("#postComment").on("click", function () {
        var commentInput = $("#commentInput").val();
        $.ajax({
            type: 'POST',
            url: 'CrossSiteScriptingStored/stored-xss',
            data: JSON.stringify({text: commentInput}),
            contentType: "application/json",
            dataType: 'json'
        }).then(
            function () {
                getChallenges();
                $("#commentInput").val('');
            }
        )
    })

    getChallenges();

    function getChallenges() {
        $("#list").empty();
        $.get('CrossSiteScriptingStored/stored-xss', function (result, status) {
            for (var i = 0; i < result.length; i++) {
                // API values are untrusted text. Build structure from constants and insert
                // each value with .text() so it cannot become executable HTML.
                var comment = $("<li>").addClass("comment");
                var avatarContainer = $("<div>").addClass("pull-left");
                var avatar = $("<img>")
                    .addClass("avatar")
                    .attr("src", "images/avatar1.png")
                    .attr("alt", "avatar");
                var commentBody = $("<div>").addClass("comment-body");
                var heading = $("<div>").addClass("comment-heading");
                var user = $("<h4>").addClass("user").text(result[i].user);
                var time = $("<h5>").addClass("time").text(result[i].dateTime);
                var text = $("<p>").text(result[i].text);

                avatarContainer.append(avatar);
                heading.append(user, time);
                commentBody.append(heading, text);
                comment.append(avatarContainer, commentBody);
                $("#list").append(comment);
            }

        });
    }
})


<td><%= request.getParameter("first_name") %></td>
<td><%= request.getParameter("last_name") %></td>


function checkIsLiked(posts) {

    for (let i = 0; i < posts.length; i++) {
        const url = "http://localhost:8080/api/likes/check-is-liked/" + posts[i].id;

        $.ajax({
                type: "GET",
                url: url,
                success: function (like) {
                    if (like.post != null) {
                        document.getElementById(posts[i].id).innerHTML = `<i class="fa fa-thumbs-down"></i>`;
                    }
                }
            }
        );
    }
}


function like(postId) {

    console.log(postId);
    let dynamicURL = "http://localhost:8080/api/likes/like-post/" + postId;

    $.ajax({
        url: dynamicURL,
        method: "PUT",
        success: function (like) {

            if (like.post != null) {
                document.getElementById(postId).innerHTML = `<i class="fa fa-thumbs-down"></i>`;
            } else {
                document.getElementById(postId).innerHTML = `<i class="fa fa-thumbs-up"></i>`;
            }
        },
        error: function () {
            alert("ops");
        }
    })
}

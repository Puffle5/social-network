$(document).ready(function () {
    const url = "http://localhost:8080/api/posts";

    $.ajax({
        type: "GET",
        url: url,
        success: getPosts,
        error: console.log.bind(console, "error in displaying all posts")
    });

    function getPosts(posts) {

        let allPosts = Object.values(posts);
       

        for (let i = 0; i < allPosts.length; i++) {
            let post;
            let postId = allPosts[i].id;
            let postText = allPosts[i].description;
            let postTitle = allPosts[i].title;
            let postPicture = allPosts[i].photoURL;
            let postDate = allPosts[i].date;

            let datee = new Date(postDate.toString());
            post = `
            
            <div id="${postId}" class="w3-container w3-card w3-white w3-margin">
            <br>
                <img  src="/images/${postPicture}"  src="image.jpg" alt="Avatar" style="width:60px">
                <span class="w3-right w3-opacity"> ${datee.toLocaleString()}</span>
                <h4 >${postTitle}</h4>
            <br>
                <hr class="w3-clear">
                <p >${postText}</p>
               <br>
               <a><button class="btn btn-primary pull-right" onclick="deletePost(${postId})">Delete</button></a></td>
               <br>
            </div>`;

            $('#container').append(post);
        }
    }
});

function deletePost(postId){

    const url = "http://localhost:8080/api/posts/delete-post/"+postId;

    $.ajax({
        type: "DELETE",
        url: url,
        success: function () {
            document.getElementById(postId).remove();
        },
        error: console.log.bind(console, "error in deleting post.")
    });
}

$(document).ready(function () {
    const url = "http://localhost:8080/api/users/without-login-user";

    $.ajax({
        type: "GET",
        url: url,
        success: getUsers,
        error: console.log.bind(console, "error in displaying all users")
    });

    function getUsers(users) {

        let allUsers = Object.values(users);
       

        for (let i = 0; i < allUsers.length; i++) {
            let username = allUsers[i].username;
            let userPicture = allUsers[i].photoURL;

            user = `
<div class="w3-card w3-white" >
    <div class="w3-container">
        <p class="w3-center"><img src="/images/${userPicture}"  src="image.jpg" class="w3-circle" style="height:106px;width:106px" alt="Avatar"></p>
        <hr>
        <h1 class="fa fa-fw w3-margin-right w3-text-theme"><span">${username}</span></h1>
        <a href="/profile/user/delete/${username}"><button class="btn btn-primary pull-right">Delete</button></a></td>
            
    </div>
</div>
<br>`;

            $('#users-container').append(user);
        }
    }
});


$(document).ready(function () {
    const url = "http://localhost:8080/api/comments";

    $.ajax({
        type: "GET",
        url: url,
        success: getComment,
        error: console.log.bind(console, "error in displaying all users")
    });

    function getComment(comments) {

        let allComment = Object.values(comments);

        console.log(allComment);

        for (let i = 0; i < allComment.length; i++) {

            let datee = new Date(allComment[i].date.toString());

            commentss = ` 
            <div  class="w3-container w3-card w3-white w3-margin">
            <br>
                <img  src="/images/${allComment[i].user.photoURL}"  src="image.jpg" alt="Avatar" style="width:60px">
               <div><h6>${allComment[i].user.username}</h6></div>
                <span class="w3-right w3-opacity"> ${datee.toLocaleString()}</span>
                <h4 >${allComment[i].content}</h4>
            <br>
                <a href="/comment/delete/${allComment[i].id}"><button class="btn btn-primary pull-right">Delete</button></a></td>
               <br>
            </div> `;

            $('#comments-container').append(commentss);
        }
    }
});

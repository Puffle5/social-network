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
        console.log(allUsers);
        console.log(allUsers.length);


        for (let i = 0; i < allUsers.length; i++) {
            let user;
            let username = allUsers[i].username;
            let userPicture = allUsers[i].photoURL;


            user = `
                            <div id="nearby"class="nearby-user fadeInLeft">
                                <div class="row fullWidth">
                                    <div class="col-md-2 col-sm-2">
                                        <img src="/images/${userPicture}" alt="user"
                                             class="profile-photo-lg">
                                    </div>
                                    <div class="col-md-3 col-sm-3 textFieldNearbyUser">
                                        <h5><a href="/profile/user/${username}" class="profile-link">${username}</a></h5>
                                    </div>
                                </div>
                            </div>`;

            $('#all-users').append(user);
        }
    }
});

$(document).ready(function () {
    $("#myInput").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#all-users #nearby").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

$(document).ready(function () {

    const url = "http://localhost:8080/api/posts/friends/posts";

    $.ajax({
        type: "GET",
        url: url,
        success: function connect(posts) {

            let allOtherPosts = Object.values(posts);
            allOtherPosts.sort((a,b) => (a.id < b.id) ? 1 : ((b.id < a.id) ? -1 : 0));

            for (let i = 0; i < allOtherPosts.length; i++) {

                 let date = new Date(allOtherPosts[i].date.toString());

                post = `<div class="w3-container w3-card w3-white w3-margin fadeIn"><br>
                <img src="/images/${allOtherPosts[i].user.photoURL}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
                <span class="w3-right w3-opacity">${date.toLocaleString()}</span>
                <h4>${allOtherPosts[i].user.username}</h4>
                <br/>
                <hr class="w3-clear">
                <p>${allOtherPosts[i].title}</p><br>
                <p>${allOtherPosts[i].description}</p>
                <div class="rounded-circle">
                <img src="/images/${allOtherPosts[i].photoURL}"  style='height: 100%; width: 100%; object-fit: contain'>
                </div>
                <br>`;
                post += `<button type="button"  id="${allOtherPosts[i].id}" onclick="like(${allOtherPosts[i].id})" class="w3-button w3-theme-d1 w3-margin-bottom" ><i class="fa fa-thumbs-up"></i></button>`

                post += `<button type="button" class="w3-button w3-theme-d2 w3-margin-bottom" value="${allOtherPosts[i].id}" onclick="showCommentsPosts(${allOtherPosts[i].id})" data-toggle="modal" data-target="#myCommentModalHomePage"><i class="fa fa-comment"></i> Comment</button>
               </div>
               
                        
    <div class="modal fade" id="myCommentModalHomePage" role="dialog" tabindex="-1">
                                <div class="modal-dialog" role="document">
                                  <div class="modal-content">
                                    <div class="modal-header">
                                      <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                        <h4 class="modal-title">Add Comment</h4>
                                          </div>
                                            <div class="modal-body">
                                            
                                            
                                            
                                             <div class="form-group" >                                                                                     
                                                           
                                                           <div class="form-group" id="comments-public-posts">
                                                           
                                                
                             
                                                            </div>
                                                                                  
                                              <form class="form-horizontal" id="post-comment"> 
                                                
                                                
                                                
                                               <div class="form-group">
                                                  <label for="post-comment" class="control-label col-md-4">Post Comment</label>
                                                    <div class="col-md-8">
                                                      <textarea cols="" rows="5" id="comment-description" class="form-control"></textarea><!--th:name="description"-->
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                  <div class="col-md-offset-4 col-md-8">
                                                    <input class="btn btn-primary" onclick="comment()" value="Create"/>
                                                  </div>
                                                </div>


                                              </form>
                                              
                                            </div>
                                  </div>
                                </div>
                              </div>`;

                $('#public-posts').append(post);
            }
            checkIsLiked(posts);
        },
    });

});

let  buttonValue;

function showCommentsPosts(postId) {

    buttonValue = postId;

    $("#comments-public-posts > li").remove();

    const url = "http://localhost:8080/api/comments/get-post-comments/" + postId;

    $.ajax({
            type: "GET",
            url: url,
            success: function (comments) {

                let allCommentsPost = Object.values(comments);

                for (let i = 0; i < allCommentsPost.length; i++) {

                    let datee = new Date(allCommentsPost[i].date.toString());

                    commentt = `<li class="media">
                            <a class="pull-left">
                                <img src="/images/${allCommentsPost[i].user.photoURL}" alt="${allCommentsPost[i].user.username}" class="img-circle"
                                style="max-height: 50px; max-width: 50px;">
                            </a>
                            <div class="media-body">
                                <span class="text-muted pull-right">
                                    <small class="text-muted">${datee.toLocaleString()}</small>
                                </span>
                                <strong class="text-success">${allCommentsPost[i].user.username}</strong>
                                <p>
                                    ${allCommentsPost[i].content}
                                </p>
                            </div>
                        </li>
                            `;


                    $('#comments-public-posts').append(commentt);
                }
            }
        }
    );

}

function comment() {

    const url = "http://localhost:8080/api/comments/create-comment/" + buttonValue;

    let commentValue = document.getElementById("comment-description").value;

    let comment = {
        "description": commentValue
    };

    fetch(url, {
        method: "POST",
        body: JSON.stringify(comment),
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json, text/plain, */*"
        }
    }).then((res) => res.json())
        .then(function (readyComment) {

            document.getElementById("comment-description").value = "";
            let datee = new Date(readyComment.date.toString());

            comment = `<li class="media">
                            <a class="pull-left">
                                <img src="/images/${readyComment.user.photoURL}" alt="${readyComment.user.username}" class="img-circle"
                                style="max-height: 50px; max-width: 50px;">
                            </a>
                            <div class="media-body">
                                <span class="text-muted pull-right">
                                    <small class="text-muted">${datee.toLocaleString()}</small>
                                </span>
                                <strong class="text-success">${readyComment.user.username}</strong>
                                <p>
                                    ${readyComment.content}
                                </p>
                            </div>
                        </li>
                             <br>`;


            $('#comments-public-posts').append(comment);

        })
}


$(document).ready(function () {

    $("#upload-post-news").click(function (event) {

        event.preventDefault();
        createNewsPost();
    });

});


function createNewsPost() {

    let title = document.getElementById("post-title-news").value;
    let description = document.getElementById("post-description-news").value;

    let radios = document.getElementsByName('status');

    let radioValue;
    for (let i = 0, length = radios.length; i < length; i++) {
        if (radios[i].checked) {
            radioValue = radios[i].value;
            break;
        }
    }

    if (radioValue === undefined) {
        alert("Choose post visibility.");
        return;
    }

    if (!title) {
        alert("Submit Title!");
        return;
    }

    if (!description) {
        alert("Submit Title!");
        return;
    }
    let url = "http://localhost:8080/api/posts/uploadAction/post/" + title + "/" + description + "/" + radioValue;

    let form = $('#post-picture-news')[0];

    let data = new FormData(form);

    data.append("CustomField", "This is some extra data, testing");

    $("#upload-post").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: url,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (post) {

            document.getElementById("post-title-news").value = "";
            document.getElementById("post-description-news").value = "";

            let datee = new Date(post.date.toString());

            postForFronEnd = `<div class="w3-container w3-card w3-white w3-margin"><br>
                <img src="/images/${post.user.photoURL}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
                <span class="w3-right w3-opacity">${datee.toLocaleString()}</span>
                <h4>${post.user.username}</h4>
                <br/>
                <hr class="w3-clear">
                 <p>${post.title}</p><br>
                <p>${post.description}</p>
                <div class="rounded-circle">
                <img src="/images/${post.photoURL}"  style='height: 100%; width: 100%; object-fit: contain'>
                </div>
                <br>`;

            postForFronEnd += `<button type="button" id="${post.id}"  onclick="like(${post.id})" class="w3-button w3-theme-d1 w3-margin-bottom"><i class="fa fa-thumbs-up"></i></button>`

            postForFronEnd += `<button type="button" class="w3-button w3-theme-d2 w3-margin-bottom" value="${post.id}" onclick="showCommentsPosts(${post.id})" data-toggle="modal" data-target="#myCommentModalHomePage"><i class="fa fa-comment"></i> Comment</button>
               </div>`;

            $('#home-page-create-post').modal('hide');
            $('#public-posts').prepend(postForFronEnd);

            $("#result").text(data);
            $("#upload-post-news").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);

            $("#upload-post-news").prop("disabled", false);
            alert("Upload picture!");

        }
    });

}

var logo = document.getElementById('illLogo');

logo.onmouseout = function () {
    this.src = '/../images/logo_invert_small.png';
};

logo.onmouseover = function () {
    this.src = '/../images/logo_invert_small_bright.png';
};

var scrollTimeOut = true,
    lastYPos = 0,
    yPos = 0,
    yPosDelta = 5,
    nav = $('nav.navbar'),
    navHeight = nav.outerHeight(),
    setNavClass = function() {
        scrollTimeOut = false;
        yPos = $(window).scrollTop();

        if(Math.abs(lastYPos - yPos) >= yPosDelta) {
            if (yPos > lastYPos && yPos > navHeight){
                nav.addClass('hide-nav');
            } else {
                nav.removeClass('hide-nav');
            }
            lastYPos = yPos;
        }
    };

$(window).scroll(function(e){
    scrollTimeOut = true;
});

setInterval(function() {
    if (scrollTimeOut) {
        setNavClass();
    }

}, 250);
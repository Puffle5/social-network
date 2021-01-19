$(document).ready(function () {
    const url = "http://localhost:8080/api/request/personal-friend-requests";

    $.ajax({
        type: "GET",
        url: url,
        success: getUsers,
        error: console.log.bind(console, "error in displaying all requests")
    });

    function getUsers(users) {

        let allUsers = Object.values(users);

        for (let i = 0; i < allUsers.length; i++) {
            let user;
            let username = allUsers[i].username;
            let userPicture = allUsers[i].photoURL;

            user = `<div id="${username}" class="w3-card w3-white w3-center fadeInRight">
    <div class="w3-container">
        <p>Friend Request</p>
        <img src="/images/${userPicture}" alt="Avatar" style="width:50%"><br>
            <span>${username}</span>
            <div class="w3-row w3-opacity">
                <div class="w3-half">
                    <button id="accept" value="${username}" onclick="accept()" class="w3-button w3-block w3-green w3-section" title="Accept"><i
                        class="fa fa-check"></i></button>
                </div>
                <div class="w3-half">
                    <button id="decline" value="${username}" onclick="decline()" class="w3-button w3-block w3-red w3-section" title="Decline"><i
                        class="fa fa-remove"></i></button>
                </div>
            </div>
    </div>
</div><br>`;

            $('#requests').append(user);
        }
    }
});

function accept() {
    let username = $('#accept').val();
    //console.log(username)
    let dynamicURL = "http://localhost:8080/api/request/accept/" + username;
    //console.log(dynamicURL)

    $.ajax({
        url: dynamicURL,
        method: "PUT",
        success: function (user) {
            let element = document.getElementById(username);
            element.parentNode.removeChild(element);

            friend = `<div class="nearby-user">
                                <div class="row">
                                    <div class="col-md-2 col-sm-2">
                                        <img src="/images/${user.photoURL}" alt="user"
                                             class="profile-photo-lg">
                                    </div>
                                    <div class="col-md-3 col-sm-3">
                                        <a href="/profile/user/${user.username}" class="profile-link">${user.username}</a>
                                    </div>
                                </div>
                            </div>`;

            $('#friends').append(friend);

        },
        error: function (error) {
            alert("ops");
        }
    })
}


function decline() {
    let username = $('#decline').val();

    let dynamicURL = "http://localhost:8080/api/request/decline/" + username;

    $.ajax({
        url: dynamicURL,
        method: "PUT",
        success: function () {
            let element = document.getElementById(username);
            element.parentNode.removeChild(element);
        },
        error: function (error) {
            alert("ops");
        }
    })
}

function getRandomInt(max) {
    return Math.floor(Math.random() * (max - 0 + 1)) + 0;
}

$(document).ready(function () {
    const url = "http://localhost:8080/api/request/personal-friend";

    $.ajax({
        type: "GET",
        url: url,
        success: getFriends,
        error: noFriends
    });

    function noFriends() {
        friend = `<div class="fadeInLeft"  style="color: #d4d4d4;">
                                        No friends yet :( <br> Time to make some!
                                        </div>`;
        $("#noFriends").append(friend);

    }

    function getFriends(friends) {
        let allFriend = Object.values(friends);

        let n = "1";
        let i = "1";
        let row = "1";
        let cont = 0;
        let friendsAdded = [];
        let maxFriends = allFriend.length;
        if (maxFriends >= 9) {
            while (n <= 9) {
                let counter = 1;
                cont = 0;
                i = getRandomInt(maxFriends - 1);
                do {
                    if (friendsAdded.includes(i)) {
                        cont = 0;
                        if (i >= (maxFriends - 1)) {
                            i = getRandomInt(maxFriends - 1);
                        } else {
                            i++;
                        }
                    } else {
                        cont = 1;
                    }
                    counter++;
                } while (cont == 0)
                friendsAdded.push(i);

                let username = allFriend[i].username;
                let userPicture = allFriend[i].photoURL;
                friend = `<div class="fadeInLeft">
                                        <a href="/profile/user/${username}" class="profile-link"><img src="/images/${userPicture}" alt="user"
                                             class="profile-photo-lg"></a>
                                        </div>`;

                $("#imagesRow" + row).append(friend);

                if (n == 4 || n == 7) {
                    row++;
                }
                n++;
            }
        } else if (maxFriends < 9 && maxFriends > 0) {
            for (let j = 0; j < n; j++) {
                let username = allFriend[j].username;
                let userPicture = allFriend[j].photoURL;
                friend = `<div class="fadeInLeft">
                <a href="/profile/user/${username}" class="profile-link"><img src="/images/${userPicture}" alt="user"
                                             class="profile-photo-lg"></a>
                                        </div>`;

                $("#imagesRow" + row).append(friend);
                if (n == 4 || n == 7) {
                    row++;
                }
                n++;
            }
        } else if (maxFriends <= 0) {
            friend = `<div class="fadeInLeft" style="color: darkred;">
                                        No friends yet :( <br> Time to make some!
                                        </div>`;
            $("#noFriends").append(friend);
        }

        /*
        for (let i = 0; i < allFriend.length; i++) {



            $('#friends').append(friend);

        }*/
    }
});


$(document).ready(function () {

    const url = "http://localhost:8080/api/posts/personal-posts";


    $.ajax({
        type: "GET",
        url: url,
        success: getPosts,
        error: console.log.bind(console, "error in displaying all Posts")
    });


    function getPosts(posts) {

        let allPosts = Object.values(posts);

        let user = allPosts[0].user;

        for (let i = 0; i < allPosts.length; i++) {

            let date = new Date(allPosts[i].date.toString());

            post = `<div class="w3-container w3-card w3-white w3-margin fadeIn"><br>
                <img src="images/${user.photoURL}" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">
                <span class="w3-right w3-opacity">${date.toLocaleString()}</span>
                <h4>${user.username}</h4>
                <br/>
                <hr class="w3-clear">
                <p>${allPosts[i].title}</p><br>
                <p>${allPosts[i].description}</p>
                <div class="rounded-circle">
                <img src="images/${allPosts[i].photoURL}"  style='height: 100%; width: 100%; object-fit: contain'>
                </div>
                <br>`;
            post += `<button type="button"  id="${allPosts[i].id}" onclick="like(${allPosts[i].id})" class="post-button w3-theme-d1 w3-margin-bottom" ><i class="fa fa-thumbs-up"></i></button>`

            post += `<button type="button" class="post-button w3-theme-d2 w3-margin-bottom" value="${allPosts[i].id}" onclick="showCommentsPosts(${allPosts[i].id})" data-toggle="modal" data-target="#myCommentModal"><i class="fa fa-comment"></i> Comment </button>
               </div>
               
              
    <div class="modal fade" id="myCommentModal" role="dialog" tabindex="-1">
                                <div class="modal-dialog" role="document">
                                  <div class="modal-content">
                                    <div class="modal-header">
                                      <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                        <h4 class="modal-title">Add Comment</h4>
                                          </div>
                                            <div class="modal-body">
                                            
                                            
                                            
                                             <div class="form-group" >                                                                                     
                                                           
                                                           <div class="form-group" id="comments-post">
                                                           
                                                
                             
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
                                                    <input class="btn btn-primary" onclick="comment()" id ="${allPosts[i].id}"value="Create"/>
                                                  </div>
                                                </div>


                                              </form>
                                              
                                            </div>
                                  </div>
                                </div>
                              </div>`;

            $('#personal-posts').append(post);

        }

        checkIsLiked(posts)
    }
});

let buttonValue;

function showCommentsPosts(postId) {

    buttonValue = postId;

    $("#comments-post > li").remove();

    console.log(postId);
    const url = "http://localhost:8080/api/comments/get-post-comments/" + postId;
    console.log(url);

    $.ajax({
            type: "GET",
            url: url,
            success: function (comments) {

                let allCommentsPost = Object.values(comments);
                console.log(allCommentsPost);

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


                    $('#comments-post').append(commentt);
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


            $('#comments-post').append(comment);

        })

}

$(document).ready(function () {

    $("#upload-profile").click(function (event) {

        event.preventDefault();

        uploadProfilePicture();

    });

});


$(document).ready(function () {

    $("#upload-post-picture-news").click(function (event) {

        event.preventDefault();
        createPost();
    });

});


function createPost() {
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

    let title = document.getElementById("post-title-news").value;
    if (!title) {
        alert("Submit Title!");
        return;
    }
    let description = document.getElementById("post-description-news").value;


    if (!description) {
        alert("Submit Description!");
        return;
    }

    //Same as above:
    //description || alert("Submit Description!"); return;

    //Same as above:
    //description = description || "";


    let url = "http://localhost:8080/api/posts/uploadAction/post/" + title + "/" + description + "/" + radioValue;

    let form = $('#post-picture')[0];

    let data = new FormData(form);

    console.log("here is the data");
    console.log(data);
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

            postForFronEnd += `<button type="button" class="w3-button w3-theme-d2 w3-margin-bottom" value="${post.id}" onclick="showCommentsPosts(${post.id})" data-toggle="modal" data-target="#myCommentModal"><i class="fa fa-comment"></i> Comment</button>
               </div>`;

            $('#myPostModal').modal('hide');
            $('#personal-posts').prepend(postForFronEnd);

            $("#result").text(data);
            $("#upload-post-picture-news").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#upload-post-picture-news").prop("disabled", false);
            alert("Upload picture!");

        }
    });

}

function playAudio(url) {
    var audio = document.createElement('audio');
    audio.src = url;
    audio.play();
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
    setNavClass = function () {
        scrollTimeOut = false;
        yPos = $(window).scrollTop();

        if (Math.abs(lastYPos - yPos) >= yPosDelta) {
            if (yPos > lastYPos && yPos > navHeight) {
                nav.addClass('hide-nav');
            } else {
                nav.removeClass('hide-nav');
            }
            lastYPos = yPos;
        }
    };

$(window).scroll(function (e) {
    scrollTimeOut = true;
});

setInterval(function () {
    if (scrollTimeOut) {
        setNavClass();
    }

}, 250);

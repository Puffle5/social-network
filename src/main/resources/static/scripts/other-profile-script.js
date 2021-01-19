function check(username) {
    const url = "http://localhost:8080/api/request/user/check-request/" + username;

    $.ajax({
        type: "GET",
        url: url,
        success: function checkConnect(request) {

            if (request.firstUser != null) {
                document.getElementById("button-accept-decline").innerHTML = " Disconnect ";
            } else if (request.firstUser == null && request.secondUser != null) {
                $("#button-accept-decline").hide();
            } else {
                document.getElementById("button-accept-decline").innerHTML = " Connect ";
            }

        },
        error: document.getElementById("button-accept-decline").innerHTML = " Wait Response. "
    });
}

function connectOrDisconnect(username) {

    const url = "http://localhost:8080/api/request/user/send-request/" + username;

    $.ajax({
        type: "GET",
        url: url,
        success: function connect(request) {
            console.log(request);
            if (request.firstUser != null) {
                document.getElementById("button-accept-decline").innerHTML = " Disconnect ";
            } else {
                document.getElementById("button-accept-decline").innerHTML = " Connect ";
            }
        },
        error: document.getElementById("button-accept-decline").innerHTML = " Wait Response."
    });
}


function publicOtherPersonPosts(userId) {

    const url = "http://localhost:8080/api/posts/user/" + userId;

    $.ajax({
        type: "GET",
        url: url,
        success: function connect(posts) {

            let allOtherPosts = Object.values(posts);


            for (let i = 0; i < allOtherPosts.length; i++) {

                let date = new Date(allOtherPosts[i].date.toString());

                post = `<div class="w3-container w3-card w3-white w3-margin"><br>
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

                post += `<button type="button" class="w3-button w3-theme-d2 w3-margin-bottom fadeIn" value="${allOtherPosts[i].id}" onclick="showCommentsPosts(${allOtherPosts[i].id})" data-toggle="modal" data-target="#myCommentModalOtherPersons"><i class="fa fa-comment"></i> Comment</button>
               </div>
               
                        
    <div class="modal fade" id="myCommentModalOtherPersons" role="dialog" tabindex="-1">
                                <div class="modal-dialog" role="document">
                                  <div class="modal-content">
                                    <div class="modal-header">
                                      <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                        <h4 class="modal-title">Add Comment</h4>
                                          </div>
                                            <div class="modal-body">
                                            
                                            
                                            
                                             <div class="form-group" >                                                                                     
                                                           
                                                           <div class="form-group" id="comments-post-other-person">
                                                           
                                                
                             
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

                $('#other-profile-posts').append(post);
                $('#NoPostsError').remove();
            }
            checkIsLiked(posts);
        },
        error:  $('#other-profile-posts').append(`<div><h1 style="color: whitesmoke; margin:15px;" class="fadeIn" id="NoPostsError">There are no posts yet.</h1></div>`)
    });

}

let  buttonValue;

function showCommentsPosts(postId) {

    buttonValue = postId;

    $("#comments-post-other-person > li").remove();

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


                    $('#comments-post-other-person').append(commentt);
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


            $('#comments-post-other-person').append(comment);

        })
}

var logo = document.getElementById('illLogo');

logo.onmouseout = function () {
    this.src = '/images/logo_invert_small.png';
};

logo.onmouseover = function () {
    this.src = '/images/logo_invert_small_bright.png';
};


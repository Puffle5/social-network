$(document).ready(function () {

    $("#upload-profile").click(function (event) {

        event.preventDefault();

        uploadProfilePicture();

    });

});

function uploadProfilePicture() {

    let form = $('#fileUploadForm')[0];

    let data = new FormData(form);

    data.append("CustomField", "This is some extra data, testing");

    $("#upload-profile").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "http://localhost:8080/api/users/uploadAction/profile-picture",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            const url = "http://localhost:8080/api/users/user/login-user";

            $.ajax({
                type: "GET",
                url: url,
                success: getUser,
                error: console.log.bind(console, "error in displaying all Posts")
            });

            function getUser(user){
                let photo = '/images/' + user.photoURL;
                console.log(photo);
                $('#profile-photo').attr('src',photo);
            }

            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#upload-profile").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#upload-profile").prop("disabled", false);

        }
    });
}


$(document).ready(function () {

    $("#upload-cover").click(function (event) {

        event.preventDefault();

        uploadCoverPicture();

    });

});

function uploadCoverPicture() {

    let form = $('#cover-upload')[0];

    let data = new FormData(form);

    data.append("CustomField", "This is some extra data, testing");

    $("#upload-cover").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "http://localhost:8080/api/users/uploadAction/cover-picture",
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            const url = "http://localhost:8080/api/users/user/login-user";

            $.ajax({
                type: "GET",
                url: url,
                success: getUser,
                error: console.log.bind(console, "error in displaying all Posts")
            });

            function getUser(user){
                let photo = '/images/' + user.coverPhotoURL;

                $('#cover-picture').attr('src',photo);
            }

            $("#result").text(data);
            $("#upload-cover").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            $("#upload-cover").prop("disabled", false);

        }
    });
}

function changeProfileInfo() {
    let url = "http://localhost:8080/api/users/update-user";
    let email = document.getElementById("account-email").value;


    let data = {
        "username" : null,
        "email" : email
    };

    fetch(url,{
        method : "POST",
        body : JSON.stringify(data),
        headers : {
            "Content-type":"application/json",
            "Accept": "application/json, text/plain, */*"
        }

    }).then((res) => res.json())
        .then(function (user) {
            console.log(user);
        })
        .catch((error)=>{
            alert("ops");
        });
}
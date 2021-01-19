
$('#login-button').click(function () {
    $('#formid').attr('action', '/authenticate');
    $('#formid').submit();
});

$('#register-button').click(function () {

    window.location.href = "http://localhost:8080/register";
});

$('#register').click(function () {
    $('#register-form').attr('action', '/register');
    $('#register-form').submit();
});
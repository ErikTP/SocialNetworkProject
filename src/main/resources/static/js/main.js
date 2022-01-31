//## JavaScript funktioner som påminner användaren att inmata text till inläggets titel och meddelande ##//

$('#title').blur(function(){
    if ( $(this).val() == ""){
        $(this).css('border', 'solid 1px red');
        $('#remind-title').text('Forgot to write Title?');
    }
})

$('#title').keydown(function(){
    if ( $(this).val() !== ""){
        $(this).css('border', 'solid 1px #777');
        $('#remind-title').text('Thanks for adding Title!');
    }
})

$('#message').blur(function(){
    if ( $(this).val() == ""){
        $(this).css('border', 'solid 1px red');
        $('#remind-message').text('Forgot to write Message?');
    }
})

$('#message').keydown(function(){
    if ( $(this).val() !== ""){
        $(this).css('border', 'solid 1px #777');
        $('#remind-message').text('Thanks for adding Message!');
    }
})

//## JavaScript funktioner för popup-fönster med varningsmeddelande inför särskilda request som radering och utloggning ##//

const postDel = document.getElementById('postDel')
const logOff = document.getElementById('logOff')
const delAcc = document.getElementById('delAcc')

$(postDel).click(() => {
    if (confirm("Do you really want to delete all your posts? This process cannot be undone!")) {
        alert("Your posts have been deleted successfully!")
        return true;
    } else {
        alert("Delete request has been cancelled!")
        return false;
    }
})

$(logOff).click(() => {
    if (confirm("Do you really want to log out? This process cannot be undone!")) {
        alert("You have been successfully logged out!")
        return true;
    } else {
        alert("Log out request has been cancelled!")
        return false;
    }
})

$(delAcc).click(() => {
    if (confirm("Do you really want to delete this account? This process cannot be undone!")) {
        alert("Your account has been deleted successfully!")
        return true;
    } else {
        alert("Delete request has been cancelled!")
        return false;
    }
})
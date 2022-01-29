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
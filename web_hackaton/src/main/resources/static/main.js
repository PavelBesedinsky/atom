var host = "localhost";
var port = 8080;

function addAttr(elemId, attr, value) {
    $(elemId).attr(attr, value);
}

function removeAttr(elemId, attr) {
    $(elemId).removeAttr(attr);
}

function loadHistory() {
    var settings = {
        "crossDomain": true,
        "url": "http://" + host + ":" + port + "/chat/chat",
        "method": "GET",
    }
    $.ajax(settings).done(function (response) {
        $("#history").html(response.replace(/\n/g, "<br />"));
        $("#history").scrollTop($("#history")[0].scrollHeight);

        let textHtml = $('#history');
        let textToStrs;
        for(let i = 0; i < textHtml.length; i++) {
            let text = $(textHtml[i]).html();
            textToStrs = text.toString().split('<br>');
            let names = [];
            for (let s of textToStrs) {
                let words = s.split(']');
                names.push(words[0].slice(1));
            }
            textToStrs = textToStrs.map((s, i) => s.replace(names[i], '<span style="color:red">' + names[i] + '</span>'));
        }
        let result = textToStrs.join('<br>');
        $('#history').html(result);
        online();
    }).fail(function (jqXHR, textStatus) {
        console.log(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
    });
}

function say() {
    var name = $('#loginName').serialize();
    var password = $('#loginPassword').serialize();
    var msg = $('#msg').serialize();

    var settings = {
        "method": "POST",
        "crossDomain": true,
        "url": "http://" + host + ":" + port + "/chat/say",
        "data": name + '&' + password + '&' + msg
    };

    $.ajax(settings).done(function (response) {
        $('#msgform').trigger("reset");
        document.getElementById("msg").value = "";
        loadHistory();
    }).fail(function (jqXHR, textStatus) {
        alert(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
        console.log(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
    });
}

function signUp() {
    var name = $('#signUpName').serialize();
    var password = $('#signUpPassword').serialize();

    var settings = {
        "method": "POST",
        "crossDomain": true,
        "url": "http://" + host + ":" + port + "/chat/signup",
        "data": name + '&' + password
    };

    $.ajax(settings).done(function (response) {
        loadHistory();
    }).fail(function (jqXHR, textStatus) {
        alert(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
        console.log(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
    });
}

function login() {
    var name = $('#loginName').serialize();
    var password = $('#loginPassword').serialize();

    var settings = {
        "method": "POST",
        "crossDomain": true,
        "url": "http://" + host + ":" + port + "/chat/login",
        "data": name + '&' + password
    };

    $.ajax(settings).done(function (response) {
        loadHistory();
        addAttr("#loginName", "readonly", "readonly");
        addAttr("#loginPassword", "readonly", "readonly");
        addAttr("#buttonLogout", "disabled", false);
        addAttr("#buttonLogin", "disabled", true);
    }).fail(function (jqXHR, textStatus) {
        alert(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
        console.log(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
    });
}

function logout() {
    var name = $('#loginName').serialize();
    var password = $('#loginPassword').serialize();

    var settings = {
        "method": "POST",
        "crossDomain": true,
        "url": "http://" + host + ":" + port + "/chat/logout",
        "data": name + '&' + password
    };

    $.ajax(settings).done(function (response) {
        loadHistory();
        removeAttr("#loginName", "readonly");
        removeAttr("#loginPassword", "readonly");
        addAttr("#buttonLogout", "disabled", true);
        addAttr("#buttonLogin", "disabled", false);
    }).fail(function (jqXHR, textStatus) {
        alert(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
        console.log(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
    });
}

function online() {
    var settings = {
        "crossDomain": true,
        "url": "http://" + host + ":" + port + "/chat/online",
        "method": "GET",
    }
    $.ajax(settings).done(function (response) {
        $("#online").html(response.replace(/\n/g, "<br />"));
        $("#online").scrollTop($("#online")[0].scrollHeight);
    }).fail(function (jqXHR, textStatus) {
        console.log(jqXHR.status + " " + jqXHR.statusText + ". " + jqXHR.responseText);
    });
}

loadHistory();
setInterval(loadHistory, 10000);
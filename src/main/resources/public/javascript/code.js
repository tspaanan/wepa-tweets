function searchTweetters() {
    var url = contextRoot + "search"
    var searchterm = document.getElementById("searchterm").value
    if (searchterm) {
        var xhttp = new XMLHttpRequest()
        xhttp.onload = function() {
            responseInList = JSON.parse(this.responseText)
            var htmlSection = document.getElementById("searchResultsList")
            htmlSection.removeChild(htmlSection.firstChild)
            for (var i = 0; i < responseInList.length; i++) {
                var newLiElement = document.createElement("li")
                newLiElement.style = "margin-top: 10px"
                htmlSection.appendChild(newLiElement)
                if (responseInList[0].username == null) {
                    var errorMessage = document.createTextNode("Found nothing!")
                    newLiElement.appendChild(errorMessage)
                } else {
                var newAElement = document.createElement("a")
                newAElement.href = "/wepa-tweetter/" + responseInList[i].random
                var newAContent = document.createTextNode(responseInList[i].username)
                newAElement.appendChild(newAContent)
                newAElement.title = responseInList[i].username

                var newFollowElement = document.createElement("button")
                newFollowElement.id = responseInList[i].username
                newFollowElement.innerHTML = "Follow"
                newFollowElement.style = "margin-left: 10px"
                newFollowElement.className = "btn btn-primary"
                newLiElement.id = "li" + responseInList[i].username
                
                //for some strange reason, only an anonymous function like below
                //can be attached to onclick-attribute via Javascript
                //modern alternative would be to use eventListener
                newFollowElement.onclick = function() {
                    follow(newFollowElement.id)
                }
                
                newLiElement.appendChild(newAElement)
                newLiElement.appendChild(newFollowElement)
                }
            }
        }
        xhttp.open("GET", url + "?searchterm=" + searchterm)
        xhttp.send()
    }
}

function follow(id) {
    //alert("Hello, " + document.getElementById(id).id)
    var xhttp = new XMLHttpRequest()
    xhttp.onload = function() {
        //followed-teksti napin oikealle puolelle
        var LiElement = document.getElementById("li" + id)
        //LiElement.appendChild(document.createTextNode("testi"))
        var testiOlio = JSON.parse(this.responseText)
        var alertElement = document.createElement("div")
        if (testiOlio.username == null) {
            //LiElement.appendChild(document.createTextNode("oliNull"))
            alertElement.className = "alert alert-danger"
            alertElement.style = "margin-top: 10px"
            alertElement.innerHTML = "Impossible!"
        } else {
            //LiElement.appendChild(document.createTextNode("EINull"))
            alertElement.className = "alert alert-success"
            alertElement.style = "margin-top: 10px"
            alertElement.innerHTML = "Success!"
        }
            if (LiElement.classList.contains("alert")) {
                LiElement.removeChild(LiElement.lastChild)
            }
            LiElement.appendChild(alertElement)
            LiElement.classList.add("alert")
            
    }
    var url = contextRoot + "follow"
    xhttp.open("GET", url + "?follow=" + id)
    xhttp.send()
}

function test() {
    alert("test")
}
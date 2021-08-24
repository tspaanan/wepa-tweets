function searchTweetters() {
    var url = contextRoot + "search"
    var searchterm = document.getElementById("searchterm").value
    if (searchterm) {
        var xhttp = new XMLHttpRequest()
        xhttp.onload = function() {
            responseInList = JSON.parse(this.responseText)
            var htmlSection = document.getElementById("searchResultsList")
            for (var i = 0; i < responseInList.length; i++) {
                var newLiElement = document.createElement("li")
                htmlSection.appendChild(newLiElement)
                
                var newAElement = document.createElement("a")
                newAElement.href = "/wepa-tweetter/" + responseInList[i].random
                var newAContent = document.createTextNode(responseInList[i].username)
                newAElement.appendChild(newAContent)
                newAElement.title = responseInList[i].username

                var newFollowElement = document.createElement("button")
                newFollowElement.id = responseInList[i].username
                newFollowElement.innerHTML = "Follow"
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
            alertElement.innerHTML = "Impossible!"
        } else {
            //LiElement.appendChild(document.createTextNode("EINull"))
            alertElement.className = "alert alert-success"
            alertElement.innerHTML = "Success!"
        }
            LiElement.appendChild(alertElement)
    }
    var url = contextRoot + "follow"
    xhttp.open("GET", url + "?follow=" + id)
    xhttp.send()
}

function test() {
    alert("test")
}
var content = document.getElementById("objectives");
var button = document.getElementById("show-more")

button.onclick = function(){

    document.getElementById("demo").innerHTML = text;

    if(content.className === "open"){
        content.className = "";
        button.innerHTML = "More information";

    } else {
        content.className = "open";
        button.innerHTML = "Less information";
    }

};

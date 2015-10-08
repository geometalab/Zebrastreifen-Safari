$(".buttons").addClass("js").before('<div id="menu">&#9776;</div>');
var hide = false;
$("#menu").click(function(){
    $(".buttons").toggle();
    hide = !hide;
    $(".header-text").hide();

    if (!hide){
        $(".header-text").show();
        $(".header").height("15%");
        $("#map").height("85%");
    }
    if (hide) {
        $(".header").height("40%");
        $("#map").height("60%");
    }


});
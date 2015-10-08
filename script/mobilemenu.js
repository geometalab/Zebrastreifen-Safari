$(".buttons").addClass("js").before('<div id="menu">&#9776;</div>');
var hide = false;
$("#menu").click(function(){
    $(".buttons").toggle();
    hide = !hide;
    $(".header-text").hide();
    if (!hide){
        $(".header-text").show();
    }
});

$(window).resize(function(){
    if(window.innerWidth > 799) {
        $(".buttons").removeAttr("style");
    }
});

$(".test").addClass("js").before('<div id="menu">&#9776;</div>');
var hide = false;
$("#menu").click(function(){
    $(".buttons").toggle();
    hide = !hide;
    $(".header-text").hide();
    if (!hide &&
        $(document).width() <= 600 &&
        $(document).height() >= 705){
        $(".header-text").show();
        $(".header").height("5%");
        $("#map").height("95%");
    } else if(!hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 705 && $(document).height() >= 576){
        $(".header-text").show();
        $(".header").height("7%");
        $("#map").height("93%");
    } else if(!hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 575 && $(document).height() >= 441){
        $(".header-text").show();
        $(".header").height("10%");
        $("#map").height("90%");
    } else if(!hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 440 && $(document).height() >= 336){
        $(".header-text").show();
        $(".header").height("12%");
        $("#map").height("88%");
    } else if(!hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 335 && $(document).height() >= 251){
        $(".header-text").show();
        $(".header").height("15%");
        $("#map").height("85%");
    } else if(!hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 250){
        $(".header-text").show();
        $(".header").height("17%");
        $("#map").height("83%");
    } else if (!hide &&
        $(document).width() <= 325 &&
        $(document).height() >= 745 ){
        $(".header-text").show();
        $(".header").height("5%");
        $("#map").height("95%");
    } else if (!hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 745 && $(document).height() >= 576){
        $(".header-text").show();
        $(".header").height("7%");
        $("#map").height("93%");
    } else if (!hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 575 && $(document).height() >= 471){
        $(".header-text").show();
        $(".header").height("8%");
        $("#map").height("92%");
    } else if (!hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 470 && $(document).height() >= 361){
        $(".header-text").show();
        $(".header").height("10%");
        $("#map").height("90%");
    } else if (!hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 360 && $(document).height() >= 311){
        $(".header-text").show();
        $(".header").height("12%");
        $("#map").height("88%");
    } else if (!hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 310){
        $(".header-text").show();
        $(".header").height("15%");
        $("#map").height("85%");
    }

    if (hide &&
        $(document).width() <= 600 &&
        $(document).height() >= 705) {
        $(".header").height("15%");
        $("#map").height("85%");
    } else if (hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 705 && $(document).height() >= 576){
        $(".header").height("20%");
        $("#map").height("80%");
    } else if (hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 575 && $(document).height() >= 441){
        $(".header").height("28%");
        $("#map").height("82%");
    } else if (hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 440 && $(document).height() >= 336){
        $(".header").height("30%");
        $("#map").height("70%");
    } else if (hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 335 && $(document).height() >= 251){
        $(".header").height("32%");
        $("#map").height("68%");
    } else if (hide &&
        $(document).width() <= 600 &&
        $(document).height() <= 250){
        $(".header").height("35%");
        $("#map").height("65%");
    } else if (hide &&
        $(document).width() <= 325 &&
        $(document).height() >= 745){
        $(".header").height("80%");
        $("#map").height("20%");
    } else if (hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 745 && $(document).height() >= 576){
        $(".header").height("30%");
        $("#map").height("70%");
    } else if (hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 575 && $(document).height() >= 471){
        $(".header").height("30%");
        $("#map").height("70%");
    } else if (hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 470 && $(document).height() >= 361){
        $(".header").height("25%");
        $("#map").height("75%");
    } else if (hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 360 && $(document).height() >= 311){
        $(".header").height("28%");
        $("#map").height("72%");
    } else if (hide &&
        $(document).width() <= 325 &&
        $(document).height() <= 310){
        $(".header").height("30%");
        $("#map").height("70%");
    }


});
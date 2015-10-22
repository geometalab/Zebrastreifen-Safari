jQuery(document).ready(function () {

    //Open the menu
    jQuery("#navigation").click(function () {

        jQuery('#content').css('min-height', jQuery(window).height());

        jQuery('nav').css('opacity', 1);

        //set the width of primary content container -> content should not scale while animating
        var contentWidth = jQuery('#content').width();

        //set the content with the width that it has originally
        jQuery('#content').css('width', contentWidth);

        //display a layer to disable clicking and scrolling on the content while menu is shown
        jQuery('#contentLayer').css('display', 'block');

        //disable all scrolling on mobile devices while menu is shown
        jQuery('#container').bind('touchmove', function (e) {
            e.preventDefault()
        });

        //set margin for the whole container with a jquery UI animation
        jQuery("#container").animate({"marginLeft": ["30%", 'easeOutExpo']}, {
            duration: 700
        });

    });

    //close the menu
    jQuery("#contentLayer").click(function () {

        //enable all scrolling on mobile devices when menu is closed
        jQuery('#container').unbind('touchmove');

        //set margin for the whole container back to original state with a jquery UI animation
        jQuery("#container").animate({"marginLeft": ["-1", 'easeOutExpo']}, {
            duration: 700,
            complete: function () {
                jQuery('#content').css('width', 'auto');
                jQuery('#contentLayer').css('display', 'none');
                jQuery('nav').css('opacity', 0);
                jQuery('#content').css('min-height', 'auto');

            }
        });
    });

});

//
//$(".nav1").addClass("js").before('<div id="menu">&#9776;</div>');
//var hide = false;
//$("#menu").click(function(){
//    $(".nav1").toggle();
//    hide = !hide;
//    $(".header-text").hide();
//    if (!hide
//        && $(document).height() >= 1155){
//        $(".header-text").show();
//        $(".header").height("5%");
//        $("#map").height("95%");
//    } else if (!hide
//        && $(document).height() <= 1155
//            && $(document).height() > 840){
//        $(".header-text").show();
//        $(".header").height("7%");
//        $("#map").height("93%");
//    } else if (!hide
//        && $(document).height() <= 840
//            && $(document).height() > 610){
//        $(".header-text").show();
//        $(".header").height("9%");
//        $("#map").height("91%");
//    } else if (!hide
//        && $(document).height() <= 610
//            && $(document).height() > 550){
//        $(".header-text").show();
//        $(".header").height("10%");
//        $("#map").height("90%");
//    } else if (!hide
//        && $(document).height() <= 550
//            && $(document).height() > 465){
//        $(".header-text").show();
//        $(".header").height("12%");
//        $("#map").height("88%");
//    } else if (!hide && $(document).height() <= 465){
//        $(".header-text").show();
//        $(".header").height("15%");
//        $("#map").height("85%");
//    }
//
//    if (hide &&
//        $(document).height() >= 720) {
//        $(".header").height("15%");
//        $("#map").height("85%");
//    } else if (hide &&
//        $(document).height() <= 720
//        && $(document).height() > 600){
//        $(".header").height("18%");
//        $("#map").height("82%");
//    } else if (hide &&
//        $(document).height() <= 600
//        && $(document).height() > 540){
//        $(".header").height("20%");
//        $("#map").height("80%");
//    } else if (hide &&
//        $(document).height() <= 540
//        && $(document).height() > 475){
//        $(".header").height("23%");
//        $("#map").height("77%");
//    } else if (hide &&
//        $(document).height() <= 475
//        && $(document).height() > 435){
//        $(".header").height("25%");
//        $("#map").height("75%");
//    } else if (hide &&
//        $(document).height() <= 435
//        && $(document).height() > 390){
//        $(".header").height("28%");
//        $("#map").height("72%");
//    } else if (hide &&
//        $(document).height() <= 390
//        && $(document).height() > 300){
//        $(".header").height("32%");
//        $("#map").height("68%");
//    }
//
//
//
//});
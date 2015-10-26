$(document).ready(function () {
    $("#navigation").click(function () {

        $('#content').css('min-height', $(window).height());

        $('nav').css('opacity', 1);

        var contentWidth = $('#content').width();

        $('#content').css('width', contentWidth);

        $('#contentLayer').css('display', 'block');

        $('#container').bind('touchmove', function (e) {
            e.preventDefault()
        });

        $("#container").animate({"marginLeft": ["30%", 'easeOutExpo']}, {
            duration: 700
        });

    });

    $("#contentLayer").click(function () {

        $('#container').unbind('touchmove');

        $("#container").animate({"marginLeft": ["-1", 'easeOutExpo']}, {
            duration: 700,
            complete: function () {
                $('#content').css('width', 'auto');
                $('#contentLayer').css('display', 'none');
                $('nav').css('opacity', 0);
                $('#content').css('min-height', 'auto');

            }
        });
    });

});

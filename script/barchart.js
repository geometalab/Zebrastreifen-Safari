$(document).ready(function() {
    
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/crossingbarchart',
        dataType: 'json',
        method: 'get',
        success: function (response)  {
            var ctx = document.getElementById("barcanvas").getContext("2d");

            var dataSaved = response;
            var chartjsData = [];
            for (var i = 0; i < dataSaved.length; i++) {
                chartjsData.push(dataSaved[i].amount);
            }
            var chartjsLabel = [];
            for (var i = 0; i < dataSaved.length; i++) {
                chartjsLabel.push(dataSaved[i].week);
            }
            $.ajax({
                url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/ratingbarchart',
                dataType: 'json',
                method: 'get',
                success: function (response2) {
                    var dataSaved2 = response2;
                    var ratingData = [];
                    for (var i = 0; i < dataSaved2.length; i++) {
                        ratingData.push(dataSaved2[i].amount);
                    }

                    var data = {
                        labels: chartjsLabel,
                        datasets: [
                            {
                                fillColor: "rgba(151,187,205,0.5)",
                                strokeColor: "rgba(151,187,205,0.8)",
                                highlightFill: "rgba(151,187,205,0.75)",
                                highlightStroke: "rgba(151,187,205,1)",
                                data: chartjsData
                            },
                            {
                                fillColor: "rgba(151,187,205,0.5)",
                                strokeColor: "rgba(151,187,205,0.8)",
                                highlightFill: "rgba(151,187,205,0.75)",
                                highlightStroke: "rgba(151,187,205,1)",
                                data: ratingData
                            }
                        ]
                    };

                    if ($(document).width() <= 800){
                        var myBarChart = new Chart(ctx).Bar(data, {
                            scaleLabel : "<%= Number(value) + ' Z*'%>",
                            scaleBeginAtZero: true,
                            responsive: true
                        });
                    } else {
                        var myBarChart = new Chart(ctx).Bar(data, {
                            scaleLabel : "<%= Number(value) + ' Z*'%>",
                            scaleBeginAtZero: true,
                        });
                    }
                }
            })
        }

    })
});






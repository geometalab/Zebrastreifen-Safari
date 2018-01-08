$(document).ready(function() {
    var BASE_URL = 'https://zebrastreifen.sifs0003.infs.ch/zebra';
    
    $.ajax({
        url: `${BASE_URL}/api/v1/crossinglinechart`,
        dataType: 'json',
        method: 'get',
        success: function (data1)  {
            var ctx = document.getElementById("linecanvas").getContext("2d");
            var dataSaved1 = data1;
            var chartjsData = [];
            for (var i = 0; i < dataSaved1.length; i++) {
                chartjsData.push(dataSaved1[i].amount);
            }

            var chartjsLabel = [];
            for (var i = 0; i < dataSaved1.length; i++) {
                chartjsLabel.push(dataSaved1[i].week);
            }
            var data = {
                labels: chartjsLabel,
                datasets: [
                    {
                        fillColor: "rgba(151,187,205,0.2)",
                        strokeColor: "rgba(151,187,205,1)",
                        pointColor: "rgba(151,187,205,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(151,187,205,1)",
                        data: chartjsData
                    }
                ]
            };
            if($(document).width() <= 800){
                var myLineChart = new Chart(ctx).Line(data, {
                    scaleLabel : "<%= Number(value) + ' F*'%>",
                    //scaleBeginAtZero: true,
                    responsive: true
                });
            } else {
                var myLineChart = new Chart(ctx).Line(data, {
                    scaleLabel : "<%= Number(value) + ' F*'%>",
                   // scaleBeginAtZero: true
                });
            }

        }

    });
    $.ajax({
        url: `${BASE_URL}/api/v1/ratinglinechart`,
        dataType: 'json',
        method: 'get',
        success: function (data1)  {
            var ctx = document.getElementById("ratinglinecanvas").getContext("2d");
            var dataSaved1 = data1;
            var chartjsData = [];
            for (var i = 0; i < dataSaved1.length; i++) {
                chartjsData.push(dataSaved1[i].amount);
            }

            var chartjsLabel = [];
            for (var i = 0; i < dataSaved1.length; i++) {
                chartjsLabel.push(dataSaved1[i].week);
            }
            var data = {
                labels: chartjsLabel,
                datasets: [
                    {
                        fillColor: "rgba(151,187,205,0.2)",
                        strokeColor: "rgba(151,187,205,1)",
                        pointColor: "rgba(151,187,205,1)",
                        pointStrokeColor: "#fff",
                        pointHighlightFill: "#fff",
                        pointHighlightStroke: "rgba(151,187,205,1)",
                        data: chartjsData
                    }
                ]
            };
            if($(document).width() <= 800){
                var LineChart = new Chart(ctx).Line(data, {
                    scaleLabel : "<%= Number(value) + ' F*'%>",
                    //scaleBeginAtZero: true,
                    responsive: true
                });
            } else {
                var LineChart = new Chart(ctx).Line(data, {
                    scaleLabel : "<%= Number(value) + ' F*'%>",
                    // scaleBeginAtZero: true
                });
            }

        }

    })
});


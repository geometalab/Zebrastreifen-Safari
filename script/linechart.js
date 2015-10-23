$(document).ready(function() {
    $.ajax({
        url: 'http://sifsv-80047.hsr.ch/zebra/api/v1/linechart',
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

            var myLineChart = new Chart(ctx).Line(data, {
                scaleLabel : "<%= Number(value) + ' Zeb*'%>",
                scaleBeginAtZero: true,
                responsive: true
            });
        }

    })
});


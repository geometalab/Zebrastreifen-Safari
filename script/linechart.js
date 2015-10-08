var ctx = document.getElementById("linecanvas").getContext("2d");

var json = [{"week":"1999-02-15","total":1},{"week":"2000-10-16","total":2501},{"week":"2005-08-08","total":42510},{"week":"2005-08-15","total":50513},{"week":"2006-08-07","total":58516},{"week":"2006-12-18","total":66519},{"week":"2009-06-08","total":74522},{"week":"2010-01-25","total":82525},{"week":"2010-12-20","total":90528},{"week":"2011-01-03","total":98531},{"week":"2011-07-04","total":106534},{"week":"2012-05-07","total":114537},{"week":"2015-08-10","total":122541}]
var chartjsData = [];
for (var i = 0; i < json.length; i++) {
    chartjsData.push(json[i].total);
}

var chartjsLabel = [];
for (var i = 0; i < json.length; i++) {
    chartjsLabel.push(json[i].week);
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
    responsive: true,
});
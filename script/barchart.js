var ctx = document.getElementById("barcanvas").getContext("2d");

var json = [{"week":"2005-08-15","amount":8003},{"week":"2006-08-07","amount":8003},{"week":"2006-12-18","amount":8003},{"week":"2009-06-08","amount":8003},{"week":"2010-01-25","amount":8003},{"week":"2010-12-20","amount":8003},{"week":"2011-01-03","amount":8003},{"week":"2011-07-04","amount":8003},{"week":"2012-05-07","amount":8003},{"week":"2015-08-10","amount":8004}]

var chartjsData = [];
for (var i = 0; i < json.length; i++) {
    chartjsData.push(json[i].amount);
}

var chartjsLabel = [];
for (var i = 0; i < json.length; i++) {
    chartjsLabel.push(json[i].week);
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
        }
    ]
};

var myBarChart = new Chart(ctx).Bar(data, {
    scaleLabel : "<%= Number(value) + ' Zeb*'%>",
    scaleBeginAtZero: true,
    responsive: true,
});

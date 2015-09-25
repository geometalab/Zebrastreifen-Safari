var ctx = document.getElementById("linecanvas").getContext("2d");



var data = {
    labels: ["January", "February", "March", "April", "May", "June", "July", " ejfo", "jebhil", "iojesopi"],
    datasets: [
        {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: [28, 48, 40, 19, 86, 27, 90, 45, 50, 50, 50]
        }
    ]
};

var myLineChart = new Chart(ctx).Line(data, {
    scaleLabel : "<%= Number(value) + ' Zebrastreifen'%>",
    scaleBeginAtZero: true,
    responsive: true,
});
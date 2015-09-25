var ctx = document.getElementById("linecanvas").getContext("2d");



 var data = {
    labels: ["January", "February", "March", "April", "May", "June", "July"],
    datasets: [
        {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: [65, 59, 80, 81, 56, 55, 40]
        }
    ]
};
var myLineChart = new Chart(ctx).Line(data, {


});
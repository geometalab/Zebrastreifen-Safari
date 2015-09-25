var ctx = document.getElementById("barcanvas").getContext("2d");

var data = {
    labels: ["January", "February", "March", "April", "May", "June", "July", " ejfo", "jebhil", "iojesopi"],
    datasets: [
        {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.5)",
            strokeColor: "rgba(151,187,205,0.8)",
            highlightFill: "rgba(151,187,205,0.75)",
            highlightStroke: "rgba(151,187,205,1)",
            data: [28, 48, 40, 19, 86, 27, 30, 50, 50, 50]
        }
    ]
};

var myBarChart = new Chart(ctx).Bar(data, {
    scaleLabel : "<%= Number(value) + ' Zebrastreifen'%>",
    scaleBeginAtZero: true,
    responsive: true,
});

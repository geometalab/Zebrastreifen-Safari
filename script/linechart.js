$(document).ready(function() {
  var BASE_URL = 'https://zebrastreifen.sifs0003.infs.ch/zebra';
  var lineChartOptionsLogarithmic = {
    responsive: true,
    layout: {
      padding: {
        left: 20,
        right: 20,
        top: 0,
        bottom: 0
      }
    },
    title: {
      display: false,
    },
    scales: {
      xAxes: [{
        display: true,
      }],
      yAxes: [{
        display: true,
        type: 'logarithmic',
        ticks: {
          callback: function(value, index, values) {
            return Number(value).toString() + " F* ";
          },
        },
      }]
    },
  };
  var lineChartOptions = {
    responsive: true,
    layout: {
      padding: {
        left: 20,
        right: 20,
        top: 0,
        bottom: 0
      }
    },
    title: {
      display: false,
    },
    scales: {
      xAxes: [{
        display: true,
      }],
      yAxes: [{
        display: true,
        ticks: {
          callback: function(value, index, values) {
            return Number(value).toString() + " F* ";
          },
        },
      }]
    },
  };

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
            label: "Fussgängerstreifen Total",
            backgroundColor: "rgba(151,187,205,0.2)",
            borderColor: "rgba(151,187,205,1)",
            pointBackgroundColor: "rgba(151,187,205,1)",
            pointBorderColor: "#fff",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(151,187,205,1)",
            data: chartjsData
          }
        ]
      };
      var myLineChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: lineChartOptionsLogarithmic,
      });
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
            label: "Bewertete Fussgängerstreifen",
            backgroundColor: "rgba(151,187,205,0.2)",
            borderColor: "rgba(151,187,205,1)",
            pointBackgroundColor: "rgba(151,187,205,1)",
            pointBorderColor: "#fff",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(151,187,205,1)",
            data: chartjsData
          }
        ]
      };
      var myLineChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: lineChartOptions,
      });
    }
  })
});


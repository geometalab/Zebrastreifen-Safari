$(document).ready(function() {
  var BASE_URL = 'https://zebrastreifen.sifs0003.infs.ch/zebra';

  var barChartOptions = {
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
          beginAtZero: true,
          callback: function(value, index, values) {
            return Number(value).toString() + " F* ";
          },
        },
      }]
    },
    tooltips: {
      callbacks: {
        label: function(tooltipItem, data) {
          var label = data.datasets[tooltipItem.datasetIndex].label || '';

          if (label) {
            label += ': ';
          }
          var value = tooltipItem.yLabel;
          if (value === 0.3) {
              value = "0";
          }
          label += value;
          return label;
        }
      }
    },
  };

  $.ajax({
    url: `${BASE_URL}/api/v1/crossingbarchart`,
    dataType: 'json',
    method: 'get',
    success: function (response)  {
      var ctx = document.getElementById("barcanvas").getContext("2d");

      var dataSaved = response;
      var chartjsData = [];
      var chartjsLabel = [];
      for (var i = 0; i < dataSaved.length; i++) {
        chartjsData.push(dataSaved[i].amount === 0 ? 0.3 : dataSaved[i].amount);
        chartjsLabel.push(dataSaved[i].week);
      }
      $.ajax({
        url: `${BASE_URL}/api/v1/ratingbarchart`,
        dataType: 'json',
        method: 'get',
        success: function (response2) {
          var dataSaved2 = response2;
          var ratingData = [];
          for (var i = 0; i < dataSaved2.length; i++) {
            ratingData.push(dataSaved2[i].amount === 0 ? 0.3 : dataSaved2[i].amount);
          }

          var data = {
            labels: chartjsLabel,
            datasets: [
              {
                label: "Neu erfasste Fussgängerstreifen",
                backgroundColor: "rgba(151,187,205,0.5)",
                borderColor: "rgba(151,187,205,0.8)",
                hoverBackgroundColor: "rgba(151,187,205,0.75)",
                hoverBorderColor: "rgba(151,187,205,1)",
                data: chartjsData
              },
              {
                label: "bewertete Fussgängertreifen",
                backgroundColor: "rgba(151,187,205,0.5)",
                borderColor: "rgba(151,187,205,0.8)",
                hoverBackgroundColor: "rgba(151,187,205,0.75)",
                hoverBorderColor: "rgba(151,187,205,1)",
                data: ratingData
              },
            ]
          };
          var myBarChart = new Chart(ctx, {
            data: data,
            type: 'bar',
            options: barChartOptions,
          });

        }
      })
    }

  })
});






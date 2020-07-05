// Google Chart Library
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawGraph);
google.charts.setOnLoadCallback(drawCSVGraph);

function drawGraph() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Projects');
  data.addColumn('number', 'Number of Lines Coded (#)');
  data.addRows([
    ['Information Kiosk', 5300],
    ['Data Science Research Project', 2568],
    ['Research Project', 4100]
  ]);

  const options = {
    'title': 'Number of Lines Coded by Project',
    'width':500,
    'height':400
  };

  const chart = new google.visualization.BarChart(
      document.getElementById('chart-container'));
  chart.draw(data, options);
}

function drawCSVGraph() {
  fetch('/graph-data').then(response => response.json()).then((electricityData) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'County');
    data.addColumn('number', 'Millions of kWh (GWh)');
    Object.keys(electricityData).forEach((county) => {
      data.addRow([county, electricityData[county]]);
    });

    const options = {
        'title': 'Usage of Electricity in Residental Counties by Millions of kWh (GWh)',
        'width':800,
        'height':2700
    };

    const chart = new google.visualization.BarChart(
        document.getElementById('chart-csv-container'));
    chart.draw(data, options);
  });
}
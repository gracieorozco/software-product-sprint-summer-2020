// Google Chart Library
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawGraph);

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
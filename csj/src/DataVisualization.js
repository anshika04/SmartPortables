google.charts.load('current', {packages: ['corechart', 'bar']});
google.charts.setOnLoadCallback(drawBasic);

function drawBasic() {

  var newData = JSON.parse(document.getElementById("idchk").value);
  var reportType = document.getElementById("reporttype").value;

  console.log(newData);

  var data = [];

  if(reportType === "inventory") {

    for(var i=0; i < newData.length; i++)
      delete newData[i].price;

    var Header= ['Product Name', 'Total available Items'];
    data.push(Header);
  }

  if(reportType === "sales") {

    for(var i=0; i < newData.length; i++)
      delete newData[i].quantity;

    var Header= ['Product Name', 'Total Sales'];
    data.push(Header);
  }
 

  // determine the number of rows and columns.
  var numRows = newData.length;      

  // now add the rows.
  for (var i = 0; i < numRows; i++)
    data.push(Object.values(newData[i]));

  console.log(data);

  var dataTable = new google.visualization.arrayToDataTable(data);

  if(reportType === "inventory") {

      var options = {

              title: 'Inventory Report',
              chartArea: {width: '60%', height: '60%'},
              legend: { position: "none" },
              hAxis: {
                title: 'Total Number Of Available Items',
                minValue: 0
              },
              vAxis: {
                title: 'Products'
              },
              bars: 'horizontal'
    };
  }

  if(reportType === "sales") {

    var options = {

              title: 'Sales Report',
              chartArea: {width: '70%', height: '60%'},
              legend: { position: "none" },
              hAxis: {
                title: 'Total Sales of Products',
                minValue: 0
              },
              vAxis: {
                title: 'Products'
              },
              bars: 'horizontal'
    };
  }

  


      var chart = new google.visualization.BarChart(document.getElementById('chart_div'));

      chart.draw(dataTable, options);
    }
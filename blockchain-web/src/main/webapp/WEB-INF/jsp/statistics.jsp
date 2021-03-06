<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>

<title>BlockChain</title>

      <link href="
      <c:url value="/resources/css/background-styles.css" />
      " rel="stylesheet">

<script>
window.onload = function() {

var chart = new CanvasJS.Chart("chartContainer", {
	theme: "light2", // "light1", "light2", "dark1", "dark2"
	exportEnabled: true,
	animationEnabled: true,
	title: {
		text: "Your income on operations - ${totalIncome}:"
	},
	data: [{
		type: "pie",
		startAngle: 25,
		toolTipContent: "<b>{label}</b>: {y}%",
		showInLegend: "true",
		legendText: "{label}",
		indexLabelFontSize: 16,
		indexLabel: "{label} - {y}%",
		dataPoints: [
			{ y: ${minerReward}, label: "Miner reward" },
			{ y: ${senderReward}, label: "Cash back on transactions" }
		]
	}]
});
chart.render();

}
</script>
</head>
<body>
<div id="chartContainer" style="height: 300px; width: 100%;"></div>
<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
<h4>Thank you for being with <a href="/blockchain-web/home" class="text-primary">BlockChain</a>! :)</h4>
<p><a href="/blockchain-web/${userId}/wallet-all" class="text-primary">Back to cabinet</a></p>
</body>
</html>
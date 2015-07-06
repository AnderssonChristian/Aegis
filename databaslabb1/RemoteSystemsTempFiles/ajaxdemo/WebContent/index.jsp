<html>
<head>
	<title>Ajax demo with HowToDoInJava.com</title>
	<script type="text/javascript">
		function ajaxAsyncRequest(reqURL)
		{
			//Creating a new XMLHttpRequest object
			var xmlhttp;
			if (window.XMLHttpRequest){
				xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
			} else {
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
			}
			//Create a asynchronous GET request
			xmlhttp.open("GET", reqURL, true);
			
			//When readyState is 4 then get the server output
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4) { 
					if (xmlhttp.status == 200) 
					{
						document.getElementById("message").innerHTML = xmlhttp.responseText;
						//alert(xmlhttp.responseText);
					} 
					else
					{
						alert('Something is wrong !!');
					}
				}
			};
			
			xmlhttp.send(null);
		}
		
	function ajaxSyncRequest(reqURL)
	{
		//Creating a new XMLHttpRequest object
		var xmlhttp;
		if (window.XMLHttpRequest){
			xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
		} else {
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
		}
		//Create a asynchronous GET request
		xmlhttp.open("GET", reqURL, false);
		xmlhttp.send(null);
		
		//Execution blocked till server send the response
		if (xmlhttp.readyState == 4) { 
			if (xmlhttp.status == 200) 
			{
				document.getElementById("message").innerHTML = xmlhttp.responseText;
				//alert(xmlhttp.responseText);
			} 
			else
			{
				alert('Something is wrong !!');
			}
		}
	}
	</script>
</head>
<body>
	<br/>
	<input type="button" value="Show Server Time" onclick='ajaxSyncRequest("get-current-time")'/>
	<br/><br/>
	Message from server :: <span id="message"></span>
</body>
</html>
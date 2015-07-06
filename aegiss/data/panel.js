self.port.on("show", function onShow(activeTabURL, pluginID) {
	document.getElementById("temp").value = activeTabURL;
	document.getElementById("pluginid").value = pluginID;
});
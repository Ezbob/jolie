$(document).ready(function(){
	/*$.ajax({
		url: 'getPageTitle',
		dataType: 'xml',
		success: function(response) { $("#title").html( $(response).find('getPageTitleResponse').text() ) }
	});*/
	
	$.ajax({
		url: 'getPageTitle',
		dataType: 'json',
		data: { "hello": "world" },
		success: function(response) { $("#title").html( response.$ ) }
	});
});
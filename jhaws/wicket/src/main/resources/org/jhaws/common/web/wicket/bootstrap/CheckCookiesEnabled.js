function showhide(layer_ref) {
	var el = document.getElementById(layer_ref); 
	var visible = el.style.display == 'block'; 
	el.style.display = visible ? 'none' : 'block'; 
	return !visible;
	}; 
var nocookies_more_information = document.getElementById('nocookies_more_information');
if (typeof nocookies_more_information != "undefined") {
	nocookies_more_information.style.display = 'none';
}

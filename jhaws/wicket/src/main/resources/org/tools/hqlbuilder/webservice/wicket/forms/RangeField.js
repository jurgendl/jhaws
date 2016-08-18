function outputUpdate(id, val) {
	document.querySelector('#' + id).value = val;
}

function ticks(id, tickstep) {
	var element = document.querySelector('#' + id);
	if (element.hasOwnProperty('list') && element.hasOwnProperty('min') && element.hasOwnProperty('max') && element.hasOwnProperty('step')) {
		var datalist = document.createElement('datalist');
		var minimum = parseInt(element.getAttribute('min'));
		var step = tickstep; //parseInt(element.getAttribute('step'));
		var maximum = parseInt(element.getAttribute('max'));
		datalist.id = element.getAttribute('list');
		for (var i = minimum; i < maximum + step; i = i + step) {
			datalist.innerHTML += "<option value=" + i + "></option>";
		}
		element.parentNode.insertBefore(datalist, element.nextSibling);
	}
}

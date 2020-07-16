if (attrs.event.originalEvent.target.dataset.doContinueConfirm === 'true') {
	attrs.event.originalEvent.target.dataset.doContinueConfirm = 'false';
	return true;
}
attrs.event.preventDefault();
grecaptcha.execute('$SITEKEY$', {
	action : 'submit'
}).then(function(token) {
	$('#$TOKENID$').val(token);
	attrs.event.originalEvent.target.dataset.doContinueConfirm = 'true';
	if (navigator.userAgent.toLowerCase().indexOf('firefox') != -1 || navigator.userAgent.toLowerCase().indexOf('chrome') != -1) {
		attrs.event.target.dispatchEvent(attrs.event.originalEvent);
	} else {
		let event;
		if (typeof (Event) === 'function') {
			event = new Event(attrs.event.originalEvent.type);
		} else {
			event = document.createEvent('Event');
			event.initEvent(attrs.event.originalEvent.type, true, true);
		}
		attrs.event.target.dispatchEvent(event);
	}
});
return false;
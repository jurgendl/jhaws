if (attrs.event.originalEvent.target.dataset.doContinueConfirm === 'true') {
	attrs.event.originalEvent.target.dataset.doContinueConfirm = 'false';
	return true;
}
window.reCaptchaCallback = function() {
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
};
attrs.event.preventDefault();
grecaptcha.execute();
return false;
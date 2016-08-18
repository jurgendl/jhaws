var opts = {
  lines: 16, // The number of lines to draw
  length: 16, // The length of each line
  width: 4, // The line thickness
  radius: 20, // The radius of the inner circle
  corners: 1, // Corner roundness (0..1)
  rotate: 0, // The rotation offset
  direction: 1, // 1: clockwise, -1: counterclockwise
  color: '#000', // #rgb or #rrggbb or array of colors
  speed: 1, // Rounds per second
  trail: 60, // Afterglow percentage
  shadow: true, // Whether to render a shadow
  hwaccel: true, // Whether to use hardware acceleration
  className: 'spinner', // The CSS class to assign to the spinner
  zIndex: 2e9, // The z-index (defaults to 2000000000)
  top: '50%', // Top position relative to parent
  left: '50%' // Left position relative to parent
};
var spinnerInstance;
function startSpinner() {
	if(spinnerInstance) {
	} else {
		console.log('start spinner');
		try {
			$('#spinnercontainer').addClass('visible');
		} catch (e) {
			//
		}
		spinnerInstance = new Spinner(opts);
		spinnerInstance.spin(document.getElementById('spinner'));
	}
}
function stopSpinner() {
	try {
		console.log('stop spinner');
		try {
			$('#spinnercontainer').removeClass('visible');
		} catch (e) {
			//
		}
		spinnerInstance.stop();
		spinnerInstance = null;
	} catch (e) {
		//
	}
}


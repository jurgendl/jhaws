//import {Spinner} from 'spin.js';
var opts = {
  lines: 20, // The number of lines to draw
  length: 0, // The length of each line
  width: 4, // The line thickness
  radius: 20, // The radius of the inner circle
  scale: 1.3, // Scales overall size of the spinner
  corners: 1, // Corner roundness (0..1)
  color: '#000000', // CSS color or array of colors
  fadeColor: 'transparent', // CSS color or array of colors
  opacity: 0, // Opacity of the lines
  rotate: 0, // The rotation offset
  direction: 1, // 1: clockwise, -1: counterclockwise
  speed: 0.8, // Rounds per second
  trail: 32, // Afterglow percentage
  fps: 20, // Frames per second when using setTimeout() as a fallback in IE 9
  zIndex: 2e9, // The z-index (defaults to 2000000000)
  className: 'spinner', // The CSS class to assign to the spinner
  top: '50%', // Top position relative to parent
  left: '50%', // Left position relative to parent
  shadow: false, // Box-shadow for the lines
  position: 'absolute' // Element positioning
};
var spinnerInstance;
function startSpinner() {
	if(spinnerInstance) {
		//
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


//https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API/Using_the_Web_Storage_API
//https://developer.mozilla.org/en-US/docs/Web/API/Window/localStorage?retiredLocale=nl
//https://developer.mozilla.org/en-US/docs/Web/API/Window/sessionStorage?retiredLocale=nl

function storageAvailable(type) {
	var storage;
	try {
		storage = window[type];
		var x = '__storage_test__';
		storage.setItem(x, x);
		storage.removeItem(x);
		return true;
	}
	catch (e) {
		return e instanceof DOMException && (
			// everything except Firefox
			e.code === 22 ||
			// Firefox
			e.code === 1014 ||
			// test name field too, because code might not be present
			// everything except Firefox
			e.name === 'QuotaExceededError' ||
			// Firefox
			e.name === 'NS_ERROR_DOM_QUOTA_REACHED') &&
			// acknowledge QuotaExceededError only if there's something already stored
			(storage && storage.length !== 0);
	}
}

if (storageAvailable('localStorage')) {
	console.log('Yippee! We can use localStorage awesomeness');
} else {
	console.log('Too bad, no localStorage for us');
}

if (storageAvailable('sessionStorage')) {
	console.log('Yippee! We can use sessionStorage awesomeness');
} else {
	console.log('Too bad, no sessionStorage for us');
}
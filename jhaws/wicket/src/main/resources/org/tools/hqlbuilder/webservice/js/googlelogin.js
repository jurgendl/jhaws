function onGoogleSignIn(googleUser) {
	// Useful data for your client-side scripts:
	var profile = googleUser.getBasicProfile();
	// Don't send this directly to your server!
	console.log("ID: " + profile.getId());
	console.log("Name: " + profile.getName());
	console.log("Image URL: " + profile.getImageUrl());
	console.log("Email: " + profile.getEmail());
	// The ID token you need to pass to your backend:
	console.log("ID Token: " + googleUser.getAuthResponse().id_token);
	
	validateGoogleLogin(googleUser.getAuthResponse().id_token);
};

function validateGoogleLogin(id_token) {
	var xhr = new XMLHttpRequest();
	xhr.open('POST', REST + '/googlelogin/tokensignin');
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.onload = function() {
	  console.log('Signed in as: ' + xhr.responseText);
	};
	xhr.send('idtoken=' + id_token);
};
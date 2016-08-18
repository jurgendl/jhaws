function initJQDatepicker(inputId, countryIsoCode, dateFormat, calendarIcon) {
	//var localizedArray = $.datepicker.regional[countryIsoCode];
	var localizedArray = $.datepicker.regional;
	localizedArray['buttonImage'] = calendarIcon;
	localizedArray['dateFormat'] = dateFormat;
	localizedArray['changeMonth'] = true;
	localizedArray['changeYear'] = true;
	localizedArray['showOn'] = 'button';
	localizedArray['buttonImageOnly'] = true;
	$("#" + inputId).datepicker(localizedArray).datepicker("option", "disabled", false);
};

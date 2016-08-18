function jqueryuifactory(prefix) {
	console.log('jqueryuifactory['+prefix+']');
	$(prefix+'.jquiaccordion').accordion();
	$(prefix+'.jquiautocomplete').autocomplete();
	$(prefix+'.jquibutton').button();
	$(prefix+'.jquibuttonset').buttonset();
	$(prefix+'.jquidatepicker').datepicker();
	$(prefix+'.jquidialog').dialog();
	$(prefix+'.jquimenu').menu();
	$(prefix+'.jquiprogressbar').progressbar();
	$(prefix+'.jquislider').slider();
	$(prefix+'.jquispinner').spinner();
	$(prefix+'.jquitabs').tabs();
	$(prefix+'.jquiselectmenu').selectmenu(); /* added in 6.16 */
	$(prefix+'.feedbackPanel').addClass('ui-state-highlight').addClass('ui-corner-all').addClass('ui-state-error').addClass('ui-state-error-text');
}
$(document).ready(function() {
	jqueryuifactory('');
});
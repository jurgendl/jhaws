;
$('.tempusdominus').datetimepicker({
	locale : moment.locale(),
	calendarWeeks : true,
	buttons : {
		showToday : true,
		showClear : true,
		showClose : true
	},
	widgetPositioning : {
		horizontal : 'left',
		vertical : 'bottom'
	},
	sideBySide : true,
	debug : true,
	enter : function() {
		this.hide();
	},
	escape : function() {
		this.hide();
	}
});
$('.tempusdominusdate').datetimepicker({
	format : 'L'
});
$('.tempusdominustime').datetimepicker({
	format : 'LT'
});
$('.tempusdominusdatetime').datetimepicker({
	format : 'L LT'
});

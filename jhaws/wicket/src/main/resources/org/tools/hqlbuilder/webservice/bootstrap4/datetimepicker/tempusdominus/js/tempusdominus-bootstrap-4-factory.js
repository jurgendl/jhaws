;
$('.tempusdominussimple').datetimepicker();
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
	format : 'L',
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
	debug : true,
	enter : function() {
		this.hide();
	},
	escape : function() {
		this.hide();
	}
});
$('.tempusdominustime').datetimepicker({
	format : 'LT',
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
	debug : true,
	enter : function() {
		this.hide();
	},
	escape : function() {
		this.hide();
	}
});
$('.tempusdominusdatetime').datetimepicker({
	format : 'L LT',
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

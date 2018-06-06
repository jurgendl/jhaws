;
$.fn.datetimepicker.Constructor.Default = $.extend({}, $.fn.datetimepicker.Constructor.Default, {
    icons: {
        time: 'far fa-clock',
        date: 'far fa-calendar',
        up: 'far fa-arrow-up',
        down: 'far fa-arrow-down',
        previous: 'far fa-chevron-left',
        next: 'far fa-chevron-right',
        today: 'far fa-calendar-check-o',
        clear: 'far fa-trash',
        close: 'far fa-times'
    } });
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

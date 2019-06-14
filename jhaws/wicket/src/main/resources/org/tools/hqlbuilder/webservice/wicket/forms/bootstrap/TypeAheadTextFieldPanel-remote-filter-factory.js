console.log(REST + '$URL$');
$('#$ID$').typeahead({
	autoSelect: false,
	highlight: true,
	hint: true,
	items: $MAX$,
	delay : $DELAY$,
	minLength: $MIN$,
	source : function(query, process) {
		var tmp = $.get(REST + '$URL$', {query : query}, function(data) {
			console.log(data);
			return process(data);
		});
		console.log(tmp);
		return tmp;
	},
	matcher: function (item) { return true; },
	// default template
	menu: '<ul class="typeahead dropdown-menu" role="listbox"></ul>',
	item: '<li><a class="dropdown-item" href="#" role="option"></a></li>',
	headerHtml: '<li class="dropdown-header"></li>',
	headerDivider: '<li class="divider" role="separator"></li>',
	itemContentSelector:'a'
});

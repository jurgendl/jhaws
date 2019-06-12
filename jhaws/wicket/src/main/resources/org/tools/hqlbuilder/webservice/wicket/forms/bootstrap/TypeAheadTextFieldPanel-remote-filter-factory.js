console.log(REST + '$URL$');
$('#$ID$').typeahead({
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
	templates : {
		empty : ['<div class="empty-message">','no results','</div>' ].join('\n')
	}
});

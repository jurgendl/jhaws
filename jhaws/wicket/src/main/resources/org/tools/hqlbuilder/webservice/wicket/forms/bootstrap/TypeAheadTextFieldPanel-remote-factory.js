var source_$ID$ = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	remote : {
		wildcard : '%QUERY',
		url : REST + '$URL$' + '%QUERY'
	}
});
console.log(REST + '$URL$' + '%QUERY');
source_$ID$.initialize();
$('#$ID$').typeahead({
	highlight: true,
	hint: true,
	items: $MAX$,
	delay : $DELAY$,
	minLength: $MIN$,
	source: source_$ID$.ttAdapter(),
	templates : {
		empty : ['<div class="empty-message">','no results','</div>' ].join('\n')
	}
});

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
source_$ID$.initialize();
$('#$ID$').typeahead({
	items: 20,
	source: source_$ID$.ttAdapter()
});

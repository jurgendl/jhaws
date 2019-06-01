var sourceoptions_$ID$ = $OPTIONS$;
var source_$ID$ = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	local : sourceoptions_$ID$
});
source_$ID$.initialize();
$('#$ID$').typeahead({
	items: $MAX$,
	delay : $DELAY$,
	minLength: $MIN$,
	source: source_$ID$.ttAdapter()
});
/* FIXME doesn't work */
var sourceoptions_$ID$ = $OPTIONS$;
var source_$ID$ = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	local : sourceoptions_$ID$
});
source_$ID$.initialize();
$('#$ID$').tagsinput({
	trimValue : true,
	typeahead : {
		delay : 200,
		source : source_$ID$.ttAdapter()
	}
});

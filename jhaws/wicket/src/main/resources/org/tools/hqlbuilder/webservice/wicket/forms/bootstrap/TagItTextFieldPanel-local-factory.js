var source_$ID$ = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	local : $OPTIONS$
});
source_$ID$.initialize();
$('#$ID$').tagsinput({
	trimValue : true,
	typeaheadjs : {
		delay : 200,
		source : source_$ID$
	}
});
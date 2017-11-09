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
$('#$ID$').tagsinput({
	trimValue : true,
	allowDuplicates: false,
	freeInput: $FREE$,
	typeaheadjs : {
		delay : $DELAY$,
		items : 20,
		minLength : $MIN$,
		fitToElement : false,
		/*source : source_$ID$.ttAdapter()*/
		source : source_$ID$
	}
});
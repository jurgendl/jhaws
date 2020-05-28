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
	allowDuplicates: false,
	freeInput: $FREE$,
	delimiter: '$DELIMITER$',
	typeahead : {
		highlight: true,
		hint: true,
		items : $MAX$,
		delay : $DELAY$,
		minLength : $MIN$,
		fitToElement : false,
		source : source_$ID$.ttAdapter(),
		templates : {
			empty : ['<div class="empty-message">','no results','</div>' ].join('\n')
		}
	}
});

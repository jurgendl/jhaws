/*var source_$ID$ = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	remote : {
		wildcard : '%QUERY',
		url : REST + '$URL$' + '%QUERY'
	}
});
source_$ID$.initialize();*/
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
		/*source : source_$ID$.ttAdapter()*/
		/*source : source_$ID$,*/
		source: function(query) { console.log(REST+'$URL$'+query); return $.get(REST+'$URL$'+query); },
		templates : {
			empty : ['<div class="empty-message">','no results','</div>' ].join('\n')
		}
	}
});
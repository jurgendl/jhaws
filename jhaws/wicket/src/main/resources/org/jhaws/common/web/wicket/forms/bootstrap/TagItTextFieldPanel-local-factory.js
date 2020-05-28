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
		source : $OPTIONS$,
		templates : {
			empty : ['<div class="empty-message">','no results','</div>' ].join('\n')
		}
	}
});

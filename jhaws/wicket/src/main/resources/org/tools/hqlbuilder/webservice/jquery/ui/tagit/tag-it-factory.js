function tagItJson(selector,optionsUrl) {
	$(selector).hide().tagit({
		singleFieldDelimiter : ',',
		singleField : true,
		allowSpaces : true,
		minLength : 2,
		allowDuplicates : false,
		tagSource : function(request, response) {
			console.log("tagit>>"+request.term);
			$.ajax({
				url : optionsUrl,
				data : {
					query : request.term
				},
				dataType : "json",
				success : function(data) {
					console.log("tagit<<"+data);
					response($.map(data, function(item) {
						return {
							label : item.label,
							value : item.value
						}
					}));
				}
			});
		}
	});
}

jQuery.fn.extend({
	tagIt : function(_tagChoices, _delay, _minLength, _allowSpaces, _caseSensitive, _singleField, _singleFieldDelimiter) {
		var temp = 'A1B2C3';
		return this.each(function() {
			$(this).hide().tagit(
					{
						autocomplete : {
							source : function(request, response) {
								var matcherStr = request.term.replace(new RegExp('\\*', 'g'), temp);
								matcherStr = matcherStr.replace(/\W/g, '');
								matcherStr = $.ui.autocomplete.escapeRegex(matcherStr);
								matcherStr = matcherStr.replace(new RegExp(temp, 'g'), '.*');
								//console.log('matcherStr=' + matcherStr);
								var matcher = new RegExp(matcherStr, 'i');
								response($.grep(_tagChoices, function(item) {
									return matcher.test(item.replace(/\W/g, ''));
								}));
							},
							delay : _delay,
							minLength : _minLength
						},
						allowSpaces : _allowSpaces,
						caseSensitive : _caseSensitive,
						allowDuplicates : false,
						singleField : _singleField,
						singleFieldDelimiter : _singleFieldDelimiter
					});
		});
	}
});

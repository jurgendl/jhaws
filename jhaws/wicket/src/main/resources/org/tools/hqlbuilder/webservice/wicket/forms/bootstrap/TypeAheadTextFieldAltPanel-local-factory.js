$.typeahead({
	input : '#$ID$',
	delay : $DELAY$,
	minLength : $MIN$,
	maxItem: $MAX$,
	accent : true,
	hint : true,
	order : null, // 'asc', 'desc'
	/*compression: true, cache: true, ttl: 3600000,*/ 
	source : {
		data : $OPTIONS$
	},
	/*dynamic : true,*/
	display : [$PROPERTIES$],
	emptyTemplate : "no result for {{query}}",
	template : function(query, item) {
		return "$TEMPLATE$";
	},
	callback : {
		onInit : function(node) {
			console.log('Typeahead Initiated on ' + node.selector);
			node.blur(function() {
				$('#$RESULTS_ID$').html(''); /* hide results info */
				if(node._data) {
					var val = node[0].value;
					for (var i = 0; i < node._data.length; i++) {
						if(node._data[i].name == val) {
							return;
						}
					}
					node[0].value = null; /* no match, clear value */
				}
			})
		},
		onResult : function(node, query, obj, objCount) {
			console.log(objCount)
			var text = "";
			if (query !== "" && query.length>=2) {
				text = '<span class="badge badge-pill badge-light">' + objCount + '</span> elements matching "' + query + '"';
			}
			$('#$RESULTS_ID$').html(text);
		},
		onPopulateSource: function (node, data, group, path) {
			node._data = data;
			return data;
		}
	},
	debug : true
});
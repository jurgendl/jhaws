$.typeahead({
	input : "#$ID$",
	delay : $DELAY$,
	minLength : $MIN$,
	maxItem: $MAX$,
	accent : false,
	hint : false,
	order : null, // 'asc', 'desc'
	dynamic : true,
	filter: false,
	source : {
		ajax: function (query) {
            return {
            	type: "GET",
                url: REST +  "$URL$",
                data: {
                	max: $MAX$,
                	$QUERY$: "{{query}}"
                },
                /*path: '',*/
                /*beforeSend: function (jqXHR, options) {
                    console.log('beforeSend');
                },*/
                callback: {
                    done: function (data) {
                    	console.log(data);
                        return data;
                    },
	                fail: function (jqXHR, textStatus, errorThrown) {console.log('fail');}/*,
	                always :function (data, textStatus, jqXHR) {console.log('always');},
	                then: function (jqXHR, textStatus) {console.log('then');}*/
                }
            };
        }
	},
	display : [$PROPERTIES$],
	emptyTemplate : "no result for {{query}}",
	template : function(query, item) {
		return $TEMPLATE$;
	},
	callback : {
		onInit : function(node) {
			console.log('Typeahead Initiated on ' + node.selector);
			node.blur(function() {
				$('#$RESULTS_ID$').html(''); /* hide results info */
				if(node._data && !$FREE$) {
					var val = node[0].value;
					for (var i = 0; i < node._data.length; i++) {
						if(node._data[i].$PROPERTY$ == val) {
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
				text = '<span class="badge badge-pill badge-light">' + objCount + '</span>';
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
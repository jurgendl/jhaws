var source_$ID$ = new Bloodhound({
	datumTokenizer : function(datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer : Bloodhound.tokenizers.whitespace,
	local : $OPTIONS$
});
source_$ID$.initialize();
$('#$ID$').typeahead({
	selectOnBlur: false,
	autoSelect: false,
	highlight: true,
	hint: true,
	items: $MAX$,
	delay : $DELAY$,
	minLength: $MIN$,
	source: source_$ID$.ttAdapter(),
	// default template
	menu: '<ul class="typeahead dropdown-menu" role="listbox"></ul>',
	item: '<li><a class="dropdown-item" href="#" role="option"></a></li>',
	headerHtml: '<li class="dropdown-header"></li>',
	headerDivider: '<li class="divider" role="separator"></li>',
	itemContentSelector:'a'
});

/* ============= TestPage-factory.js ============= */

/* https://css-tricks.com/indeterminate-checkboxes/ */
;
$('input[type="checkbox"].indeterminate')
		.change(
				function(e) {
					var checked = $(this).prop("checked"), container = $(this)
							.parent(), siblings = container.siblings();
					container.find('input[type="checkbox"].indeterminate')
							.prop({
								indeterminate : false,
								checked : checked
							});
					function checkSiblings(el) {
						var parent = el.parent().parent(), all = true;
						el
								.siblings()
								.each(
										function() {
											return all = ($(this)
													.children(
															'input[type="checkbox"].indeterminate')
													.prop("checked") === checked);
										});
						if (all && checked) {
							parent.children(
									'input[type="checkbox"].indeterminate')
									.prop({
										indeterminate : false,
										checked : checked
									});
							checkSiblings(parent);
						} else if (all && !checked) {
							parent.children(
									'input[type="checkbox"].indeterminate')
									.prop("checked", checked);
							parent
									.children(
											'input[type="checkbox"].indeterminate')
									.prop(
											"indeterminate",
											(parent
													.find('input[type="checkbox"].indeterminate:checked').length > 0));
							checkSiblings(parent);
						} else {
							el.parents("li").children(
									'input[type="checkbox"].indeterminate')
									.prop({
										indeterminate : true,
										checked : false
									});
						}
					}
					checkSiblings(container);
				});





var statesUSA = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California',
	  'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii',
	  'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana',
	  'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota',
	  'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire',
	  'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
	  'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island',
	  'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
	  'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
	];
var states = new Bloodhound({
	datumTokenizer: Bloodhound.tokenizers.whitespace,
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	local: statesUSA});
states.initialize();
$("#typeahead").typeahead({ items: 4, source: states.ttAdapter() });

$("#tagstypeahead").tagsinput({
	  typeahead: {
		  delay: 200,
	    source: statesUSA /* states.ttAdapter() = ERROR https://github.com/bassjobsen/Bootstrap-3-Typeahead/issues/40 */
//	 JSON   source: function(query) {
//	        return $.get("http://someservice.com");
//	      }
	    , afterSelect: function(newVal) {
//	    	console.log('1');
//	    	console.log(this.$element);
//	    	console.log('2a');
//	    	console.log(this.$element[0]);
//	    	console.log('3a');
//	    	console.log(this.$element[0].lastChild);
//	    	$(this.$element[0].lastChild).empty();
//	    	$(this.$element[0]).text('');
//	    	console.log('2b');
//	    	console.log(this.$element[0]);
//	    	console.log('3b');
//	    	console.log(this.$element[0].childNodes[0]);
//	    	console.log();
	    }
	  }
	});


var multiselectoptions = [
    {
        label: 'Group 1', children: [
            {label: 'Option 1.1', value: '1-1', selected: true},
            {label: 'Option 1.2', value: '1-2'},
            {label: 'Option 1.3', value: '1-3'}
        ]
    },
    {
        label: 'Group 2', children: [
            {label: 'Option 2.1', value: '1'},
            {label: 'Option 2.2', value: '2', selected: true},
            {label: 'Option 2.3', value: '3', disabled: true}
        ]
    }
];
/* https://github.com/davidstutz/bootstrap-multiselect/issues/741 */
$('.multiselect').multiselect({
    buttonClass: 'btn btn-secondary btn-sm',
    enableFiltering: true,
    enableCaseInsensitiveFiltering: true,
    includeSelectAllOption: true, 
    nonSelectedText: 'None selected.',
    allSelectedText: 'All selected.',
    enableClickableOptGroups: true,
    enableCollapsibleOptGroups: true,
    templates: { 
        //button: '<button type="button" class="multiselect dropdown-toggle" data-toggle="dropdown"></button>',
        ul: '<ul class="pr-2 multiselect-container dropdown-menu"></ul>',
        filter: '<li class="multiselect-item filter"><div class="input-group input-group-sm"><div class="input-group-prepend"><span class="input-group-text"><i class="fa-fw fas fa-search"></i></span></div><input class="form-control multiselect-search" type="text"></div></li>',
        filterClearBtn: '<div class="input-group-append"><button class="btn btn-sm btn-default multiselect-clear-filter" type="button"><i class="fa-fw fas fa-remove"></i></button></div>',
        li: '<li><a tabindex="0" class="dropdown-item"><label style="width:100%" class="pl-2 pr-2"></label></a></li>',
        //divider: '<li class="multiselect-item divider"></li>',
        //liGroup: '<li class="multiselect-item group"><label class="multiselect-group"></label></li>',
        dummy: null
    }
  })
  //.multiselect('dataprovider', multiselectoptions)
  ;
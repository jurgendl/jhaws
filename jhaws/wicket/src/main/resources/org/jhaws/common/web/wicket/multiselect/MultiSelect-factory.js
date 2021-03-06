/*var multiselectoptions = [
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
];*/
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
    maxHeight: 200,
    templates: { 
        //button: '<button type="button" class="multiselect dropdown-toggle" data-toggle="dropdown"></button>',
        ul: '<ul class="pr-2 multiselect-container dropdown-menu"></ul>',
        filter: '<li class="multiselect-item filter"><div class="input-group input-group-sm"><div class="input-group-prepend"><span class="input-group-text"><span class="fa-fw fas fa-search"></span></span></div><input class="form-control multiselect-search" type="text"></div></li>',
        filterClearBtn: '<div class="input-group-append"><button class="btn btn-sm btn-default multiselect-clear-filter" type="button"><span class="fa-fw fas fa-remove"></span></button></div>',
        li: '<li><a tabindex="0" class="dropdown-item"><label style="width:100%" class="pl-2 pr-2"></label></a></li>',
        //divider: '<li class="multiselect-item divider"></li>',
        //liGroup: '<li class="multiselect-item group"><label class="multiselect-group"></label></li>',
        dummy: null
    }
  })
  //.multiselect('dataprovider', multiselectoptions)
  ;
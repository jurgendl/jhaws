// hier worden alle instanties van typeahead in bewaard
const typeaheadInstances = new Map();

// als je op het vergrootglas klikt om oopnieuw te zoeken
function typeAheadReQuery(_THIS) {
	//console.log("typeAheadReQuery");
	let __THIS = $(_THIS); // jquer wrapper rond 'this'
	let TA_GROUP = __THIS.parents('.typeahead-container'); // ga naar component waar alles in zit
	let TA_INPUT = TA_GROUP.find('.typeahead__field').find('input'); // input veld waardat typeahead op geinit is
	//let typeaheadInstance = typeaheadInstances.get('#'+TA_INPUT.attr('id')); // zoek typeahead instanceop
	//typeaheadInstance.search();
	//console.log("typeAheadReQuery","TA_INPUT",TA_INPUT);
	TA_INPUT.focus();//zoek input focusen
	TA_INPUT.trigger('input.typeahead');
}

// defaults, is geen constante want misschien wil iemand deze defaults overschrijven
var taMapDefauls = new Map(); {
	// typeahead settings
	taMapDefauls.set('min', 2);
	taMapDefauls.set('delay', 500);
	taMapDefauls.set('emptyText', '---');
	//taMapDefauls.set('moreText', '...');
	taMapDefauls.set('max', 10);
	//taMapDefauls.set('backdrop',true);
	// REST call settings
	// typeahead/?max={max}&query={query}&lang={lang}
	taMapDefauls.set('query', '{{query}}'); 
	taMapDefauls.set('lang', $('html').attr('lang')); // staat als attribuut op html tag
	// DTO settings
	taMapDefauls.set('htmlProperty', '{{html}}'); // DTO.html
	taMapDefauls.set('idProperty', 'uid'); // DTO.uid
	taMapDefauls.set('displayProperty', 'display'); // DTO.display
	// andere settings
	taMapDefauls.set('onlySuccessfulMatch', true);
	taMapDefauls.set('markSuccessfulSearch', true);
	taMapDefauls.set('onBlur', true);
	taMapDefauls.set('newAsButton', false);
	taMapDefauls.set('hasSuccessClass', 'is-valid');
}

// factory call config:
//
//field: '#typeahead-persoonUid', // verplicht
//rest: '/persoon/typeahead/', // verplicht
//targetField: '#persoonUid', // naar dit veld wordt ofwel id ofwel uid gezet, hidden field
//moreText: '-- Meer resultaten gevonden, verfijn eventueel uw zoekterm. --' // optioneel, dan wordt '...' getoond, MOET GELOCALISEERD WORDEN, je kan hier {{query}} gebruiken
//emptyText: '-- Geen resultaten. --', // optioneel, dan wordt '---' getoond, MOET GELOCALISEERD WORDEN
//newText: '-- Andere toevoegen. --' // optioneel, MOET GELOCALISEERD WORDEN, heeft voorrang op 'emptyText' wanneer geen resultaat
//placeholder: 'Zoeken' // optioneel, anders Zoeken/Search
//restReturnPath: null // optioneel (dan is hetgeen de REST call return de result list), als collectie van waarden genest is op dit pad
//
// factory call
function typeaheadFactory(_taVars) { 
	// check verplichte config
	if(!_taVars) alert('config verplicht');
	
	// merge config met defaults
	const taVars = mapNaarJson(mergeMappen(taMapDefauls,jsonNaarMap(_taVars)));

	// debugging
	if(taVars.debug) console.log('typeaheadFactory-incoming-settings:',_taVars);
	if(taVars.debug) console.log('typeaheadFactory-default-settings:',taMapDefauls);
	if(taVars.debug) console.table(taVars);
	else console.log('typeaheadFactory-settings:',taVars);
	
	// check verplichte config
	if(!taVars.field) alert('config field verplicht');
	if(!taVars.rest) alert('config rest verplicht');
	if(!taVars.targetField) alert('config targetField verplicht');
	if(!taVars.idProperty) alert('config idProperty verplicht');
	
	// placeholder localiseren
	if(taVars.placeholder) $(taVars.field).attr('placeholder',taVars.placeholder);
	else if('en'==taVars.lang) $(taVars.field).attr('placeholder','Search');
	else $(taVars.field).attr('placeholder','Zoeken');
	
	// typeahead setup
	const typeaheadInstance = $.typeahead({
		/*backdrop: {
			"opacity": 0.15,
			"filter": "alpha(opacity=15)",
			"background-color": "#eaf3ff"
		},*/
		//blurOnTab:true,
		display: [taVars.displayProperty],
		debug: taVars.debug,
		input: taVars.field,
		minLength: taVars.min,
		maxItem: taVars.max,
		delay: taVars.delay,
		hint: false,
		order: null,// sorteren gebeurt serverside, normaal 'asc' of 'desc'
		dynamic: true,// doe REST calls
		filter: false,// filteren gebeurt serverside
		cache: false,// resultaten zijn altijd live, EL is snel genoeg
		// accent : false,
		emptyTemplate: function (query) {
			/* zie --1-- */
			if(taVars.debug) console.table({
				'Typeahead.emptyTemplate':'-',
				'taVars.emptyText':taVars.emptyText,
				'taVars.newText':taVars.newText,
				'taVars.triggerOnNew':taVars.triggerOnNew,
				'taVars.newAsButton':taVars.newAsButton
			});
			if(taVars.newText) {
				if(taVars.triggerOnNew) {
					if(taVars.newAsButton) {
						return $('<li/>', {
							"class": "typeahead__item typeahead__group-group"
						}).append(
							$('<a/>', {
								"text": taVars.newText,
								"class": "btn btn-primary",
								"data-on-click": taVars.triggerOnNew,
								"style": "color: var(--dark-blue); display: inline-block; margin: 5px; padding: 13px 15px 12px 15px;",
								"onclick": "event.preventDefault();$($(this).attr('data-on-click')).click();"
							})
						);
					} else {
						return $('<li/>', {
							"text": taVars.newText,
							"class": "typeahead__item typeahead__group-group extra_items extra_items_add",
							"style": "cursor: pointer;",
							"data-on-click": taVars.triggerOnNew,
							"onclick": "event.preventDefault();$($(this).attr('data-on-click')).click();"
						});
					}
				} else {
					return $('<li/>', {
						"text": taVars.newText,
						"class": "typeahead__item typeahead__group-group extra_items extra_items_add",
						"style": "cursor: pointer;"
					});
				}
			} else {
				return $('<li>', {
					"text": taVars.emptyText,
					"class": ""
				});
			}
		},
		template: function(query, item) { return taVars.htmlProperty; },
		source: {
			ajax: function(query) {
				return {
					type: "GET",
					url: REST + taVars.rest, // REST = staat als meta tag in html en wordt door js in const gezet
					path: taVars.restReturnPath, // optioneel (dan is hetgeen de REST call return de result list), als collectie van waarden genest is op dit pad
					data: // query parameters
						mapNaarJson(
							mergeMappen(
								jsonNaarMap({
									max: taVars.max,//
									query: taVars.query,//
									lang: taVars.lang // $("html").attr("lang")
								}),
								jsonNaarMap(taVars.extraRestParameters) // extra hardcoded waarden toevoegen komende van config die binnen komt in function
							)
						)
					//{ // query parameters
						//max: taVars,//
						//query: taVars.query,//
						//lang: taVars.lang // $("html").attr("lang")
					//}
					,
					beforeSend: function (jqXHR, options) {
						//if(taVars.debug) console.log('beforeSend',jqXHR, options);
					},
					callback: {
						done: function(data) {
							//if(taVars.debug) console.log('Typeahead.ajax.done',data);
							typeaheadInstances.get(taVars.field).node._json = data;
							return data;
						},
						fail: function(jqXHR, textStatus, errorThrown) {
							//if(taVars.debug) console.log('Typeahead.ajax.fail',jqXHR, textStatus, errorThrown);
						},
						always: function (data, textStatus, jqXHR) {
							//if(taVars.debug) console.log('always');
						},
						then: function (jqXHR, textStatus) {
							//if(taVars.debug) console.log('then');
						}
					}
				};
			}
		},
		callback: {
			// Will be executed on Typeahead initialization, before anything else.
			onInit: function (node) {
				if(taVars.debug) console.table({
					'Typeahead.onInit':'-',
					'taVars.onBlur':taVars.onBlur
				});
				if(taVars.onBlur) node.blur(function() {
					if(taVars.debug) console.table({
						'Typeahead.onBlur':'-', 
						'node._data':node._data,
						'node._selectedIdentification':node._selectedIdentification
					});
					
					if(node._selectedIdentification) {
						// WEL value
					} else {
						// GEEN value
						if(taVars.onlySuccessfulMatch) {
							let typeaheadInstance = typeaheadInstances.get(taVars.field);
							typeaheadInstance.resetInput(); // zet waarde in zoeker op null // of $(taVars.field).val(null); 
							typeaheadInstance.toggleCancelButtonVisibility(); // verbergt x (cancel button)
							$(taVars.targetField).val(null); // zet waarde in hidden formfield op null
						}
						if(taVars.markSuccessfulSearch) $(taVars.field).parent().removeClass(taVars.hasSuccessClass); // verwijder groene boord
						$(taVars.field).parents('.typeahead__container').parent().children('.typeahead__resultcontainer').html(''); // hide results info
					}
				});
			},
			// When the result container is displayed
			// !!!!! dus bij zoeken, NIET bij selecteren van een waarde !!!!!
			onResult: function(node, query, obj, objCount, resultCountPerGroup) {
				if(taVars.debug) console.table({
					'Typeahead.onResult':'-',
					'query':query, 
					'obj':obj, 
					'objCount':objCount,
					'resultCountPerGroup':resultCountPerGroup
				});
				
				node._query = query;
				node._object = obj;
				node._count = objCount;
				node._countPerGroup = resultCountPerGroup;
				
				node._selectedIdentification = null;
				$(taVars.targetField).val(null);
				if(taVars.markSuccessfulSearch) $(taVars.field).parent().removeClass(taVars.hasSuccessClass); // verwijder groene boord
				if(query.length>=taVars.min&&node._json&&node._json.totalResults) $(taVars.field).parents('.typeahead__container').parent().children('.typeahead__resultcontainer').html(Math.min(taVars.max,objCount)/*node._json.totalResults*/+' resultaten.');
				else $(taVars.field).parents('.typeahead__container').parent().children('.typeahead__resultcontainer').html('');
			},
			//Gets called after the Ajax requests are all received and the data is populated inside Typeahead. 
			//This is the place where extra parsing or filtering should occure before the data gets 
			//available inside any Typeahead query
			//For example, the Backend sends the "display" key separated by underscores "_" instead of spaces " ".
			//The data parameter HAS to be returned after it's transformed.
			onPopulateSource: function(node, data, group, path) {
				if(taVars.debug) console.table({
					'Typeahead.onPopulateSource':'-',
					'data':data,
					'group':group,
					'path':path
				});
				
				// Only apply the change for a specific group
				/*if (group === "mySourceGroup") {
					for (var i = 0; i < data.length; i++) {
						data[i].compiled = data[i].compiled.replace(/_/g, ' ');
						data[i].display = data[i].compiled.replace(/_/g, ' ');
					}
				}*/
				
				node._data = data;
				
				return data;
			},
			// Possibility to e.preventDefault() to prevent the Typeahead behaviors
			// Will be executed when a result item is clicked or the right arrow is pressed when an item is 
			// selected from the results list. This function will trigger before the regular behaviors.
			onClickBefore: function(node, a, item, event) {
				if(taVars.debug) console.table({
					'Typeahead.onClickBefore':'-',
					'a':a,
					'item':item,
					'event':event
				});
			},
			// Happens after the default clicked behaviors has been executed
			// Will be executed when a result item is clicked or the right arrow is pressed when an item is 
			// selected from the results list. This function will trigger after the regular behaviors.
			onClickAfter: function(node, a, item, event) {
				if(taVars.debug) console.table({
					'Typeahead.onClickAfter':'-',
					'a':a,
					'item':item,
					'item[taVars.idProperty]':item[taVars.idProperty],
					'taVars.markSuccessfulSearch':taVars.markSuccessfulSearch,
					'taVars.targetField':taVars.targetField,
					'taVars.triggerOnSelect':taVars.triggerOnSelect,
					'taVars.resetOnTriggerOnSelect':taVars.resetOnTriggerOnSelect
				});

				node._selectedIdentification = item[taVars.idProperty];
				$(taVars.targetField).val(item[taVars.idProperty]);
				if(taVars.markSuccessfulSearch) $(taVars.field).parent().addClass(taVars.hasSuccessClass);
				$(taVars.field).parents('.typeahead__container').parent().children('.typeahead__resultcontainer').html(''); // hide results info

				if(taVars.triggerOnSelect) {
					$(taVars.triggerOnSelect).click();

					if(taVars.resetOnTriggerOnSelect) {
						let typeaheadInstance = typeaheadInstances.get(taVars.field);
						typeaheadInstance.resetInput(); // zet waarde in zoeker op null // of $(taVars.field).val(null); 
						typeaheadInstance.toggleCancelButtonVisibility(); // verbergt x (cancel button)
						$(taVars.targetField).val(null); // zet waarde in hidden formfield op null
						node._selectedIdentification = null;
						if(taVars.markSuccessfulSearch) $(taVars.field).parent().removeClass(taVars.hasSuccessClass); // verwijder groene boord
						$(taVars.field).parents('.typeahead__container').parent().children('.typeahead__resultcontainer').html(''); // hide results info
					}
				}
			},
			// When a key is pressed to navigate the results.
			// It is possible to disable the input text change when using up and down arrows when event.preventInputChange is set to true
			onNavigateBefore: function(node, query, event) {
				if(taVars.debug) console.table({
					'Typeahead.onNavigateBefore':'-',
					'query':query,
					'event':event
				});
				
				//if (~[38,40].indexOf(event.keyCode)) {
				//	event.preventInputChange = true;
				//}
			},
			//When the result HTML is build, modify it before it get showed. 
			//This callback should be used to modify the result DOM before it gets inserted into Typeahead.
			// If you are using this callback, the resultHtmlList param needs to be returned at the end of your function.
			onLayoutBuiltBefore: function (node, query, result, resultHtmlList) {
				if(taVars.debug) console.table({
					'Typeahead.onLayoutBuiltBefore':'-',
					'query':query,
					'result':result,
					'resultHtmlList':resultHtmlList,
					'taVars.max':taVars.max,
					'node._count':node._count,
					'node._json.totalResults':node._json.totalResults,
					'taVars.moreText':taVars.moreText,
					'taVars.newText':taVars.newText,
					'taVars.newAsButton':taVars.newAsButton,
					'MORE':(resultHtmlList && taVars.moreText && node._json.totalResults>taVars.max),
					'NEW':(taVars.newText && resultHtmlList /* zie --1-- */ && node._count>0 /* zie --1-- */)
				});
				
				// Replace all underscore "_" by spaces in the result list
				/*$.each(resultHtmlList.find('a'), function (i, a) {
						a.text = a.text.replace(/_/g, ' ');
					});
				return resultHtmlList;*/
				
				// voeg "meer resultaten" toe
				if(resultHtmlList && taVars.moreText && node._json.totalResults > taVars.max) {
					resultHtmlList.append($('<li/>', {
						"text": taVars.moreText.replace('{{total}}',node._json.totalResults),
						"class": "typeahead__item typeahead__group-group extra_items extra_items_more",
						"style": "padding: 0.5rem 0.75rem;"
					}));
				}
				
				// voeg "nieuw toevoegen" toe als er op z'n minst 1 resultaat is
				// wanneer er geen resultaat is wordt dat gedaan in emptytemplate
				// zie --1-- 
				if(taVars.newText && resultHtmlList /* zie --1-- */ && node._count > 0 /* zie --1-- */) {
					let liEl;
					if(taVars.triggerOnNew) {
						if(taVars.newAsButton) {
							liEl = $('<li/>', {
								"class": "typeahead__item typeahead__group-group"
							}).append(
								$('<a/>', {
									"text": taVars.newText,
									"class": "btn btn-primary",
									"data-on-click": taVars.triggerOnNew,
									"style": "color: var(--dark-blue); display: inline-block; margin: 5px; padding: 13px 15px 12px 15px;",
									"onclick": "event.preventDefault();$($(this).attr('data-on-click')).click();"
								})
							);
						} else {
							liEl = $('<li/>', {
								"text": taVars.newText,
								"class": "typeahead__item typeahead__group-group extra_items extra_items_add",
								"style": "cursor: pointer;",
								"data-on-click": taVars.triggerOnNew,
								"onclick": "event.preventDefault();$($(this).attr('data-on-click')).click();"
							});
						}
					} else {
						liEl = $('<li/>', {
							"text": taVars.newText,
							"class": "typeahead__item typeahead__group-group extra_items extra_items_add",
							"style": "cursor: pointer;"
						});
					}
					// --1--
					// werkt niet omdat het gereturnde niet in de waarde gestoken wordt waarmee het binnen komt
					// daar wordt de empty results vervangen door dit element bij 0 resultaten
					//if(!resultHtmlList) {
					//	resultHtmlList = $('<ul/>', {"class": "typeahead__list"});
					//}
					resultHtmlList.append(liEl);
				}

				return resultHtmlList;
			},
			//Perform an action right after the result HTML gets inserted into Typeahead's DOM.
			onLayoutBuiltAfter: function (node, query, result, resultHtmlList) {
				/*if(taVars.debug) console.table({
					'Typeahead.onLayoutBuiltAfter':'-',
					'query':query,
					'result':result!==null,
					'resultHtmlList':resultHtmlList
				});*/
			},
			// Triggered if the typeahead had text inside and is cleared
			onCancel: function (node, event) {
				if(taVars.debug) console.table({
					'Typeahead.onCancel':'-',
					'event':event
				});
				node._selectedIdentification = null;
				$(taVars.targetField).val(null);
				if(taVars.markSuccessfulSearch) $(taVars.field).parent().removeClass(taVars.hasSuccessClass); // verwijder groene boord
				$(taVars.field).parents('.typeahead__container').parent().children('.typeahead__resultcontainer').html(''); // hide results info
			}
		}
	});
	
	// steek in instanties onder de css selector (meestal id, inclusief #)
	typeaheadInstances.set(_taVars.field,typeaheadInstance);
}
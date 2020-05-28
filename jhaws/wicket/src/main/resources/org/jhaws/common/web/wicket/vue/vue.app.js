function appVueProcessResponse(response) {
	var vueApplication = this;
	if (response && response['data'] && response['data']['elements']) {
		if (vueApplication.app) {
			var newLen = response['data']['elements'].length;
			var oldLen = vueApplication.app.elements.length;
			//console.log('start '+newLen+'/'+oldLen);
			var stack = [];
			for (var oldIdx = 0; oldIdx < oldLen; oldIdx++) {
				var oldId = vueApplication.app.elements[oldIdx].id;
				var updated = false;
				for (var newIdx = 0; newIdx < newLen; newIdx++) {
					var newId =  response['data']['elements'][newIdx].id;
					if(oldId==newId){
						//console.log('updating '+oldIdx+'/'+newIdx+'/'+oldId);
						Vue.set(vueApplication.app.elements, oldIdx, response['data']['elements'][newIdx]);
						updated = true;
						break;
					}
				}
				if(!updated){
					//console.log('queue to remove '+oldIdx+'/'+oldId);
					stack.push(oldIdx);
				}
			}
			var idxToRemove;
			while(undefined!==(idxToRemove=stack.pop())){
				//console.log('removing '+idxToRemove);
				vueApplication.app.elements.splice(idxToRemove,1);
			}
			oldLen = vueApplication.app.elements.length;
			for (var newIdx = 0; newIdx < newLen; newIdx++) {
				var newId =  response['data']['elements'][newIdx].id;
				var updated = false;
				for (var oldIdx = 0; oldIdx < oldLen; oldIdx++) {
					var oldId = vueApplication.app.elements[oldIdx].id;
					if(oldId==newId){
						updated = true;
						break;
					}
				}
				if(!updated){
					//console.log('adding '+newId);
					vueApplication.app.elements.push(response['data']['elements'][newIdx]);
				}
			}
		} else {
			vueApplication.app = new Vue({ 
				el : '#'+vueApplication.domId, 
				data : {
					filter : {
						showFilter : true,
						showAll : true,
						search : '',
						passesFilter : function (element) {
							var _s = this.search;
							if(!_s) return true;
							if(_s=='') return true;
							_s = _s.toUpperCase();
							if(element.title && element.title.toUpperCase().indexOf(_s)>-1) return true;
							if(element.subtitle && element.subtitle.toUpperCase().indexOf(_s)>-1) return true;
							if(element.description && element.description.toUpperCase().indexOf(_s)>-1) return true;
							return false;
						}
					},
					elements : response['data'].elements,
					background : function (element) {
						if(element.state=='waiting') return 'bg-light';
						if(element.state=='busy') return 'bg-info text-white';
						if(element.state=='error') return 'bg-warning';
						if(element.state=='success') return 'bg-success text-white';
						return 'bg-dark text-white';
					}
				},
			});
			$('#'+vueApplication.domId).attr('style', '');
		}
	}
}

function appVueRestCall() {
	var vueApplication = this;
	$.ajax({
		type : 'GET',
		url : vueApplication.restUrl+'/list',
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		success : function(response, textStatus, jqXHR) {
			vueApplication.restCallResponse(response);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log('ERROR: ' + xhr.status + ',' + thrownError);
		}
	});
}

function appVueRestRemoveElement(_this) {
	var vueApplication = this;
	$.post(vueApplication.restUrl+'/remove/'+ $(_this).parent().attr('id'),function(data){});
}

function appVueStart() {
	var vueApplication = this;
	vueApplication.restCall();
	vueApplication.interval = window.setInterval(function(){vueApplication.restCall();}, vueApplication.intervalTime);
}

function appVueStop() {
	var vueApplication = this;
	window.clearInterval(vueApplication.interval);
	vueApplication.interval = undefined;
}

function AppVue(_restUrl,_domId,_intervalTime=1000) {
	var appVue = {
		// required
		restUrl: undefined,
		domId: undefined,
		// optional
		intervalTime: 1000,
		// internal
		app: undefined,
		interval: undefined,
		// functions
		start: appVueStart,
		stop: appVueStop,
		restCall: appVueRestCall,
		restCallResponse: appVueProcessResponse,
		remove: appVueRestRemoveElement
	};
	var _appVue = Object.create(appVue);
	_appVue.intervalTime = _intervalTime;
	_appVue.restUrl = _restUrl;
	_appVue.domId = _domId;
	_appVue.start();
	return _appVue;
}
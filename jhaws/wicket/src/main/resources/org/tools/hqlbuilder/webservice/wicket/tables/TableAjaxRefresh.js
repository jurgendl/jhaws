/* http://kyleschaeffer.com/development/the-perfect-jquery-ajax-request/ */
/* http://stackoverflow.com/questions/377644/jquery-ajax-error-handling-show-custom-exception-messages */
/* http://code.tutsplus.com/tutorials/5-ways-to-make-ajax-calls-with-jquery--net-6289 */
/* http://www.javabeat.net/jquery-ajaxsetup-example/ */
/* http://www.javabeat.net/jquery-getjson-example/ */

function tableAjaxRefreshCall(tableAjaxRefreshMeta) {
	$.ajax({
		type : tableAjaxRefreshMeta['type']
		,
		url : tableAjaxRefreshMeta['url']
		,
		dataType : 'json'
		,
		data : {
			time : tableAjaxRefreshMeta['update-since']
		}
		,
		beforeSend : function() {
			tableAjaxRefreshMeta['update-start'] = $.now();
		}
		,
		success : function(response) {
			try {
				$.each(response, function(rowIdx, record) {
					var oid = record[tableAjaxRefreshMeta['oidProperty']];
					var htmlRecNode = $('tr[data-id='+oid+']');
					$.each(tableAjaxRefreshMeta['config'], function(property, propertyConfigMap) {
						var value = record[property];
						if(value) {
							var col = propertyConfigMap['idx'];
							var dataNode = $(htmlRecNode.children()[col]).children('div');
							var currentValue = dataNode.html();
							if(currentValue!=value) {
								console.log(oid+':('+property+'='+col+'):'+currentValue+'>'+value);
								dataNode.empty().append(value);
								if(propertyConfigMap['data']){
									dataNode.parent().attr('data-'+property, value);
									console.log(oid+':'+'data-'+property+'='+value);
								}
							}
						}
					});
				});
				tableAjaxRefreshMeta['update-since'] = tableAjaxRefreshMeta['update-start'];
			} catch (e) {
				window.clearInterval(tableAjaxRefreshMeta['intervalID']);
				console.log(tableAjaxRefreshMeta['intervalID']+' stopped because of '+e);
			}
		}
		,
		error : function(xhr, ajaxOptions, thrownError) {
			window.clearInterval(tableAjaxRefreshMeta['intervalID']);
			console.log(tableAjaxRefreshMeta['intervalID']+' stopped because of '+xhr.status+' - '+xhr.responseText+' - '+thrownError);
		}
	});
}

$(document).ready(function() {
	$.each( tableAjaxRefresh, function( tableId, tableAjaxRefreshMeta ) {
		console.log(tableId+'::'+tableAjaxRefreshMeta['type']+'::'+tableAjaxRefreshMeta['url']+'::'+tableAjaxRefreshMeta['oidProperty']);
		tableAjaxRefreshMeta['update-since'] = $.now();
		tableAjaxRefreshMeta['intervalID'] = window.setInterval( function() { tableAjaxRefreshCall(tableAjaxRefreshMeta); }, tableAjaxRefreshMeta['refresh'] * 1000);
	});
});

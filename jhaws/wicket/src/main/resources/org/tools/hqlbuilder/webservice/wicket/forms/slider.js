function sliderWidget(id) {
	var input = $('#'+id).hide();
	var slider = $("<span class='slider' id='" + id + "_slider'></span>");
	var output = $("<output for=" + id + " id='" + id + "_slider_output'></output>");
	var minV = 0;
	var maxV = 100;
	var stepV = 1;
	var valV = parseInt(input.val()) || 0;
	if(input.prop("type") == "range") {
		minV = parseInt(input.prop("min"));
		maxV = parseInt(input.prop("max"));
		stepV = parseInt(input.prop("step"));
	}
	slider.insertAfter(input).slider({
						min : minV,
						max : maxV,
						step: stepV,
						value : valV,
						range : "min",
						slide : function(event, ui) { input.val(ui.value); output.val(ui.value); }
					});
	output.addClass('pui-inputtext ui-widget ui-state-default ui-corner-all').insertAfter(slider).val(valV);
	$('#'+id).change(function() { var valV_ = parseInt(input.val()) || 0; slider.slider("value", valV_); output.val(valV_); });
}
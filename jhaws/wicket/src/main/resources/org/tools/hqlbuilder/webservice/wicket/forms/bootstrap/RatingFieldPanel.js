$('input[type=number].ratinginput').each(function(index) {
	var _this = $(this);
	console.log(_this);
	var _this_val = _this.val();
	if(!_this_val) _this_val='0';
	console.log(_this_val);
	var _this_name = _this.attr('name');
	console.log(_this_name);
	var _this_id = _this.attr('id');
	console.log(_this_id);
	var _this_max = _this.attr('max');
	console.log(_this_max);
	var ratingHtml = '';
	ratingHtml += '<span class="ratinginput">';
	for (var i = 1; i <= _this_max; i++) {
		ratingHtml += '<label>';
		ratingHtml += '<input type="radio" class="ratingradio" ';
		if(_this_id) ratingHtml += 'id="'+_this_id+'" ';
		if(_this_name) ratingHtml += 'name="'+_this_name+'" ';
		ratingHtml += 'value="' + i + '" />';
		for (var j = 1; j <= i; j++) {
			ratingHtml += '<span class="rating fa fa-star"></span>';
		}
		ratingHtml += '</label>';
	}
	ratingHtml += '</span>';
	console.log(ratingHtml);
	_this.replaceWith(ratingHtml);
});
/*$('.ratinginput :radio').change(function() {
	console.log('New star rating: ' + this.value);
});*/
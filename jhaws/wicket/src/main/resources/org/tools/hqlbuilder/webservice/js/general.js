/* http://stackoverflow.com/questions/8579643/simple-jquery-scroll-to-anchor-up-or-down-the-page */
function anchorDoScroll(dest) {
	$('html,body').animate({scrollTop:$('a[name='+dest+']').offset().top},1000);/*,'easeInOutCirc'*/
	console.log("scroll to:"+dest+"->"+$('a[name='+dest+']').offset().top);
}
function anchorFactory() { // put this in document ready
	$("a[href^=#]").click(function(e) {
		e.preventDefault();
		anchorDoScroll($(this).attr('href').substring(1));
	});
}

function compress(data) {
	data = data.replace(/([^&=]+=)([^&]*)(.*?)&\1([^&]*)/g, "$1$2,$4$3");
	return /([^&=]+=).*?&\1/.test(data) ? compress(data) : data;
}

$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (this.value == 'on')
			this.value = true;
		if (this.value == 'off')
			this.value = false;
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

function jsonForm(id) {
	// .serializeArray()
	// .serialize()
	return JSON.stringify($(id).serializeObject());
}

function replaceAll(str, find, replace) {
	return str.replace(new RegExp(find, 'g'), replace);
}

function escapeRegExp(string) {
	return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

function selectedOption(selector) {
	return $(selector+' option:selected').text();
}

function selectedRadioOption(name) {
	return $('input[name='+name+'][type=radio]:checked').val();
}

// http://jsfiddle.net/Marcel/jvJzX/
jQuery.fn.extend({
	inputFocus : function() {
		return this.each(function() {
			if(this.nodeName == 'INPUT') {
				if($(this).attr('type') == 'text') {
					$(this).focus(function() {
						$(this).select().mouseup(function(event) {
							event.preventDefault();
							$(this).unbind("mouseup");
						});
					});
				}
			}
		});
	}
});

// https://asimilia.wordpress.com/2008/12/17/jquery-extend-confusion/
jQuery.fn.extend({
	resetform : function() {
		var form = $(this);
		form.find('input[type][value]').val('');
		form.find('input[type=checkbox]:checked').removeAttr('checked');
		form.find('select option:selected').removeAttr('selected');
	}
});

jQuery.fn.extend({
	// http://stackoverflow.com/questions/556767/limiting-number-of-lines-in-textarea
	// http://stackoverflow.com/questions/5271782/how-to-disable-the-resize-grabber-of-an-html-textarea
	fixTextArea : function() {
		var fixedTextArea = $(this);
		fixedTextArea.css('resize','none');
		fixedTextArea.scroll(function(){
			$(this).css('overflow','hidden');/* for the mozilla browser problem */
			$(this).animate({scrollTop:$(this).outerHeight()});
			while($(this).scrollTop()>0){/* for the copy and paste case */
				var lines=$(this).val().slice(0,-1);
				$(this).val(lines);
			}
			$(this).css('overflow','auto');/* For the mozilla browser problem */
		});
	}
});

/* http://codepen.io/anon/pen/NPZNPq */
function toggleByRadio(group) {
	$("[data-group='" + group + "']").addClass("optiontoggle").addClass("hidden");
	$("input[type='radio'][name='" + group + "']").click(function() { adjustByRadio(group); });
	adjustByRadio(group);
}
function toggleByRadioJqueryUIFix(group) {
	$("input[type='radio'][name='" + group + "']").parent().on('change', function(event){ adjustByRadio(group); });
}
function adjustByRadio(group) {
	var V = $("input[type='radio'][name='" + group + "']:checked").val();
	console.log(group+'='+V);
	$("[data-group='" + group + "'][data-value!='" + V + "']").addClass("hidden");
	$("[data-group='" + group + "'][data-value='" + V + "']").removeClass("hidden");
}

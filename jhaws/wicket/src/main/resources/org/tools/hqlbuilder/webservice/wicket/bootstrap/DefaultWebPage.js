/* ============= DefaultWebPage.js ============= */

/*
	<div id="id1" class="collapsed" onclick="toggle_visibility('id1','id2')">toggle visibility</div>
	<div id="id2" class="hidden"></div>
*/
/*
	<div id="id1" class="expanded" onclick="toggle_visibility('id1','id2')">toggle visibility</div>
	<div id="id2"></div>
*/

function toggle_visibility(expander,collapsible) {
	expandertag = $("[id="+expander+"]");
	tagplus = $(expandertag).children("svg.fa-plus-square");
	console.log(tagplus);
	tagminus = $(expandertag).children("svg.fa-minus-square");
	console.log(tagminus);
	collapsibletag = $("[id="+collapsible+"]");
	ishidden = collapsibletag.hasClass( "hidden" );
	if( ishidden ) {
		collapsibletag.removeClass( "hidden" );
		expandertag.removeClass( "collapsed" );
		expandertag.addClass( "expanded" );
		tagplus.addClass( "hidden" );
		tagminus.removeClass( "hidden" );
	} else {
		collapsibletag.addClass( "hidden" );
		expandertag.removeClass( "expanded" );
		expandertag.addClass( "collapsed" );
		tagplus.removeClass( "hidden" );
		tagminus.addClass( "hidden" );
	}
}

/* http://stackoverflow.com/questions/8579643/simple-jquery-scroll-to-anchor-up-or-down-the-page */
function anchorDoScroll(dest) {
	$('html,body').animate({scrollTop:$('a[name='+dest+']').offset().top},1000);/*,'easeInOutCirc'*/
	console.log("scroll to:"+dest+"->"+$('a[name='+dest+']').offset().top);
}
// FIXME
/*
function anchorFactory() { // put this in document ready
	$("a[href^=#]").click(function(e) {
		e.preventDefault();
		anchorDoScroll($(this).attr('href').substring(1));
	});
}
*/

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


// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
	var upper = document.getElementById("back-to-top");
	if(upper) {
	    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
	    	upper.style.display = "block";
	    } else {
	    	upper.style.display = "none";
	    }
	}
}

// When the user clicks on the button, scroll to the top of the document
function topFunction() {
    document.body.scrollTop = 0; // For Chrome, Safari and Opera 
    document.documentElement.scrollTop = 0; // For IE and Firefox
}

/*
$('.btn-group').on('input', 'change', function() {
	var radio = $(this);
	var label = radio.parent('label');
	if (radio.is(':checked')) {
		label.addClass('active');
	} else {
		label.removeClass('active');
	}
});
*/

var substringMatcher = function(strs) {
	return function findMatches(q, cb) {
		var matches, substringRegex;

		// an array that will be populated with substring matches
		matches = [];

		// regex used to determine if a string contains the substring `q`
		substrRegex = new RegExp(q, 'i');

		// iterate through the pool of strings and for any string that
		// contains the substring `q`, add it to the `matches` array
		$.each(strs, function(i, str) {
			if (substrRegex.test(str)) {
				matches.push(str);
			}
		});

		cb(matches);
	};
};

String.prototype.toHHMMSS = function() {
	var sec_num = parseInt(this, 10); // don't forget the second param
	var hours = Math.floor(sec_num / 3600);
	var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
	var seconds = sec_num - (hours * 3600) - (minutes * 60);
	if (hours < 10) {
		hours = "0" + hours;
	}
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	if (seconds < 10) {
		seconds = "0" + seconds;
	}
	var time = hours + ':' + minutes + ':' + seconds;
	return time;
}

function rangeslider(id,m,from,to) {
	$('#' + id ).rangeSlider( {
		formatter:function(val) {
			return val.toString().toHHMMSS();
		},
		scales : [
		// Primary scale
		{
			first : function(val) {
				return val;
			},
			next : function(val) {
				return val + 60;
			},
			stop : function(val) {
				return false;
			},
			label : function(val) {
				return (val / 60);
			}
		},
		// Secondary scale
		{
			first : function(val) {
				return val;
			},
			next : function(val) {
				return val + 10;
			},
			stop : function(val) {
				return false;
			},
			label : function() {
				return null;
			}
		} ]
		,
		bounds: {
			min: 0,
			max: m
		},
		defaultValues: {
			min: 0,
			max: m
		}
	} ).bind('userValuesChanged' /*valuesChanged*/, function(e, data){
		$('#'+from).val(data.values.min.toString().toHHMMSS());
		$('#'+to).val(data.values.max.toString().toHHMMSS());
	});
}

/* ======================================================= */

// https://coderwall.com/p/9loihg/video-js-disable-simultaneous-video-playback-html5-amp-flash
//html5 - prevent simultaneous video playback - pauses other playing videos upon play
document.addEventListener('DOMContentLoaded', function(event) {
	$('audio,video').bind('play', function() {
	        activated = this;
	        $('audio,video').each(function() {
	            if(this != activated) this.pause();
	        });
	});
});

//https://stackoverflow.com/questions/14256894/video-js-v-is-not-defined
//flash - prevent simultaneous video playback - pauses other playing videos upon play
/*$(".video-js").click(function(){
    activated = this;
    $('.video-js').each(function() {
        if(this != activated) _V_($(this).attr("id")).pause();
    });
});*/
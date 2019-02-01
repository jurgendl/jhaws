// https://www.codeply.com/go/SkIJQ5LqKp/custom-file-input
/*;$('.custom-file-input').on('change', function() {
	let fileName = $(this).val();
	console.log('file selected: '+fileName);
	let fileName = fileName.indexOf('/');
	if (fromfileName == -1)
		fromfileName = fileName.lastIndexOf('\\');
	fileName = fileName.substring(fromfileName + 1);
	console.log('file selected: '+filename);
	$(this).next('.form-control-file').addClass('selected').html(fileName);
});*/

;$('[data-toggle="tooltip"]').tooltip();

// ;$('.disabled').click(function(e){ e.preventDefault(); });

;$('.toast').toast('show');

; try {
	bsCustomFileInput.init();
} catch (e) {
    if (e instanceof ReferenceError) {
        console.log('reference error: requires bs-custom-file-input.js')
    }
} ;

/*
 * Bootstrap v4 Beta-2 add filename in custom input file dinamic and restore default "fake placeholder" in "Event.RESET"
 * Based on this issue: @see {https://github.com/twbs/bootstrap/issues/20813} - original as jQuery
 */
/*
let customInputs = document.querySelectorAll('input[type="file"][data-toggle="custom-file"]');
if ( customInputs.length > 0 ) {
    for (let i = 0; i < customInputs.length; i++) {
         let input = customInputs[i];
         if ( input.hasAttribute('data-target') ) {
             // only accept "id" (don't accept "class")
             let targetid = input.getAttribute('data-target');
             let target = document.getElementById(targetid);
             if ( !target || !target.getAttribute('data-content') ) {
                 // next...
                 continue;
             } else {
                 if ( !target.getAttribute('data-original-content') ) {
                     target.setAttribute('data-original-content', target.getAttribute('data-content'));
                 }
                 // handler Event (per input)
                 input.addEventListener('change', function(evt) {
                    // remark variables by "this" input refer
                    targetid = this.getAttribute('data-target');
                    target = document.getElementById(targetid);
                    // get file name
                    let filename = evt.target.files[0].name;
                    if ( !filename || filename === '' ) {
                        filename = target.getAttribute('data-original-content');
                    }
                    target.setAttribute('data-content', filename);
                    // trigger "Event.RESET" by form
                    this.form.addEventListener('reset', function() {
                       target.setAttribute('data-content', target.getAttribute('data-original-content'));
                    }, false);
                 }, false);
             }
         }
    }
}
*/
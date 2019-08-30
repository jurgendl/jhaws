/* ============= base-factory.js =============>>> */

/* Bootstrap popover */
console.log("base-factory.js ...");

$('[data-toggle="popover"]').popover({ html: true, delay: { "show": 50, "hide": 50 } });
$('.popover-dismiss').popover({ html: true, trigger: 'focus', delay: { "show": 50, "hide": 500 } });

/* Bootstrap tooltip */
/* https://stackoverflow.com/questions/35079509/bootstrap-button-tooltip-hide-on-click */
/* data-toggle="tooltip" data-trigger="hover" data-placement="left" */
$('[data-toggle="tooltip"]').tooltip({ trigger: 'hover', html: true, delay: { "show": 50, "hide": 50 } });

/* Bootstrap toast */
;$('.toast').toast('show');

/* Bootstrap CustomFileInput */
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

/* Bootstrap form validation */

//(function() {
//  'use strict';
  window.addEventListener("load", function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName("needs-validation");
    console.log(forms);
    $("form.needs-validation").each(function(index) {
    	var formNeedsValidation = $(this);
    	console.log("formNeedsValidation="+formNeedsValidation)
    	var bootstrapValidator = formNeedsValidation.data("bootstrapValidator");
        formNeedsValidation.find(".disable-validation:not([name=''])").each(function(index) {
        	var fieldNeedsNoValidation = $(this);
        	console.log("fieldNeedsNoValidation="+fieldNeedsNoValidation)
            bootstrapValidator.enableFieldValidators(fieldNeedsNoValidation.attr('name'), false);
        });
    });

    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
        console.log(event);
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
  }, false);
//})();


// activate with $('form').preventDoubleSubmission();
//jQuery plugin to prevent double submission of forms
jQuery.fn.preventDoubleSubmission = function() {
  $(this).on('submit',function(e){
    var $form = $(this);
    if ($form.data('submitted') === true) {
      // Previously submitted - don't submit again
      e.preventDefault();
    } else {
      // Mark it so that the next submit can be ignored
      $form.data('submitted', true);
    }
  });
  // Keep chainability
  return this;
};


//https://www.codeply.com/go/SkIJQ5LqKp/custom-file-input
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


// ;$('.disabled').click(function(e){ e.preventDefault(); });

console.log("... base-factory.js");
/* <<<============= base-factory.js ============= */
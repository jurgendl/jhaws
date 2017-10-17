// https://www.codeply.com/go/SkIJQ5LqKp/custom-file-input
;
$('.custom-file-input').on('change', function() {
	var fileName = $(this).val();
	var from = fileName.indexOf('/');
	if (from == -1)
		from = fileName.lastIndexOf('\\');
	fileName = fileName.substring(from + 1);
	$(this).next('.form-control-file').addClass('selected').html(fileName);
});

;
$('[data-toggle="tooltip"]').tooltip();

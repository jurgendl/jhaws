;
for (var i = tinymce.editors.length - 1 ; i > -1 ; i--) {
    var ed_id = tinymce.editors[i].id;
    tinyMCE.execCommand("mceRemoveEditor", true, ed_id);
}
;
tinymce.init({
	selector:'.tinymce'
	,menubar:'file edit insert view format table tools help'
	,toolbar:'undo redo | styleselect | bold italic underline strikethrough subscript superscript outdent indent numlist bullist link anchor | visualchars | table | forecolor backcolor | charmap | print'
	,plugins:"anchor,link,code,lists,autolink,print,searchreplace,table,visualchars,paste,advlist,charmap,textcolor,colorpicker,media,mediaembed"
	,branding:false
	,theme:'modern'
	,mediaembed_max_width:1920
	,setup:function(editor){
        editor.on('change',function(){
            editor.save();
        });
    }
});
;
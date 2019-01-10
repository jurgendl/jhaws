;
for (var i = tinymce.editors.length - 1 ; i > -1 ; i--) {
    var ed_id = tinymce.editors[i].id;
    tinyMCE.execCommand("mceRemoveEditor", true, ed_id);
}
;
tinymce.init({
	selector:'.tinymce'
	,menubar:'file edit insert view format table tools help'
	,toolbar:'undo redo | styleselect | bold italic underline strikethrough subscript superscript outdent indent numlist bullist link anchor | alignleft aligncenter alignright alignjustify | table | insertdatetime | forecolor backcolor | charmap | image | visualchars | print | removeformat'
	,plugins:"anchor,link,code,lists,advlist,autolink,print,searchreplace,table,visualchars,paste,charmap,textcolor,colorpicker,media,hr,image,imagetools,insertdatetime,wordcount,help"
	,branding:false
	,theme:'modern'
	,setup:function(editor){
        editor.on('change',function(){
            editor.save();
        });
    }
});
;
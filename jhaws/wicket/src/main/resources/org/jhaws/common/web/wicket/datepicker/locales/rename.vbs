Set objFso = CreateObject("Scripting.FileSystemObject")
Set Folder = objFSO.GetFolder(".")

For Each File In Folder.Files
    sNewFile = File.Name
    sNewFile = Replace(Replace(Replace(Replace(Replace(sNewFile,"-","_"),".","_"),"_min",""),"_js",".js"),"bootstrap_datepicker","bootstrap-datepicker")
    if (sNewFile<>File.Name) then 
        File.Move(File.ParentFolder+"\"+sNewFile)
    end if

Next
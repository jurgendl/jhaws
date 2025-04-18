#!/bin/bash

xidel -s pom.xml --xquery '
  for $d in //*[local-name()="dependency"]
  return concat(
    $d/*[local-name()="groupId"]/text(), ":",
    $d/*[local-name()="artifactId"]/text(), ":",
    $d/*[local-name()="version"]/text()
  )
' > deps.txt


xidel -s pom.xml --xquery '
  for $prop in //project//properties/* 
  return concat(local-name($prop), ":", string($prop))
' > props.txt

#NR==FNR ensures we process props.txt first.
#props[$1]=$2 stores the version variables.
#gsub(/\$\{[^}]+\}/, ...) replaces ${...} with the correct version.
#substr($3, 3, length($3)-3) extracts the version key inside ${...}.
awk -F: 'NR==FNR { props[$1]=$2; next } { gsub(/\$\{[^}]+\}/, v=props[substr($3, 3, length($3)-3)]); print $1 ":" $2 ":" v }' props.txt FS=':' deps.txt > dp.txt

rm props.txt
rm deps.txt

#https://unix.stackexchange.com/questions/86321/how-can-i-display-the-contents-of-a-text-file-on-the-command-line
#https://www.geeksforgeeks.org/sort-command-linuxunix-examples/
sort dp.txt > tmp.txt
cat tmp.txt

rm dp.txt

awk -F: '{printf "<a href=\"https://search.maven.org/search?q=g:%s%%20AND%%20a:%s\">%s:%s:%s</a><br>\n", $1, $2, $1, $2, $3}' tmp.txt > deps.html

rm tmp.txt

read -p "..." x
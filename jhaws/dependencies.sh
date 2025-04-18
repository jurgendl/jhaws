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
awk -F: 'NR==FNR { props[$1]=$2; next } { gsub(/\$\{[^}]+\}/, v=props[substr($3, 3, length($3)-3)]); print $1 ":" $2 ":" v }' props.txt FS=':' deps.txt

rm props.txt
rm deps.txt
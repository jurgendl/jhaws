#!/bin/bash

xidel -s pom.xml --xquery '
  for $d in //*[local-name()="dependency"]
  return concat(
    "<dependency><groupId>",
	$d/*[local-name()="groupId"]/text(),
	"</groupId><artifactId>",
    $d/*[local-name()="artifactId"]/text(),
	"</artifactId></dependency>"
  )
'

read -p "..." x
#!/bin/bash

printf "\033c"
line="============================================================================="
echo $line
echo "> git pull -v"
git pull -v
echo $line
echo "> git add -v -A"
git add -v -A
echo $line
while [ -z "$message" ]; do
	read -p "Commit message: " message
done
#message=${message:-"update"}
echo "> git commit -v -a -m '$message'"
git commit -v -am "$message"
echo $line
while true; do
    read -p "Push (y/n)? " yn
    case $yn in
        [Yy]* ) 
			echo "> git push -v"
			git push -v;
			break;;
        [Nn]* )
			break;;
        * )
			;;
    esac
done
echo $line
read -p "Press 'any' key to continue..."

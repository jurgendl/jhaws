#!/bin/sh

#git diff

echo "<<< git status -u"
git status -u

echo "<<< git remote update"
git remote update

LOCAL=$(git rev-parse @)
REMOTE=$(git rev-parse @{u})
BASE=$(git merge-base @ @{u})

echo "Local:  " $LOCAL;
echo "Remote: " $REMOTE;
echo "Base:   " $BASE;

if [ $LOCAL = $REMOTE ]; then
    echo ">>> Up-to-date"
elif [ $LOCAL = $BASE ]; then
    echo ">>> Need to pull"
elif [ $REMOTE = $BASE ]; then
    echo ">>> Need to push"
else
    echo ">>> Diverged"
fi

read -p "Press 'any' key to continue..."

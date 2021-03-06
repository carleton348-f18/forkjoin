#!/bin/bash

git merge --no-ff --no-commit master
for filename in "SumForkJoin.java MaxForkJoin.java SharedCounter.java";
do
    git reset HEAD $1
    git checkout -- $1
done

git commit -am "merged <merge-branch>"
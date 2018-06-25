#!/bin/bash

if [ "$1" == "" ]; then echo "input directory : $0 path" && exit; fi
for file in  $(find $1 -name "*.java" -type f -not -path "./.git/*"); do

  #echo "${file}"
  temp=$(file --mime ${file}| grep -v "charset=utf-8"|sed 's/: .*//' )
  iconv --verbose -f euc-kr -t utf-8 "${temp}" -o ${file}
done

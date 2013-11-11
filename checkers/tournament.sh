#!/bin/bash

make clean > /dev/null 
make all > /dev/null 
touch results.txt
touch output.txt
echo "# Trials is $1"
echo "Opponent is $2"
echo "Number of seconds per turn is $3"
for i in `seq 1 $1`;
do
    ./checkers $2 computer $3 >> output.txt 2>>/dev/null
    if [ $(($i%1)) == 0 ]; then
    	echo "Still Working"
    fi
done 
grep 'has lost the game' output.txt >> results.txt
let TOTAL="$(cat results.txt | wc -l)"
let TWOWINS="$(cat results.txt | grep -o 1 | wc -l)"
let ONEWINS="$(cat results.txt | grep -o 2 | wc -l)"
echo "I won $(bc -l <<< "scale=2;$TWOWINS/$TOTAL") of games"
echo "$2 won $(bc -l <<< "scale=2;$ONEWINS/$TOTAL") of games"

rm results.txt  
mv output.txt $2_$1_$3_$(date "+%s")

echo "Produced $2_$1_$3_$(date "+%s")"
echo "I lost $(grep -i time $2_$1_$3_$(date "+%s") | wc -l) due to timing out"
echo "I lost $(grep -i illegal $2_$1_$3_$(date "+%s") | wc -l) due to illegal moves"
mv $2_$1_$3_$(date "+%s") outputs/

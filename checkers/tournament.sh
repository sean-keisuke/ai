#!/bin/bash

make clean > /dev/null 2>&1
make all > /dev/null 2>&1
touch results.txt
for i in `seq 1 $1`;
do
    ./checkers computer other_players/random 1 -MaxDepth $2 | grep 'has lost the game' >> results.txt
done 
let TOTAL="$(cat results.txt | wc -l)"
let ONEWINS="$(cat results.txt | grep -o 1 | wc -l)"
let TWOWINS="$(cat results.txt | grep -o 2 | wc -l)"
echo "I won $(bc -l <<< "scale=2;$ONEWINS/$TOTAL") of games"
echo "Bot won $(bc -l <<< "scale=2;$TWOWINS/$TOTAL") of games"
rm results.txt  	

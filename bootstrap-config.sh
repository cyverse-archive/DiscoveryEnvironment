#!/bin/bash

CONFIGULON="../configulon"

if [[ ! -e $CONFIGULON ]]; then
    echo "ERROR: A clone of configulon much be available at "$CONFIGULON
    exit 1
fi

if [[ ! -n $1 ]]; then
    DE_ENV="de-1"
    echo "...No development environment indicated, using 'de-1'"
else 
    DE_ENV=$1
    echo "...development environment set to "$DE_ENV
fi

PROP_FILE="discoveryenvironment.properties"

TARGET_PATH="./src/main/resources/"

if [[ ! -e $TARGET_PATH ]]; then
    echo "ERROR: Target path "$TARGET_PATH" does not exist." 
    exit 1
fi

cp $CONFIGULON"/dev/"$DE_ENV"/"$PROP_FILE $TARGET_PATH

if [[ $? ]]; then 
    echo "...bootstrapped! (you are free to go about your local development)" 
else 
    echo "ERROR: one or both of the configuration copy operations failed (bummer!)"
fi


#!/bin/sh
set -e
DEPLOY_TARGET="PLAY"

echo "Make sure an android emulator is running for testing"

if [ -z ${STOREPATH} ]; then
echo "Input KeyStore path"
read STOREPATH
echo "Input KeyStore alias"
read KEYALIAS
fi
if [ -z ${STOREPASSWORD} ]; then
echo "Input Store password"
read -s STOREPASSWORD
echo "Input Key password"
read -s KEYPASSWORD
fi
if [ -z ${ACCOUNTEMAIL} ]; then
echo "Input AccountEmail"
read ACCOUNTEMAIL
echo "Input Jsonfile"
read JSONFILE
fi

export STOREPATH=$STOREPATH
export KEYALIAS=$KEYALIAS
export STOREPASSWORD=$STOREPASSWORD
export KEYPASSWORD=$KEYPASSWORD
export ACCOUNTEMAIL=$ACCOUNTEMAIL
export JSONFILE=$JSONFILE

echo "Using $STOREPATH for signing"
./gradlew checkstyle
./gradlew test connectedAndroidTest --stacktrace


BRANCH=$(git rev-parse --abbrev-ref HEAD | tr -d '\n')

if [ "$BRANCH" = "master" ]; then

    #Deployment stuff 
    if [ "$DEPLOY_TARGET" = "PLAY" ]; then

      ./gradlew assembleRelease
      ./gradlew publishRelease
    fi

    export VERSION=$(./gradlew properties | grep versionName | sed 's#versionName: ##g'  | tr -d '\n')
    git tag -a $VERSION -m "v$VERSION"
    git push github master --tags

    #increase version in gradle.properties
    VERSIONCODE=$(./gradlew properties | grep versionCode | sed 's#versionCode: ##g' | tr -d '\n')
    OLDVERSIONCODE=$VERSIONCODE
    VERSIONCODE=$((VERSIONCODE+1))
    OLDVERSION=$VERSION
    VERSIONFIRSTPART=${VERSION%.*}
    VERSIONLASTPART=${VERSION#*.}
    VERSION=$VERSIONFIRSTPART.`echo $VERSIONLASTPART + 1 | bc`

    sed -i "s#versionName=$OLDVERSION#versionName=$VERSION#g" gradle.properties
    sed -i "s#versionCode=$OLDVERSIONCODE#versionCode=$VERSIONCODE#g" gradle.properties

fi

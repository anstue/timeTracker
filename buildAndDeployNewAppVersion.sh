#!/bin/sh
set -e
DEPLOY_TARGET="PLAY"

echo "Make sure an android emulator is running for testing"

echo "Input KeyStore path"
read STOREPATH
echo "Input KeyStore alias"
read KEYALIAS
echo "Input Store password"
read -s STOREPASSWORD
echo "Input Key password"
read -s KEYPASSWORD
echo "Input AccountEmail"
read ACCOUNTEMAIL
echo "Input Jsonfile"
read JSONFILE

export STOREPATH=$STOREPATH
export KEYALIAS=$KEYALIAS
export STOREPASSWORD=$STOREPASSWORD
export KEYPASSWORD=$KEYPASSWORD
export ACCOUNTEMAIL=$ACCOUNTEMAIL
export JSONFILE=$JSONFILE

echo "Using $STOREPATH for signing"
./gradlew checkstyle
./gradlew test connectedAndroidTest --stacktrace
./gradlew assembleRelease

export VERSION=$(./gradlew properties | grep versionName | sed 's#versionName: "##g' | sed 's#"##g' | tr -d '\n')
git tag -a $VERSION -m "v$VERSION"
git push github master --tags

./gradlew publishRelease

#increase version in gradle.properties
VERSIONCODE=$(./gradlew properties | grep versionCode | sed 's#versionCode: ##g' | tr -d '\n')
OLDVERSIONCODE=VERSIONCODE
VERSIONCODE=$((VERSIONCODE+1))
OLDVERSION=VERSION
VERSION=`echo $VERSION + 0.1 | bc`
#TODO persist into gradle.properties
sed -i 's#versionName="$OLDVERSION"#versionName="$VERSION"#g' gradle.properties
sed -i 's#versionCode=$OLDVERSIONCODE#versionCode=$VERSIONCODE#g' gradle.properties


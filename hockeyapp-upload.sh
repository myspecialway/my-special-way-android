#!/bin/sh
echo "Starting deployment"

if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
  echo "This is a pull request. No deployment will be done to HocketApp."
  exit 0
fi

#if [[ "$TRAVIS_BRANCH" != "master" ]]; then
#  echo "Testing on a branch other than master. No deployment will be done."
#  exit 0
#fi

echo "Testing build directory"

OUTPUTDIR="$TRAVIS_BUILD_DIR/app/build/outputs/apk/debugUnsigned"
APP_NAME="my-special-way"
RELEASE_DATE=`date '+%Y-%m-%d %H:%M:%S'`
RELEASE_NOTES="Build: $TRAVIS_BUILD_NUMBER Uploaded: $RELEASE_DATE"

if [ ! -z "$HOCKEY_APP_ID" ] && [ ! -z "$HOCKEY_TOKEN" ]; then

echo ""
echo "***************************"
echo "* Uploading to Hockeyapp  *"
echo "***************************"

curl https://rink.hockeyapp.net/api/2/apps/$HOCKEY_APP_ID/app_versions \
  -F status="2" \
  -F notify="1" \
  -F notes="$RELEASE_NOTES" \
  -F notes_type="0" \
  -F ipa="@$OUTPUTDIR/$APP_NAME.apk" \
  -H "X-HockeyAppToken: $HOCKEY_TOKEN"
fi


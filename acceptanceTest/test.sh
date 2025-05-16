#!/usr/bin/env bash

set -eo pipefail

function tearDown {
  # stop-emulator
  $HOME/Library/Android/sdk/platform-tools/adb emu kill
  echo "All devices disconnected."
}

trap tearDown EXIT

echo "Build debug"
# Build
./gradlew assembleDebug

# start-emulator

echo "Starting emulator..."

$HOME/Library/Android/sdk/emulator/emulator -avd MaestroTestRunner -no-snapshot-save & $HOME/Library/Android/sdk/platform-tools/adb wait-for-device shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done; input keyevent 82'

echo "Emulator is running."

echo "Sign Build"
$HOME/Library/Android/sdk/build-tools/35.0.0/zipalign -p -f -v 4 ./app/build/outputs/apk/debug/app-debug.apk ./app/build/outputs/apk/debug/app-debug-prepared.apk
$HOME/Library/Android/sdk/build-tools/34.0.0/apksigner sign --ks ./acceptanceTest/test.keystore --ks-pass pass:abcdefg  ./app/build/outputs/apk/debug/app-debug-prepared.apk

echo "Install debug"

$HOME/Library/Android/sdk/platform-tools/adb install ./app/build/outputs/apk/debug/app-debug-prepared.apk

echo "Run Userflows"
# run test
$HOME/.maestro/bin/maestro test --output acceptanceTest/build/reports/maestro/report-offer.xml acceptanceTest/Offer.yaml

#!/usr/bin/env bash

gradle assembleDebug
adb install -r app/build/outputs/apk/app-debug-unaligned.apk
sleep 5
adb shell am start -n com.example.slmolloy.movieapp/com.example.slmolloy.movieapp.MainActivity

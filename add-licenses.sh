echo "Android Home: $ANDROID_HOME"
mkdir -p "$ANDROID_HOME/licenses"
touch "$ANDROID_HOME/licenses/android-sdk-preview-license"
touch "$ANDROID_HOME/licenses/android-sdk-license"
echo -e "\n504667f4c0de7af1a06de9f4b1727b84351f2910" >
echo -e "8933bad161af4178b1185d1a37fbf41ea5269c55" >
ls "$ANDROID_HOME/licenses"
cat "$ANDROID_HOME/licenses/android-sdk-preview-license"
cat "$ANDROID_HOME/licenses/android-sdk-license"

# Premium Fire TV Launcher (starter source)

## GitHub Actions APK build
1. Create a GitHub repository.
2. Upload the contents of this folder to the repository root.
3. Open **Actions** and run **Build Fire TV APK** (or push to `main`).
4. Open the workflow run and download the `firetv-launcher-debug-apk` artifact.

## Local preview
Open `app/src/main/assets/index.html` in a browser. For an APK, use the GitHub Actions workflow.

## Current implementation
- Native Android Activity hosting local HTML/CSS/JS in a WebView.
- Leanback launcher entry so it appears in Fire TV's app list.
- Remote-friendly button focus and basic D-pad movement.
- Attempts to launch installed apps by matching their visible app label.
- Three theme choices and a clock/date display.

## Important limitations
This is a starter implementation, not a guarantee of replacing Amazon's Home screen. Amazon may restrict Home-button interception or default launcher replacement. It does not disable Amazon's launcher. The browser UI is not a complete native Android TV launcher yet. App discovery depends on the app exposing a Leanback or launcher activity and Fire OS package-visibility behavior. The app labels in the demo are examples; only installed, discoverable apps can launch.

The UI loads a remote Unsplash background and Google Fonts when internet access is available. Replace these with local assets for a fully offline experience.

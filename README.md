# Grayscale Toggle for Android

Application for enables and disable the system-wide grayscale (monochrome) display mode.

The application does **not** apply a color filter to its own UI. Instead, it changes Android's system `Settings.Secure` values used by the system display color adjustment / daltonizer mechanism.

## How it works

Android provides a system setting called:

**Developer options → Simulate color space → Monochromacy**

For the monochromacy mode, Android uses the following secure settings:

```text
accessibility_display_daltonizer = 0
accessibility_display_daltonizer_enabled = 1
```

The application changes these values directly through `Settings.Secure`.

Because `Settings.Secure` is protected, a regular third-party application cannot normally write these settings.

Root access is **not required**.

The application therefore requires the following permission:

```
android.permission.WRITE_SECURE_SETTINGS
```

This permission is granted **once using USB ADB**.

After that, ADB is no longer required for normal operation.

```text
PC
 │ USB / ADB
 ▼
Install APK
 │
 ▼
Grant WRITE_SECURE_SETTINGS
 │
 ▼
Android application
 │
 ▼
Settings.Secure
 │
 ▼
System display color adjustment
 │
 ├── Monochromacy ON
 └── Monochromacy OFF
```

## Installation

Build and install the APK normally:

```bash
adb install app-debug.apk
```

Or install the APK from Android Studio.

## Granting `WRITE_SECURE_SETTINGS`

After installing the application, connect the phone to the computer using USB and make sure USB debugging is enabled.

Verify the device:

```bash
adb devices
```

Then grant the permission:

```bash
adb shell pm grant machine7y.grayforce android.permission.WRITE_SECURE_SETTINGS
```

If the command completes without an error, the permission has been granted.

### Verify the permission

You can inspect the application package:

```bash
adb shell dumpsys package machine7y.grayforce
```

Look for:

```text
android.permission.WRITE_SECURE_SETTINGS
```

## Important

`WRITE_SECURE_SETTINGS` is a privileged Android permission.

Declaring it in the manifest is **not enough**:

A normal application will not receive the permission automatically.

The permission must be granted using ADB:

```bash
adb shell pm grant machine7y.grayforce android.permission.WRITE_SECURE_SETTINGS
```

This is a one-time setup step for the installed application.

## Limitations

### Reinstalling the application

`WRITE_SECURE_SETTINGS` is associated with the installed package.

If the application is completely uninstalled:

```bash
adb uninstall <package-name>
```

the permission will no longer be available after a fresh installation.

In that case, grant it again:

```bash
adb shell pm grant com.example.grayscale android.permission.WRITE_SECURE_SETTINGS
```

Installing an updated APK over the existing installation normally does not require repeating the initial setup.

### ADB is only required for initial permission setup

After the permission has been granted, the application can change the settings directly:

## Security considerations

`WRITE_SECURE_SETTINGS` is a powerful permission because it allows an application to modify protected system settings.

The APK should therefore only be granted this permission if it is trusted.

The permission is intentionally **not** requested using a normal Android runtime permission dialog.

The required setup is explicitly performed by the device owner through ADB:

```bash
adb shell pm grant <package-name> android.permission.WRITE_SECURE_SETTINGS
```
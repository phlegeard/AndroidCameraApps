# Introduction

A collection of android camera applications

# MyCameraApplication0

A camera api based example, taken from https://www.dynamsoft.com/codepool/android-camera-preview-app-camera2.html
This is the most simple code sample, that displays stream from a camera 

The following camera-related use permissions are added to AndroidManifest.xml

```
    <uses-permission android:name="android.permission.CAMERA" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
```


Oo make the app displayed as full screen, add the following to values/styles.xml:
```
        <item name="android:windowFullscreen">true</item>
```	

no XML UI layout file is needed. TextureView is rendered as the camera preview. 
TextureView : https://developer.android.com/reference/android/view/TextureView.html

Register TextureView.SurfaceTextureListener to monitor the surface events : Available, SizeChanged, Destroyed
Once the surface is available, open a camera, set some parameters and start the camera preview.

# MyCameraApplication1

It's MyCameraApplication0 code sample with some addition to fix image distortion when rotating the smartphone. 
To fix the issue, AutoFitTextureView is implemented and used instead of TextureView

# MyCameraApplication2

Camera2 api based simple app

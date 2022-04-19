# PickMe
A Simple Android Media Picker Libary Made in Java with Multiple Features.
#### This Libary is under constant Updates.

Features :
 1. Make newly created videos and photos on the go.
 2. Takes the context into the Part and Returns the Uri of the Image from the Picker or the Camera
 Also, takes video from the camera.
3. Take Media Preview Including Video and Photos Before finally picking them
4. Works and Tested on Android 12 & Android 10 


Adding the Libary Dependency to your Project

1. Add ~ dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url "https://jitpack.io" }
    }
} >> In the Settings.gradle file of your project.

2. implementation 'com.github.RedEyesNCode:PickMe:PickMe'
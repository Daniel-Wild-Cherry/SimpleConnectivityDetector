[![](https://jitpack.io/v/cherrydaniel/SimpleConnectivityDetector.svg)](https://jitpack.io/#cherrydaniel/SimpleConnectivityDetector)

# SimpleConnectivityDetector

SimpleConnectivityDetector is a simple lifecycle-aware internet connectivity detector compatible with all Android versions.
This library implements a BroadcastReceiver for devices with API level lower than 24 (N), and a NetworkCallback for devices with API level 24 and above.
No version checks are necessary in your code, since the library handles these and provides notifications to a single callback which supports all device versions.

## Installation

1. Add Jitpack to your project build.gradle:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add this dependency to your app module build.gradle:

```groovy
dependencies {
    implementation 'com.github.cherrydaniel:SimpleConnectivityDetector:VERSION'
}
```

`VERSION` being [![](https://jitpack.io/v/cherrydaniel/SimpleConnectivityDetector.svg)](https://jitpack.io/#cherrydaniel/SimpleConnectivityDetector)

## Usage

There are multiple ways to use SimpleConnectivityDetector.
The most straightforward way is to use the [ConnectivityDetector](simpleconnectivitydetector/src/main/java/com/wildcherryapps/simpleconnectivitydetector/ConnectivityDetector.java) `bind` static method with a [ConnectivityListener](simpleconnectivitydetector/src/main/java/com/wildcherryapps/simpleconnectivitydetector/ConnectivityListener.java) callback.

In your Activity's onCreate method:
```java
ConnectivityDetector.bind(this, new ConnectivityListener() {
    @Override
    public void onNetworkAvailable(boolean backOnline, Network network) {
                
    }

    @Override
    public void onNetworkAvailable(boolean backOnline, NetworkInfo networkInfo) {

    }

    @Override
    public void onNetworkUnavailable() {

    }
});
```
// TODO: Finish writing README

## License

MIT License

Copyright (c) 2019 Daniel-Wild-Cherry

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

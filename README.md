# gsm_info

A new flutter plugin project.


## Package

Package fetches the current Android GSM signal strength in dBm. The package works exclusively on Android
The package requires `ACCESS_FINE_LOCATION` permission, or else it will throw a `PlatformException`

## Installation

Add to dev dependencies inside pubspec_yaml:

```yaml
    dependencies:
        gsm_info: [version]
```


## Usage

```dart
import 'package:gsm_info.dart';

...

final signalStrength = await GsmInfo.gsmSignalDbM;


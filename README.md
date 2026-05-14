# News Headlines App

A Kotlin Android app that fetches top headlines from GNews and displays them in a polished list UI.

## Features

- 2-second splash screen
- Home screen with `RecyclerView` news cards (image, title, source, date)
- Country selection from a hardcoded list:
  - Pakistan (`pk`)
  - United States (`us`)
  - United Kingdom (`gb`)
  - India (`in`)
  - Saudi Arabia (`sa`)
  - UAE (`ae`)
- Search filter on loaded headlines (title/source)
- Detail screen with full description, content preview, and source/date
- `Read Full Article` button opens original URL in browser
- Loading indicator, error message handling, and manual refresh button

## API Setup

1. Create a GNews account and copy your API key.
2. Add this line to `local.properties`:

```properties
GNEWS_API_KEY=your_actual_gnews_key
```

The app reads this value into `BuildConfig.GNEWS_API_KEY`.

## Run

```powershell
Set-Location "D:\6 Sem\MOBILE_theory\Quiz"
.\gradlew.bat assembleDebug
```

Install and run the generated APK from:
`app\build\outputs\apk\debug\app-debug.apk`


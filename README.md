[![Android CI](https://github.com/Benjiko99/Android-Sample/actions/workflows/android.yml/badge.svg)](https://github.com/Benjiko99/Android-Sample/actions/workflows/android.yml)

## Architecture

The app is written in **Kotlin** and uses the [RainbowCake](https://rainbowcake.dev/) architecture framework.

*The main goals of the architecture are:*

- Clearly separate concerns between different layers and components,
- Always keep views in a safe and consistent state with ViewModels,
- Handle configuration changes (and even process death) gracefully,
- Make offloading work to background threads trivial.

*Build with:*

- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html),
[Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html),
[Retrofit](https://square.github.io/retrofit/),
[Koin](https://insert-koin.io/),
[Room](https://developer.android.com/topic/libraries/architecture/room/),
[Jetpack Navigation](https://developer.android.com/guide/navigation/),
[LeakCanary](https://square.github.io/leakcanary/),
[Stetho](https://facebook.github.io/stetho/),
[FastAdapter](https://github.com/mikepenz/FastAdapter/)

<img src="/assets/architecture.png" alt="Architecture diagram" width="508" height="562" />

## Back-end

The app is communicating with a REST API built with Ktor.
The backend is available on [GitHub](https://github.com/Benjiko99/Backend-Sample).

## Screenshots

Transactions | Detail | Profile
--- | --- | ---
![](/assets/transactions.png) | ![](/assets/detail%20full.png) | ![](/assets/profile.png)

***View the UI designs on [Figma](https://www.figma.com/file/TzouiZrbhbZw1Q8qnqntQ9/Banking-Sample).***


## License

    This is free and unencumbered software released into the public domain.

    For more information, please refer to either <http://unlicense.org> or the **LICENSE** file.

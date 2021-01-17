[![Build Status](https://app.bitrise.io/app/90d86914184610cd/status.svg?token=oQPBvmy8X1D2qFxYUjHi5w&branch=master)](https://app.bitrise.io/app/90d86914184610cd)

Architecture
-----------------
The app is written in **Kotlin** and uses the [RainbowCake](https://rainbowcake.dev/) architecture framework.

The main goals of the architecture are:

- Clearly separate concerns between different layers and components,
- Always keep views in a safe and consistent state with ViewModels,
- Handle configuration changes (and even process death) gracefully,
- Make offloading work to background threads trivial.

<img src="/assets/architecture.png" alt="Architecture diagram" width="508" height="562" />

Libraries
---------
Local persistence is done with **Room**.

Navigation is handled by the **Navigation Architecture Component**.

Networking is done through **OkHttp**, **Retrofit** and **Coroutines**.

Dependency Injection is done through **Koin**.

Creating adapters for RecyclerViews is simplified with **FastAdapter**.

Debugging is helped by **LeakCanary** and **Stetho**.

Back-end
--------
The app is communicating with a REST API built with Ktor.
The backend is available on [GitHub](https://github.com/Benjiko99/Backend-Sample).

License
-------

    This is free and unencumbered software released into the public domain.

    For more information, please refer to either <http://unlicense.org> or the **LICENSE** file.

Architecture
-----------------
The app is written entirely in **Kotlin** and uses the [RainbowCake](https://rainbowcake.dev/) architecture framework.

The main goals of the architecture are:

- Clearly separate concerns between different layers and components,
- Always keep views in a safe and consistent state with ViewModels,
- Handle configuration changes (and even process death) gracefully,
- Make offloading work to background threads trivial.

<img src="/assets/architecture.png" alt="Architecture diagram" width="508" height="562" />

Libraries
---------
Local caching is either done in-memory or using **Room**.

Navigation is handled by the **Navigation Architecture Component**.

Networking is done through **OkHttp**, **Retrofit** and **Coroutines**.

Dependency Injection is done through **Koin**.

Creating adapters for RecyclerViews is simplified with **FastAdapter**.

Debugging is facilitated by **LeakCanary** and **Stetho**.

Back-end
--------
The app is communicating with a REST API at https://benjiko99-android-sample.builtwithdark.com/ built with the serverless Dark language/platform.

License
-------

    This is free and unencumbered software released into the public domain.

    For more information, please refer to either <http://unlicense.org> or the **LICENSE** file.

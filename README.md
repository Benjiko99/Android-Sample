Project Structure
-----------------
The app has two modules: **core** and **app**.

The files are organized in a package-by-feature manner, where all files related to a given feature are located in a package named after that feature, e.g. "transaction".  
There may also be sub-packages to split large features into smaller ones, e.g.: "list" and "detail".  
The feature is then broken down into its components, like: "ui" and "domain".


Libraries
---------
The app is written entirely in **Kotlin** and uses **AndroidX** components.

Navigation is handled by the **Navigation Architecture Component**.

Networking is done through **OkHttp**, **Retrofit** and **LiveData**.

Dependency Injection is done through **Dagger 2**.

Debugging is facilitated by **LeakCanary** and **Stetho**.

License
-------

    This is free and unencumbered software released into the public domain.

    For more information, please refer to either <http://unlicense.org> or the **LICENSE** file.

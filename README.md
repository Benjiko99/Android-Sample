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

    Copyright (c) 2018 Tomáš Beňo
    
    TERMS AND CONDITIONS
    
    A. Permissions:
    
      1. You may view, review and study the Work.
      2. You may submit Contributions to the Work.
      
    B. Limitations:
    
      1. You may not use the Work except in compliance with the License.
      2. You may not use the Work for commercial purposes.
      3. You may not distribute the Work.
      4. Any copies of the Work must contain this exact license.
      5. You may only copy ("fork") the Work for the purposes of submitting Contributions to the Work.
      6. For any Contribution submitted for inclusion in the Work
         to the Licensor, the Contributor grants the Licensor a perpetual,
         worldwide, no-charge, royalty-free, irrevocable
         copyright license to reproduce, prepare Derivative Works of,
         publicly display, publicly perform, sublicense, and distribute the
         Contribution in Source or Object form.
         
See the full license in the **LICENSE.md** file.

import org.gradle.api.JavaVersion

object AppDeps {

    const val id = "com.spiraclesoftware.androidsample"
    const val versionCode = 1
    const val versionName = "1.0"

    const val compileSdk = 29
    const val targetSdk = 29
    const val minSdk = 21

    val sourceCompat = JavaVersion.VERSION_1_8
    val targetCompat = JavaVersion.VERSION_1_8
}
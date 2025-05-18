package firebase

object FirebaseConfig {
    val config = jsObject {
        apiKey = "AIzaSyDASIcrW3PE2Cx3ucJ4F41Ufi11-JJKQfI"
        authDomain = "invesproject-be46a.firebaseapp.com"
        projectId = "invesproject-be46a"
        storageBucket = "invesproject-be46a.firebasestorage.app"
        messagingSenderId = "871785524032"
        appId = "1:871785524032:web:3d39f309ecb743e83268a5"
        measurementId = "G-TGM7Z7K2RJ"
    }
}

@JsModule("firebase/app")
@JsNonModule
external object FirebaseApp {
    fun initializeApp(config: dynamic): dynamic
}

@JsModule("firebase/analytics")
@JsNonModule
external object FirebaseAnalytics {
    fun getAnalytics(app: dynamic): dynamic
}

fun initializeFirebase() {
    val app = FirebaseApp.initializeApp(FirebaseConfig.config)
    FirebaseAnalytics.getAnalytics(app)
} 
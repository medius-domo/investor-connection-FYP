import com.invesproject.web.ui.App
import firebase.initializeFirebase
import org.jetbrains.compose.web.renderComposable

fun main() {
    // Initialize Firebase
    initializeFirebase()
    
    renderComposable(rootElementId = "root") {
        App()
    }
} 
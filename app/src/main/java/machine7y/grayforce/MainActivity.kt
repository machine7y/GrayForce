package machine7y.grayforce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import machine7y.grayforce.manager.MainManagerImpl

class MainActivity : ComponentActivity() {

    private val mainManager by lazy {
        MainManagerImpl(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(mainManager)
        }
    }
}
package org.mattshoe.shoebox.listery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.navigation.ListeryNavGraph
import org.mattshoe.shoebox.listery.ui.theme.ListeryTheme
import javax.inject.Inject

@AndroidEntryPoint
class ListeryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListeryTheme {
                ListeryNavGraph()
            }
        }
    }
}

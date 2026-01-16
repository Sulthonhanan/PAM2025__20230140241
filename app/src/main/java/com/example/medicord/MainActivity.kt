package com.example.medicord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.medicord.Data.database.AppDatabase
import com.example.medicord.Data.database.entity.UserEntity
import com.example.medicord.Data.repository.UserRepository
import com.example.medicord.navigation.NavGraph
import com.example.medicord.ui.theme.MedicordTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize default user if needed
        initializeDefaultUser()
        
        setContent {
            MedicordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    private fun initializeDefaultUser() {
        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())
        
        // Check if user exists, if not create default admin user
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val userCount = userRepository.getUserCount()
                if (userCount == 0) {
                    // Create default admin user
                    userRepository.insertUser(
                        UserEntity(
                            username = "admin",
                            password = "admin123",
                            role = "Admin"
                        )
                    )
                }
            }
        }
    }
}
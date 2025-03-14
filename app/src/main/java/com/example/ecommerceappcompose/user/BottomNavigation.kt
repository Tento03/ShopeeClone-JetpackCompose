package com.example.ecommerceappcompose.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ecommerceappcompose.model.Bottom

@Composable
fun BottomNavigation(navController: NavController){
    val bottomItem= listOf(
        Bottom("Home", Icons.Default.Home,"Home"),
        Bottom("Trending",Icons.Default.Star,"Trend"),
        Bottom("Account",Icons.Default.Person,"Account")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute=navBackStackEntry?.destination?.route

    NavigationBar {
        bottomItem.forEachIndexed { index, bottom ->
            NavigationBarItem(
                selected = currentRoute==bottom.route,
                onClick = {
                    navController.navigate(bottom.route){
                        launchSingleTop=true
                        restoreState=true

                        popUpTo(navController.graph.startDestinationId){
                            saveState=true
                        }
                    }
                },
                icon = { Icon(bottom.icon,null) },
                label = { Text(bottom.label) },
            )
        }
    }
}
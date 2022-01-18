package com.example.profilesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailScreen()
        }
    }
}

@Composable
fun DetailScreen(profiles: List<Profile> = list) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "users_list") {
        composable("users_list") {
            ProfileScreen(profiles, nav)
        }
        composable("user_details/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
        })) {
            ProfileDetailScreen(it.arguments!!.getInt("userId")) {
                nav.navigateUp()
            }
        }
    }
}
@Composable
fun ProfileScreen(profiles: List<Profile>, nav: NavHostController) {
   Scaffold(
       topBar = { TopAppBar(
       title = { Text("Profile") },
       navigationIcon = { Icon(Icons.Default.Home,
           contentDescription = "",
           Modifier.padding(12.dp)) })}
   ) {
       Surface(modifier = Modifier.fillMaxSize(),
           color = Color.LightGray) {
           LazyColumn {
               items(profiles) { user ->
                   ProfileCard(profile = user) {
                       nav.navigate("user_details/${user.id}")
                   }
               }
           }
       }
   }
}

@Composable
fun ProfileCard(profile: Profile, click: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { click.invoke() }) ,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(profile.url, profile.status)
            ContentProfile(profile.name, profile.status)
        }
    }
}

@Composable
fun ProfilePicture(url: String, status: Boolean) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (status) Color.Green else Color.Red
        ),
        modifier = Modifier.padding(16.dp),
        elevation = 8.dp
    ) {
        // Using Coil
        Image(
            painter = rememberCoilPainter(request = url,
                requestBuilder = {
                    transformations(CircleCropTransformation())
                }),
            modifier = Modifier.size(70.dp),
            contentDescription = ""
        )
    }
}

@Composable
fun ProfileDetailScreen(id: Int, click: () -> Unit) {
    val profiles = list.first { profile -> id == profile.id }
    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Profile Details") },
            navigationIcon = { Icon(Icons.Default.ArrowBack,
                contentDescription = "",
                Modifier.padding(12.dp).clickable(onClick = {click.invoke() }))
            })
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = Color.LightGray) {
                //ProfileCard(profile = profiles)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(profiles.url, profiles.status)
                ContentProfile(profiles.name, profiles.status)
            }
        }
    }
}

@Composable
fun ContentProfile(userName: String, status: Boolean) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalContentAlpha provides if (status) 1f else ContentAlpha.medium) {
            Text(
                text = userName,
                style = MaterialTheme.typography.h5
            )
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = if (status) "Online" else "Offline",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DetailScreen()
}
package com.choiril36.minggu10

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.choiril36.minggu10.ui.theme.Minggu10Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Minggu10Theme {
                var posts by remember { mutableStateOf<List<Post>>(emptyList()) }

                LaunchedEffect(Unit) {
                    loadPosts { result ->
                        posts = result
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PostList(
                        posts = posts,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun loadPosts(onResult: (List<Post>) -> Unit) {
        ApiClient.instance.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts = response.body() ?: emptyList()
                    onResult(posts)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e("API_ERROR", t.message ?: "Unknown error")
                onResult(emptyList())
            }
        })
    }
}

@Composable
fun PostList(posts: List<Post>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(posts) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

package com.example.compose.previewscreenshot

import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.compose.previewscreenshot.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingPage(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.greeting),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Welcome to our Compose Screenshot Testing demo!",
                fontSize = 20.sp
            )

            Button(
                onClick = { /* Do something */ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Click Me")
            }

            Text(
                text = "Here is a list of features:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "• Compose UI support")
                Text(text = "• Locale-specific screenshots")
                Text(text = "• Light and Dark mode testing")
                Text(text = "• Screenshot validation workflow")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Below is an HTML WebView content:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Injected WebView displaying HTML content
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        loadData(
                            """
                            <html>
                                <body>
                                    <h1 style="color:red;">Hello from WebView!</h1>
                                    <p>This HTML content is rendered inside the Compose UI and will appear in screenshots.</p>
                                </body>
                            </html>
                            """.trimIndent(),
                            "text/html",
                            "utf-8"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "es")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    SampleTheme {
        GreetingPage()
    }
}

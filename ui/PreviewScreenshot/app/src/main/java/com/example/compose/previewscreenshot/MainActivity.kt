package com.example.compose.previewscreenshot

import android.content.res.Configuration
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                    HtmlPage(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun HtmlPage(modifier: Modifier = Modifier) {
    val htmlContent = stringResource(id = R.string.greeting_html)

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "es")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HtmlPagePreview() {
    SampleTheme {
        HtmlPage()
    }
}

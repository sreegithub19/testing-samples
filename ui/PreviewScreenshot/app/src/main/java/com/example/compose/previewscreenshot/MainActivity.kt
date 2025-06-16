package com.example.compose.previewscreenshot

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.previewscreenshot.ui.theme.SampleTheme
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingPage(modifier = Modifier.padding(innerPadding))
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
                text = "¡Hola!",
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
                text = "Below is static HTML-rendered content:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            HtmlText(
                html = """
                    <h1 style="color:red;">Hello from WebView!</h1>
                    <p>This HTML content is now rendered natively in Compose and will appear in CI screenshots.</p>
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Below is GraalVM-generated HTML content:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            val context = LocalContext.current
            val generatedHtml = remember { loadGeneratedHtml(context) }

            HtmlText(generatedHtml)
        }
    }
}

@Composable
fun HtmlText(html: String) {
    val annotatedString = remember(html) {
        parseHtmlToAnnotatedString(html)
    }
    Text(
        text = annotatedString,
        fontSize = 16.sp,
        textAlign = TextAlign.Start
    )
}

fun parseHtmlToAnnotatedString(html: String): AnnotatedString {
    val document = Jsoup.parse(html)
    val body = document.body()
    return buildAnnotatedString {
        parseElement(body)
    }
}

private fun AnnotatedString.Builder.parseElement(element: Element) {
    for (node in element.childNodes()) {
        when (node) {
            is TextNode -> {
                append(node.text())
            }
            is Element -> {
                val inlineColor = node.attr("style")
                    .substringAfter("color:", "")
                    .substringBefore(";")
                    .trim()

                val spanStyle = if (inlineColor.isNotEmpty()) {
                    SpanStyle(color = parseColor(inlineColor))
                } else {
                    SpanStyle()
                }

                when (node.tagName().lowercase()) {
                    "h1" -> withStyle(spanStyle.merge(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))) {
                        parseElement(node)
                    }
                    "p" -> withStyle(spanStyle) {
                        parseElement(node)
                        append("\n\n")
                    }
                    else -> withStyle(spanStyle) {
                        parseElement(node)
                    }
                }
            }
        }
    }
}

private fun parseColor(colorString: String): Color {
    return when (colorString.lowercase()) {
        "red" -> Color.Red
        "blue" -> Color.Blue
        "green" -> Color.Green
        "black" -> Color.Black
        else -> Color.Black // fallback default
    }
}

fun loadGeneratedHtml(context: Context): String {
    return try {
        context.assets.open("generated.html").bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        "<p style=\"color:red;\">Error loading generated HTML: ${e.message}</p>"
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

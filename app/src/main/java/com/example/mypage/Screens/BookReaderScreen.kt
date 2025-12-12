package com.example.mypage.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController
import com.example.mypage.models.Book
import com.example.mypage.models.getAuthorName
import com.example.mypage.ui.theme.SpaceGrotesk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import java.io.ByteArrayInputStream
import java.io.BufferedInputStream
import java.util.zip.ZipInputStream
import java.util.concurrent.TimeUnit

data class BookSection(
    val type: String, // "title", "heading", "chapter", "paragraph", "quote"
    val content: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookReaderScreen(
    navController: NavController,
    book: Book
) {
    var fontSize by remember { mutableStateOf(16.sp) }
    var lineHeight by remember { mutableStateOf(1.8f) }
    val scrollState = rememberScrollState()
    var bookSections by remember { mutableStateOf<List<BookSection>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(book.id) {
        try {
            val content = fetchAndParseEpub(book)
            bookSections = parseBookContent(content)
            isLoading = false
            hasError = false
        } catch (e: Exception) {
            isLoading = false
            hasError = true
            errorMessage = e.localizedMessage ?: "Unknown error occurred"
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        color = Color.Black,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            if (!hasError && !isLoading) {
                // Reading controls
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    color = Color(0xFFF5F5F5),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Font Size Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("A", fontSize = 12.sp, color = Color.Gray, fontFamily = SpaceGrotesk)
                            Slider(
                                value = fontSize.value,
                                onValueChange = { fontSize = it.sp },
                                valueRange = 14f..28f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF0D9488),
                                    activeTrackColor = Color(0xFF0D9488)
                                )
                            )
                            Text("A", fontSize = 22.sp, color = Color.Gray, fontFamily = SpaceGrotesk)
                        }

                        // Line Height Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Spacing", fontSize = 11.sp, color = Color.Gray, fontFamily = SpaceGrotesk)
                            Slider(
                                value = lineHeight,
                                onValueChange = { lineHeight = it },
                                valueRange = 1.4f..2.4f,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF0D9488),
                                    activeTrackColor = Color(0xFF0D9488)
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF0D9488),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Loading EPUB...",
                            fontSize = 14.sp,
                            fontFamily = SpaceGrotesk,
                            color = Color.Gray
                        )
                    }
                }
            } else if (hasError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        Text("⚠️", fontSize = 48.sp, modifier = Modifier.padding(bottom = 16.dp))
                        Text(
                            "Failed to Load Book",
                            fontSize = 18.sp,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            errorMessage,
                            fontSize = 13.sp,
                            fontFamily = SpaceGrotesk,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Formatted book content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    bookSections.forEachIndexed { index, section ->
                        BookContentSection(
                            section = section,
                            fontSize = fontSize,
                            lineHeight = lineHeight
                        )

                        // Add spacing between sections
                        if (index < bookSections.size - 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
fun BookContentSection(
    section: BookSection,
    fontSize: TextUnit,
    lineHeight: Float
) {
    when (section.type) {
        "title" -> {
            Text(
                text = section.content,
                fontSize = (fontSize.value + 8).sp,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGrotesk,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                lineHeight = (fontSize.value * lineHeight).sp
            )
        }

        "heading", "chapter" -> {
            Text(
                text = section.content,
                fontSize = (fontSize.value + 4).sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = SpaceGrotesk,
                color = Color(0xFF0D9488),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 12.dp),
                lineHeight = (fontSize.value * lineHeight).sp
            )
            Divider(
                color = Color(0xFFE5E5E5),
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        "quote" -> {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFFF0F9F8),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "\" ${section.content} \"",
                    fontSize = fontSize,
                    fontFamily = SpaceGrotesk,
                    color = Color(0xFF0D9488),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = (fontSize.value * lineHeight).sp,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }

        "paragraph" -> {
            if (section.content.trim().isNotEmpty()) {
                Text(
                    text = section.content,
                    fontSize = fontSize,
                    fontFamily = SpaceGrotesk,
                    color = Color(0xFF333333),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 12.dp),
                    textAlign = TextAlign.Justify,
                    lineHeight = (fontSize.value * lineHeight).sp
                )
            }
        }
    }
}

/**
 * Fetches EPUB file and extracts text content
 * EPUB is a ZIP file containing HTML chapters
 */
suspend fun fetchAndParseEpub(book: Book): String {
    return withContext(Dispatchers.IO) {
        try {
            // Get EPUB URL from formats
            val epubUrl = book.formats["application/epub+zip"]
                ?: book.formats["application/x-epub"]
                ?: book.formats.entries.find { it.value.contains(".epub", ignoreCase = true) }?.value
                ?: return@withContext "EPUB file not found"

            // Download EPUB with proper handling
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(epubUrl)
                .addHeader("User-Agent", "MyPage-BookReader/1.0")
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext "Failed to fetch EPUB (HTTP ${response.code})"
            }

            // Read the complete response body
            val epubData = response.body?.bytes()
                ?: return@withContext "Failed to read EPUB"

            // Parse EPUB (ZIP file) from byte array
            val textContent = StringBuilder()

            try {
                val zipInputStream = ZipInputStream(ByteArrayInputStream(epubData))

                var entry = zipInputStream.nextEntry
                while (entry != null) {
                    try {
                        // Look for XHTML/HTML files in text directories
                        if (entry.name.contains("text/", ignoreCase = true) ||
                            entry.name.endsWith(".html", ignoreCase = true) ||
                            entry.name.endsWith(".xhtml", ignoreCase = true)) {

                            val contentBytes = ByteArray(1024)
                            val byteContent = StringBuilder()
                            var bytesRead: Int

                            while (zipInputStream.read(contentBytes).also { bytesRead = it } != -1) {
                                byteContent.append(String(contentBytes, 0, bytesRead, Charsets.UTF_8))
                            }

                            val htmlContent = byteContent.toString()

                            // Parse HTML content using Jsoup
                            if (htmlContent.isNotEmpty()) {
                                val doc = Jsoup.parse(htmlContent)
                                val text = doc.body().text()

                                if (text.isNotEmpty()) {
                                    textContent.append(text).append("\n\n")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        // Skip problematic entries
                    }

                    entry = zipInputStream.nextEntry
                }

                zipInputStream.close()
            } catch (e: Exception) {
                return@withContext "Error reading ZIP: ${e.message}"
            }

            if (textContent.isEmpty()) {
                return@withContext "No text content found in EPUB"
            }

            cleanBookContent(textContent.toString())
        } catch (e: Exception) {
            "Error parsing EPUB: ${e.message}"
        }
    }
}

fun cleanBookContent(content: String): String {
    var result = content

    // Decode HTML entities using Apache Commons Text
    result = StringEscapeUtils.unescapeHtml4(result)

    // Remove Gutenberg header/footer markers (if present)
    val startMarkers = listOf(
        "***START OF THE PROJECT GUTENBERG EBOOK",
        "***START OF PROJECT GUTENBERG EBOOK",
        "***START OF THIS PROJECT GUTENBERG EBOOK"
    )

    val endMarkers = listOf(
        "***END OF THE PROJECT GUTENBERG EBOOK",
        "***END OF PROJECT GUTENBERG EBOOK",
        "***END OF THIS PROJECT GUTENBERG EBOOK"
    )

    for (marker in startMarkers) {
        val startIndex = result.indexOf(marker)
        if (startIndex != -1) {
            val newlineIndex = result.indexOf("\n", startIndex)
            if (newlineIndex != -1) {
                result = result.substring(newlineIndex + 1)
            }
            break
        }
    }

    for (marker in endMarkers) {
        val endIndex = result.indexOf(marker)
        if (endIndex != -1) {
            result = result.substring(0, endIndex)
            break
        }
    }

    // Clean up excessive whitespace
    result = result.replace(Regex("\\s+"), " ")

    return result.trim()
}

fun parseBookContent(rawContent: String): List<BookSection> {
    val sections = mutableListOf<BookSection>()
    val lines = rawContent.split("\n")

    var currentParagraph = StringBuilder()
    var emptyLineCount = 0

    for (line in lines) {
        val trimmedLine = line.trim()

        when {
            // Empty line - potential paragraph separator
            trimmedLine.isEmpty() -> {
                emptyLineCount++
                if (emptyLineCount == 1 && currentParagraph.isNotEmpty()) {
                    currentParagraph.append("\n")
                }
            }

            // Chapter detection (common patterns)
            trimmedLine.matches(Regex("(?i)(chapter|section|part)\\s+\\d+.*")) -> {
                if (currentParagraph.isNotEmpty()) {
                    sections.add(BookSection("paragraph", currentParagraph.toString().trim()))
                    currentParagraph.clear()
                }
                sections.add(BookSection("chapter", trimmedLine))
                emptyLineCount = 0
            }

            // Heading detection (multiple spaces or all caps)
            trimmedLine.length > 5 && trimmedLine.all { it.isUpperCase() || it.isWhitespace() } -> {
                if (currentParagraph.isNotEmpty()) {
                    sections.add(BookSection("paragraph", currentParagraph.toString().trim()))
                    currentParagraph.clear()
                }
                sections.add(BookSection("heading", trimmedLine))
                emptyLineCount = 0
            }

            // Quote detection (starts and ends with quotes or dashes)
            (trimmedLine.startsWith("\"") || trimmedLine.startsWith("—")) &&
                    (trimmedLine.endsWith("\"") || trimmedLine.endsWith("—")) -> {
                if (currentParagraph.isNotEmpty()) {
                    sections.add(BookSection("paragraph", currentParagraph.toString().trim()))
                    currentParagraph.clear()
                }
                sections.add(BookSection("quote", trimmedLine.trim('\"', '—')))
                emptyLineCount = 0
            }

            // Regular text
            else -> {
                emptyLineCount = 0
                if (currentParagraph.isNotEmpty() && !currentParagraph.endsWith("\n")) {
                    currentParagraph.append(" ")
                }
                currentParagraph.append(trimmedLine)
            }
        }
    }

    // Add remaining paragraph
    if (currentParagraph.isNotEmpty()) {
        sections.add(BookSection("paragraph", currentParagraph.toString().trim()))
    }

    return sections.filter { it.content.isNotEmpty() }
}
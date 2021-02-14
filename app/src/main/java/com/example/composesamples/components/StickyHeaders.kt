package com.example.composesamples.components

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composesamples.R
import com.example.composesamples.styles.NewsTypography
import com.example.composesamples.styles.cardBackground
import com.squareup.moshi.*
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.systemBarsPadding
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


private const val TAG = "PaginatedNews"

@ExperimentalFoundationApi
@Composable
fun StickyHeaders() {
    val moshi = Moshi.Builder()
        .add(DateAdapter())
        .build()

    val newsService = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(NewsService::class.java)

    val news = mutableStateListOf<News>()

    LaunchedEffect(true) {
        try {
            val list = newsService.listNews()

            //Sometimes duplicate news entries are loaded
            news.addAll(
                list
                    .articles
                    .toSet()
            )
            Log.d(TAG, "PaginatedNews: Loaded ${list.articles.size} items")
        } catch (t: Throwable) {
            Log.e(TAG, "PaginatedNews: ", t)
        }
    }

    val loadingTransition = rememberInfiniteTransition()

    ProvideWindowInsets {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .systemBarsPadding()
                .fillMaxSize(),
        ) {
            if (news.isNotEmpty()) {
                NewsList(news = news)
            } else {
                val loadingAlpha by loadingTransition.animateFloat(
                    initialValue = 0.2f, targetValue = 1f, animationSpec =
                    infiniteRepeatable(
                        repeatMode = RepeatMode.Reverse,
                        animation = tween(1000)
                    )
                )

                Text(
                    text = "Loading...",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(loadingAlpha)
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NewsList(news: List<News>) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMMM dd, yyyy") }

    val aggregatedNews = news
        .sortedByDescending {
            it.publishedAt
        }
        .groupBy {
            it.publishedAt
        }
    LazyColumn(
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        aggregatedNews.forEach { (publishedDate, newsEntries) ->

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    NewsSeparator(
                        date = if (publishedDate == LocalDate.now()) "Today" else dateFormatter.format(
                            publishedDate
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            items(newsEntries) {
                NewsCard(newsItem = it)
            }
        }

    }

}

@Composable
fun NewsCard(newsItem: News) {
    Card(
        backgroundColor = MaterialTheme.colors.cardBackground,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .preferredHeight(160.dp)
            .fillMaxWidth()
    ) {
        Row {
            val placeHolder = R.drawable.bg_passcode

            val data = newsItem.urlToImage ?: placeHolder

            CoilImage(
                loading = {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.bg_passcode),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds
                        )
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight(),
                contentDescription = null,
                data = data,
            )

            Column {
                Text(
                    style = NewsTypography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    text = newsItem.title, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    }
}


@Composable
fun NewsSeparator(date: String, modifier: Modifier = Modifier) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
            .background(
                shape = CircleShape,
                alpha = 0.4f,
                brush = SolidColor(MaterialTheme.colors.onSurface)
            )
            .padding(horizontal = if (date == "Today") 40.dp else 15.dp, vertical = 5.dp)
    ) {
        Text(
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.surface
            ),
            text = date
        )
    }
}


@JsonClass(generateAdapter = true)
data class NewsResponse(
    @Json(name = "status")
    val status: String,

    @Json(name = "totalResults")
    val totalResults: Int,

    @Json(name = "articles")
    val articles: List<News>
)

@JsonClass(generateAdapter = true)
data class News(
    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "url")
    val url: String,

    @Json(name = "urlToImage")
    val urlToImage: String?,

    @Json(name = "publishedAt")
    val publishedAt: LocalDate
)

@Suppress("unused")
class DateAdapter {
    @ToJson
    fun toJson(date: LocalDate): String = date.toString()

    @FromJson
    fun fromJson(dateJson: String): LocalDate = ZonedDateTime.parse(dateJson).toLocalDate()
}

interface NewsService {
    @GET("v2/everything")
    suspend fun listNews(
        @Query("q") q: String = "Man United",
        @Query("apiKey") apiKey: String = "bddfa3d483ae4a28ab9177a45fc8f026",
        @Query("pagesize") pageSize: Int = 50,
    ): NewsResponse
}


@Preview
@Composable
fun NewsTilePreview() {
    val news = News(
        title = "Man United thrash Soton",
        description = "Manchester United thrash Soton",
        url = "www.url.to.news",
        urlToImage = "url.to.image",
        publishedAt = LocalDate.now()
    )
    NewsCard(newsItem = news)
}
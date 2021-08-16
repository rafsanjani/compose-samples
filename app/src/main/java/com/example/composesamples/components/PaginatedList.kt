package com.example.composesamples.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.composesamples.R
import com.example.composesamples.styles.NewsTypography
import com.example.composesamples.styles.cardBackground
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.ZonedDateTime


private const val TAG = "PaginatedNews"

@ExperimentalFoundationApi
@Composable
fun PaginatedList() {
    val items = getPhotoPagination().collectAsLazyPagingItems()

    ProvideWindowInsets {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .systemBarsPadding()
                .fillMaxSize(),
        ) {
            NewsList(news = items)
        }
    }
}

private fun getPhotoPagination(): Flow<PagingData<News>> {
    val moshi = Moshi.Builder()
        .add(DateAdapter())
        .build()

    val newsService = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(NewsService::class.java)

    return Pager(PagingConfig(pageSize = 10)) {
        NewsDataSource(newsService)
    }.flow
}

@ExperimentalFoundationApi
@Composable
private fun NewsList(news: LazyPagingItems<News>) {
    LazyColumn(
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        state = rememberLazyListState()
    ) {

        itemsIndexed(news) { _, item ->
            NewsCard(newsItem = item!!)
        }

        news.apply {
            when {
                loadState.refresh is LoadState.Loading -> item {
                    Text(text = "Loading")
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}

@Composable
private fun NewsCard(newsItem: News) {
    Card(
        backgroundColor = MaterialTheme.colors.cardBackground,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .requiredHeight(160.dp)
            .fillMaxWidth()
    ) {
        Row {
            val placeHolder = R.drawable.bg_passcode

            val data = newsItem.urlToImage ?: placeHolder

            val imagePainter = rememberCoilPainter(request = data)

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = newsItem.title,
                    contentScale = ContentScale.Crop,
                )

                when (imagePainter.loadState) {
                    is ImageLoadState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                    else -> {
                    }
                }
            }

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
private class DateAdapter {
    @ToJson
    fun toJson(date: LocalDate): String = date.toString()

    @FromJson
    fun fromJson(dateJson: String): LocalDate = ZonedDateTime.parse(dateJson).toLocalDate()
}

private interface NewsService {
    @GET("v2/everything")
    suspend fun listNews(
        @Query("q") q: String = "Ghana",
        @Query("apiKey") apiKey: String = "bddfa3d483ae4a28ab9177a45fc8f026",
        @Query("pagesize") pageSize: Int = 10,
        @Query("page") page: Int = 1,
    ): NewsResponse
}

private class NewsDataSource(private val service: NewsService) : PagingSource<Int, News>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val page = params.key ?: 1
            val response = service.listNews(
                page = page
            )

            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(
                exception
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        TODO("Not yet implemented")
    }
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
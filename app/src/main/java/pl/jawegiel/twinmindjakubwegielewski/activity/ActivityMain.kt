package pl.jawegiel.twinmindjakubwegielewski.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.R
import pl.jawegiel.twinmindjakubwegielewski.composable.ModalBottomSheet
import pl.jawegiel.twinmindjakubwegielewski.composable.ScreenAllMeetingsQuestions
import pl.jawegiel.twinmindjakubwegielewski.composable.ScreenCalendar
import pl.jawegiel.twinmindjakubwegielewski.composable.ScreenMemories
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.Amber
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelCalendar
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelMain
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelMemories
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelQuestionsAllMeetings

class ActivityMain() : ComponentActivity() {

    private val tabTitles = listOf("Memories", "Calendar", "Questions")
    private val vmMain by lazy { ViewModelProvider(this)[ViewModelMain::class.java] }
    private val vmMemories by lazy { ViewModelProvider(this)[ViewModelMemories::class.java] }
    private val vmCalendar by lazy { ViewModelProvider(this)[ViewModelCalendar::class.java] }
    private val vmMultipleMeetings by lazy { ViewModelProvider(this)[ViewModelQuestionsAllMeetings::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ActivityMainScreenLayout()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Intent().apply {
                    setAction(Intent.ACTION_MAIN)
                    addCategory(Intent.CATEGORY_HOME)
                }.let { startActivity(it) }
            }
        })

        vmMain.getUserPhoto()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        lifecycleScope.launch {
            vmMultipleMeetings.updateQuestions(true)
            vmMemories.updateMemories(true)
        }
    }

    @Composable
    fun ActivityMainScreenLayout() {
        var isToShowBottomSheet by remember { mutableStateOf(false) }
        Scaffold(modifier = Modifier
            .background(LightGray)
            .padding(20.dp, 0.dp), topBar = {
            AppBar()
        }) { innerPadding ->
            val pagerState = rememberPagerState(initialPage = 1) { 3 }
            var selectedTab by remember {
                mutableIntStateOf(pagerState.currentPage)
            }
            LaunchedEffects(pagerState, selectedTab, onChange = { newTabIndex -> selectedTab = newTabIndex })
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)) {
                BelowHeader()
                Tabs(pagerState, selectedTab, onChange = { tabIndex -> selectedTab = tabIndex })
                Column {
                    HorizontalPager(state = pagerState, Modifier.weight(1f)) { currentPage ->
                        when (currentPage) {
                            0 -> ScreenMemories(vmMemories)
                            1 -> ScreenCalendar(vmCalendar)
                            2 -> ScreenAllMeetingsQuestions(vmMultipleMeetings)
                        }
                    }
                    BelowPager(onChange = { callbackValue -> isToShowBottomSheet = callbackValue })
                }
            }
            if (isToShowBottomSheet) {
                ModalBottomSheet(vmMemories, "Ask anything about all memories...", onChange = { newValue -> isToShowBottomSheet = newValue })
            }
        }
    }

    @Composable
    private fun AppBar() {
        TopAppBar(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars), contentPadding = PaddingValues(horizontal = 0.dp), content = {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(LightGray), contentAlignment = Alignment.CenterStart) {
                ToolbarContent()
            }
        })
    }

    @Composable
    fun ToolbarContent() {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopStart) {
                TextButton(onClick = {}) {
                    val userPhoto by vmMain.userPhoto.observeAsState(null)
                    if (userPhoto != null) {
                        Image(bitmap = userPhoto!!.asImageBitmap(), contentDescription = "My image", contentScale = ContentScale.Crop, modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape))
                    } else {
                        Text("No photo", color = Color.Blue)
                    }
                }
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("TwinMind PRO", color = Color.Blue)
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                Text(text = "Help", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    fun LaunchedEffects(pagerState: PagerState, selectedTab: Int, onChange: (Int) -> Unit) {
        LaunchedEffect(selectedTab) {
            pagerState.scrollToPage(selectedTab)
        }
        LaunchedEffect(pagerState.currentPage) {
            onChange(pagerState.currentPage)
        }
    }

    @Composable
    fun BelowHeader() {
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp), text = "Capture 100 Hours to Unlock Features", color = Amber, fontSize = 14.sp)
                        Text(text = "Building Your Second Brain", color = Color.Black, fontSize = 20.sp)
                    }
                    Image(painterResource(R.drawable.gear), contentDescription = "Gear", contentScale = ContentScale.Crop, modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape))
                }
                Row(Modifier.padding(0.dp, 30.dp, 0.dp, 0.dp)) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .height(12.dp)
                        .background(color = Color.Red, shape = RoundedCornerShape(20.dp)))
                    Text(text = "0 / 100 hours", modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp), color = colorResource(android.R.color.black), fontSize = 14.sp)
                }
            }
        }
    }

    @Composable
    fun Tabs(pagerState: PagerState, selectedTab: Int, onChange: (Int) -> Unit) {
        TabRow(backgroundColor = LightGray, modifier = Modifier.height(48.dp), selectedTabIndex = selectedTab) {
            for (index in 0 until pagerState.pageCount) {
                Tab(selected = index == selectedTab, onClick = {
                    onChange(index)
                }) {
                    Text(text = tabTitles[index])
                }
            }
        }
    }

    @Composable
    fun BelowPager(onChange: (Boolean) -> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            OutlinedButton(onClick = {
                onChange(true)
            }) {
                Text("Ask all memories")
            }
            Button(onClick = { startActivity(Intent(this@ActivityMain, ActivityRecorder::class.java)) }) {
                Text("Capture")
            }
        }
    }
}

fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
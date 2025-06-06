package pl.jawegiel.twinmindjakubwegielewski.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.MyApp
import pl.jawegiel.twinmindjakubwegielewski.composable.ModalBottomSheet
import pl.jawegiel.twinmindjakubwegielewski.composable.ScreenNotes
import pl.jawegiel.twinmindjakubwegielewski.composable.ScreenQuestionsSingleMeeting
import pl.jawegiel.twinmindjakubwegielewski.composable.ScreenTranscript
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelNotes
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelRecorder

class ActivityRecorder() : ComponentActivity() {

    private val tabTitles = listOf("Questions", "Notes", "Transcript")
    private val vmNotes by lazy { ViewModelProvider(this)[ViewModelNotes::class.java] }
    private val dateOnStartActivity by lazy { UtilsDate.getCurrentDateTime() }
    private val myApp by lazy { application as MyApp }
    private val questions by lazy { vmRecorder.questions }
    private val transcription by lazy { vmRecorder.transcription }

    private var isBeforeStop = true

    private lateinit var vmRecorder: ViewModelRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmRecorder = ViewModelProvider(this)[ViewModelRecorder::class.java]

        setContent {
            ActivityRecorderScreenLayout()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToActivityMain()
            }
        })

        if (myApp.isConnected) {
            vmRecorder.startAll()
        } else {
            Toast.makeText(this, "Turn on internet first.", Toast.LENGTH_SHORT).show()
        }

        vmRecorder.segmentsNotProcessed.observe(this) { queue ->
            if (vmRecorder.currentSegment > 1) {
                if (queue.isEmpty()) {
                    Toast.makeText(this, "Success! Transcript is up to date.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "There are ${queue.size} more offline segments to process.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    @Composable
    fun ActivityRecorderScreenLayout() {
        Scaffold(modifier = Modifier
            .background(LightGray)
            .padding(20.dp, 0.dp), topBar = {
            AppBar()
        }) { innerPadding ->
            val pagerState = rememberPagerState { 3 }
            var selectedTab by remember {
                mutableIntStateOf(pagerState.currentPage)
            }
            LaunchedEffect(selectedTab) {
                pagerState.scrollToPage(selectedTab)
            }
            LaunchedEffect(pagerState.currentPage) {
                selectedTab = pagerState.currentPage
            }
            var isToShowBottomSheet by remember { mutableStateOf(false) }
            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)) {
                Text(modifier = Modifier.padding(10.dp, 10.dp), text = "Untitled", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(modifier = Modifier.padding(10.dp, 10.dp), text = dateOnStartActivity, color = Color.Black, fontSize = 16.sp)
                TabRow(backgroundColor = LightGray, modifier = Modifier.height(48.dp), selectedTabIndex = selectedTab) {
                    for (index in 0 until pagerState.pageCount) {
                        Tab(selected = index == selectedTab, onClick = {
                            selectedTab = index
                        }) {
                            Text(text = tabTitles[index])
                        }
                    }
                }
                Column {
                    val notesState by vmRecorder.notes.observeAsState(Note(startDate = "", endDate = ""))
                    HorizontalPager(state = pagerState, Modifier.weight(1f)) { currentPage ->
                        when (currentPage) {
                            0 -> ScreenQuestionsSingleMeeting(questions)
                            1 -> ScreenNotes(notesState, vmRecorder, vmNotes.isEditModeOn)
                            2 -> ScreenTranscript(transcription)
                        }
                    }
                    if (notesState.text.isNotEmpty()) {
                        AfterStoppingComposables(notesState)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        OutlinedButton(onClick = {
                            isToShowBottomSheet = true
                        }) {
                            Text("Chat with Transcript")
                        }
                        if (isBeforeStop) {
                            Button(onClick = {
                                lifecycleScope.launch {
                                    vmRecorder.onRecord(false, true, true)
                                    selectedTab = 1
                                    isBeforeStop = false
                                }
                            }) {
                                Text("Stop")
                            }
                        }
                    }
                }
            }
            if (isToShowBottomSheet) {
                ModalBottomSheet(vmRecorder, "Ask anything about current transcription...", onChange = { newValue -> isToShowBottomSheet = newValue })
            }
        }
    }

    @Composable
    private fun AfterStoppingComposables(notesState: Note) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            val isEditModeOn by vmNotes.isEditModeOn.observeAsState(false)
            if (isEditModeOn) {
                OutlinedButton(onClick = {
                    vmRecorder.updateNoteTextInDb(notesState.text)
                    vmNotes.isEditModeEnabled(false)
                }) {
                    Text("Save notes")
                }
            } else {
                OutlinedButton(onClick = {
                    vmNotes.isEditModeEnabled(true)
                }) {
                    Text("Edit notes")
                }
            }
            Button(onClick = {}) {
                Text("Share")
            }
        }
    }

    @Composable
    private fun AppBar() {
        TopAppBar(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars), contentPadding = PaddingValues(horizontal = 0.dp), content = {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(LightGray), contentAlignment = Alignment.CenterStart) {
                CustomToolbarContent()
            }
        })
    }

    @Composable
    fun CustomToolbarContent() {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
            .fillMaxWidth()
            .background(LightGray)) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopStart) {
                TextButton(onClick = {
                    goToActivityMain()
                }) {
                    Text("Back")
                }
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                val timeCounter by vmRecorder.timeCounter.observeAsState("00:00")
                Text(textAlign = TextAlign.Center, text = timeCounter, color = Color.DarkGray)
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopEnd) {
                Text(text = "Text", color = Color.DarkGray)
            }
        }
    }

    fun goToActivityMain() {
        vmRecorder.onRecord(false, true, false)
        Intent(this@ActivityRecorder, ActivityMain::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }.let { startActivity(it) }
    }
}
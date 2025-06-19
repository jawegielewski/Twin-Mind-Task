package pl.jawegiel.twinmindjakubwegielewski.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.activity.isScrolledToEnd
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelQuestionsAllMeetings

@Composable
fun ScreenAllMeetingsQuestions(vmMultipleMeetings: ViewModelQuestionsAllMeetings) {
    val questions by vmMultipleMeetings.questions2.observeAsState(LinkedHashSet<Question>())

    val lazyState = rememberLazyListState()
    val reachedBottom by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            vmMultipleMeetings.apply {
                clearRecursiveValues()
                updateQuestions()
            }
        }
    }
    val questionsMap = vmMultipleMeetings.getQuestionsMap(questions)
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumnQuestions(modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .weight(1f), lazyState, questionsMap, vmMultipleMeetings)
        PreviousQuestionsLoader(questions, vmMultipleMeetings)
    }
}

@Composable
private fun LazyColumnQuestions(modifier: Modifier,
                                lazyState: LazyListState,
                                map: LinkedHashMap<String, List<Question>>,
                                vmMultipleMeetings: ViewModelQuestionsAllMeetings) {
    LazyColumn(modifier, verticalArrangement = Arrangement.Top, state = lazyState) {
        map.forEach { (key, value) ->
            stickyHeader {
                Row(modifier = Modifier
                    .background(LightGray)
                    .fillMaxWidth()
                    .height(32.dp)) {
                    Text(key, fontSize = 18.sp)
                }
            }
            items(value.size) { index ->
                CardItem(value, index)
            }
            item {
                Loader(vmMultipleMeetings)
            }
        }
    }
}

@Composable
private fun CardItem(value: List<Question>, index: Int) {
    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp, 6.dp), elevation = 4.dp) {
        Box(modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(16.dp)) {
            Column {
                Text(value.elementAt(index).text, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Loader(vmMultipleMeetings: ViewModelQuestionsAllMeetings) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 10.dp), contentAlignment = Alignment.Center) {
        val showLoader by vmMultipleMeetings.showLoader.observeAsState(false)
        if (showLoader) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp))
    }
}

@Composable
private fun PreviousQuestionsLoader(questions: java.util.LinkedHashSet<Question>,
                                    vmMultipleMeetings: ViewModelQuestionsAllMeetings) {
    val coroutineScope = rememberCoroutineScope()
    if (questions.isEmpty()) {
        val totalRecursiveCallsLiveData by vmMultipleMeetings.totalRecursiveCallsLiveData.observeAsState(0)
        if (totalRecursiveCallsLiveData >= ViewModelQuestionsAllMeetings.MAX_RECURSIVE_CALLS) {
            ButtonNoResultsLoadMore(coroutineScope, vmMultipleMeetings)
        } else {
            Text("No questions found.", textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxSize()
                .wrapContentSize())
        }
    } else {
        ButtonLoadMore(coroutineScope, vmMultipleMeetings)
    }
}

@Composable
private fun ButtonLoadMore(coroutineScope: CoroutineScope,
                           vmMultipleMeetings: ViewModelQuestionsAllMeetings) {
    Button(onClick = {
        coroutineScope.launch {
            vmMultipleMeetings.apply {
                clearRecursiveValues()
                updateQuestions()
            }
        }
    }) {
        Text("Load more")
    }
}

@Composable
private fun ButtonNoResultsLoadMore(coroutineScope: CoroutineScope,
                                    vmMultipleMeetings: ViewModelQuestionsAllMeetings) {
    Button(onClick = {
        coroutineScope.launch {
            vmMultipleMeetings.apply {
                clearRecursiveValues()
                updateQuestions()
            }
        }
    }) {
        Text("No results found within ${ViewModelQuestionsAllMeetings.MAX_RECURSIVE_CALLS} recursive calls. Do you want to search further?")
    }
}
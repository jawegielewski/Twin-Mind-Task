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
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelMemories

@Composable
fun ScreenMemories(vmMemories: ViewModelMemories) {
    val memories by vmMemories.memories2.observeAsState(LinkedHashSet<Note>())

    val lazyState = rememberLazyListState()
    val reachedBottom by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            vmMemories.apply {
                clearRecursiveValues()
                updateMemories()
            }
        }
    }
    val memoriesMap = vmMemories.getMemoriesMap(memories)
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumnMemories(modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .weight(1f), lazyState, memoriesMap, vmMemories)
        PreviousMemoriesLoader(memories, vmMemories)
    }
}

@Composable
private fun LazyColumnMemories(modifier: Modifier,
                               lazyState: LazyListState,
                               map: LinkedHashMap<String, List<Note>>,
                               vmMemories: ViewModelMemories) {
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
                Loader(vmMemories)
            }
        }
    }
}

@Composable
private fun CardItem(notes: List<Note>, index: Int) {
    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp, 6.dp), elevation = 4.dp) {
        Box(modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(16.dp)) {
            Row {
                val startDate = notes.elementAt(index).startDate
                Text(UtilsDate.getTimeFromDate(startDate), fontWeight = FontWeight.Bold, color = LightGray)
                Text(modifier = Modifier
                    .weight(1f)
                    .padding(10.dp, 0.dp), text = notes.elementAt(index).text, fontWeight = FontWeight.Bold)
                Text(UtilsDate.getTimeDifference(startDate, notes.elementAt(index).endDate), fontWeight = FontWeight.Bold, color = LightGray)
            }
        }
    }
}

@Composable
private fun Loader(vmMemories: ViewModelMemories) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 10.dp), contentAlignment = Alignment.Center) {
        val showLoader by vmMemories.showLoader.observeAsState(false)
        if (showLoader) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp))
    }
}

@Composable
private fun PreviousMemoriesLoader(memories: java.util.LinkedHashSet<Note>,
                                   vmMemories: ViewModelMemories) {
    val coroutineScope = rememberCoroutineScope()
    if (memories.isEmpty()) {
        val totalRecursiveCallsLiveData by vmMemories.totalRecursiveCallsLiveData.observeAsState(0)
        if (totalRecursiveCallsLiveData >= ViewModelMemories.MAX_RECURSIVE_CALLS) {
            ButtonNoResultsLoadMore(coroutineScope, vmMemories)
        } else {
            Text("No memories found.", textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxSize()
                .wrapContentSize())
        }
    } else {
        ButtonLoadMore(coroutineScope, vmMemories)
    }
}

@Composable
private fun ButtonLoadMore(coroutineScope: CoroutineScope, vmMemories: ViewModelMemories) {
    Button(onClick = {
        coroutineScope.launch {
            vmMemories.apply {
                clearRecursiveValues()
                updateMemories()
            }
        }
    }) {
        Text("Load more")
    }
}

@Composable
private fun ButtonNoResultsLoadMore(coroutineScope: CoroutineScope, vmMemories: ViewModelMemories) {
    Button(onClick = {
        coroutineScope.launch {
            vmMemories.apply {
                clearRecursiveValues()
                updateMemories()
            }
        }
    }) {
        Text("No results found within ${ViewModelMemories.MAX_RECURSIVE_CALLS} recursive calls. Do you want to search further?")
    }
}
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.jawegiel.twinmindjakubwegielewski.activity.isScrolledToEnd
import pl.jawegiel.twinmindjakubwegielewski.model.CalendarEvent
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelCalendar

@Composable
fun ScreenCalendar(vmCalendar: ViewModelCalendar) {
    val lazyState = rememberLazyListState()
    val reachedBottom by remember {
        derivedStateOf {
            lazyState.isScrolledToEnd()
        }
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            vmCalendar.updateCalendarEvents()
        } else {
            vmCalendar.clearLoader()
        }
    }
    val calendarEvents by vmCalendar.calendarEvents.observeAsState(LinkedHashSet<CalendarEvent>())
    val calendarEventsMap = vmCalendar.getCalendarEventsMap(calendarEvents)
    LazyColumnCalendarEvents(lazyState, calendarEventsMap, vmCalendar)
}

@Composable
private fun LazyColumnCalendarEvents(lazyState: LazyListState,
                                     map: java.util.LinkedHashMap<String, List<CalendarEvent>>,
                                     vmCalendar: ViewModelCalendar) {
    LazyColumn(modifier = Modifier
        .padding(0.dp, 20.dp, 0.dp, 0.dp)
        .fillMaxSize(), verticalArrangement = Arrangement.Top, state = lazyState) {
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
                Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp), elevation = 4.dp) {
                    Box(modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        Column {
                            Text(value.elementAt(index).title, fontWeight = FontWeight.Bold)
                            Text("${UtilsDate.convertToTime(value.elementAt(index).begin)} - ${UtilsDate.convertToTime(value.elementAt(index).end)}")
                        }
                    }
                }
            }
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 20.dp), contentAlignment = Alignment.Center) {
                    val showLoader by vmCalendar.showLoader.observeAsState(false)
                    if (showLoader) {
                        CircularProgressIndicator()
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp))
                }
            }
        }
    }
}


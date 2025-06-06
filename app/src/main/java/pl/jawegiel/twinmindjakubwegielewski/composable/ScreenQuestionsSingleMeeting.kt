package pl.jawegiel.twinmindjakubwegielewski.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray

@Composable
fun ScreenQuestionsSingleMeeting(meetingQuestions: LiveData<Question>) {
    Column(Modifier
        .fillMaxSize()
        .background(LightGray)
        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        val singleMeetingQuestions by meetingQuestions.observeAsState(Question(startDate = "", endDate = ""))
        Text(text = singleMeetingQuestions.text, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}



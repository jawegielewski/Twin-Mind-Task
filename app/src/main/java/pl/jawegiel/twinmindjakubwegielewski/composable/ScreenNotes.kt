package pl.jawegiel.twinmindjakubwegielewski.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.ui.theme.LightGray
import pl.jawegiel.twinmindjakubwegielewski.viemodel.ViewModelRecorder

@Composable
fun ScreenNotes(note: Note, vmRecorder: ViewModelRecorder, isEditModeOn: LiveData<Boolean>) {
    Column(Modifier
        .fillMaxSize()
        .background(LightGray)
        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        val isEditModeOn by isEditModeOn.observeAsState(false)
        if (isEditModeOn) {
            TextField(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp, 10.dp), value = note.text, onValueChange = {
                vmRecorder.updateNoteText(it)
            }, shape = RoundedCornerShape(8.dp), textStyle = TextStyle(fontSize = 20.sp), label = { Text(fontSize = 16.sp, color = colorResource(android.R.color.darker_gray), text = "Edit notes") })
        } else {
            if (note.text.isEmpty()) {
                Text(text = "Result will show up after pressing \"Stop\" button and when at least first segment comes after 30+ seconds.", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            } else {
                Text(text = note.text, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}




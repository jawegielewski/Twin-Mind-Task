package pl.jawegiel.twinmindjakubwegielewski.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.viemodel.AViewModelTranscriptionChat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(vm: AViewModelTranscriptionChat, title: String, onChange: (Boolean) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(modifier = Modifier
        .fillMaxHeight()
        .systemBarsPadding()
        .padding(0.dp, 56.dp, 0.dp, 0.dp), onDismissRequest = {
        onChange(false)
    }, sheetState = sheetState) {
        HideButton(onChange)
        var isToShowProgress by remember { mutableStateOf(false) }
        Row {
            var value by remember { mutableStateOf("") }
            OutlinedTextField(modifier = Modifier
                .weight(1f)
                .padding(10.dp, 20.dp), value = value, onValueChange = {
                value = it
            }, shape = RoundedCornerShape(8.dp), textStyle = TextStyle(fontSize = 20.sp), label = {
                Text(text = title, fontSize = 16.sp, color = colorResource(android.R.color.darker_gray))
            })
            val coroutineScope = rememberCoroutineScope()
            Button(modifier = Modifier
                .padding(10.dp, 20.dp)
                .align(Alignment.CenterVertically), onClick = {
                coroutineScope.launch {
                    isToShowProgress = true
                    vm.generateContentChatBased(value)
                }
            }) {
                Text("Ask", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        val chatBasedResponse by vm.chatBasedResponse.observeAsState("")
        if (chatBasedResponse.isNotEmpty()) {
            Column(Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center, text = chatBasedResponse, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                isToShowProgress = false
            }
        }
        if (isToShowProgress) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun HideButton(onChange: (Boolean) -> Unit) {
    IconButton(modifier = Modifier
        .size(42.dp)
        .padding(10.dp, 0.dp, 0.dp, 0.dp), onClick = {
        onChange(false)
    }) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Filled.Close,
            contentDescription = "Hide bottom sheet",
            tint = Color.Black,
        )
    }
}
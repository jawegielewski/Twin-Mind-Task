package pl.jawegiel.twinmindjakubwegielewski.viemodel

import android.app.Application
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ServerException
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.jawegiel.twinmindjakubwegielewski.MyApp
import pl.jawegiel.twinmindjakubwegielewski.model.Note
import pl.jawegiel.twinmindjakubwegielewski.model.Question
import pl.jawegiel.twinmindjakubwegielewski.repository.RepositoryDatabase
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsDate
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.LinkedList
import java.util.Queue

private const val TAG = "ViewModelRecorder"

private const val PROMPT_TEXT_QUESTIONS = "Transcribe the following audio file and identify the questions within the transcription. Output only the extracted questions, one question per line."
private const val PROMPT_TEXT_NOTES = "Summarize the following audio file."
private const val PROMPT_TEXT_TRANSCRIPTION = "Transcribe the following audio file."
private const val PROMPT_TEXT_CHAT = "Given a knowledge base attached to a prompt as a byte array, answer to this request:"

private const val NO_RESULTS_WAIT_FOR_SYNCHRONIZATION = "\n\n•••Result not available\nIf it is because of lacking of Internet, just wait 30 seconds for starting synchronization•••\n\n"
private const val NO_RESULT = "\n\n•••Result not available•••\n\n"
private const val NO_RESULT_CHAT = "\n\n•••Result not available :(. Try to ask another question.•••\n\n"

class ViewModelRecorder(val app: Application) : AViewModelTranscriptionChat(app) {

    private val myApp = app as MyApp
    private val model: GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.0-flash")
    private val contentResolver = app.contentResolver
    private val fileDirectory by lazy { getFilePath() }
    private val repositoryDatabase by lazy { RepositoryDatabase(app) }
    private val currentDateWhenStartRecording = UtilsDate.getCurrentDateTime()
    private val notesTextSb = StringBuilder()
    private val segmentsNotProcessedQ: Queue<Int> = LinkedList()

    private val _timeCounter = MutableLiveData<String>()
    val timeCounter: LiveData<String>
        get() = _timeCounter

    private val _questions = MutableLiveData<Question>(Question(startDate = "", endDate = ""))
    val questions: LiveData<Question>
        get() = _questions

    private val _notes = MutableLiveData<Note>(Note(text = "", startDate = "", endDate = ""))
    val notes: LiveData<Note>
        get() = _notes

    private val _transcription = MutableLiveData<String>("")
    val transcription: LiveData<String>
        get() = _transcription

    private val _segmentsNotProcessed = MutableLiveData<Queue<Int>>(LinkedList())
    val segmentsNotProcessed: LiveData<Queue<Int>>
        get() = _segmentsNotProcessed

    private var seconds = 0
    private var minutes = 0
    private var isNewDisconnection = true
    private var recorder1: MediaRecorder? = null
    private var recorder2: MediaRecorder? = null
    private var whichRecorderIsInUse = 1
    private var exceptionCases = 0
    private var noteId: Long = -1
    private var isNoBatchException = true
    private var isRecording = true

    var currentSegment = 1
    var question: Question? = null

    fun startAll() {
        updateTimeCounter()
        initRecorder1()
    }

    fun updateTimeCounter() {
        viewModelScope.launch {
            while (isRecording) {
                delay(1000)
                seconds++

                if (!myApp.isConnected) {
                    try {
                        processNewlyDisconnected()
                    } catch (ex: Exception) {
                        Log.e(TAG, "An exception occurred during processing new disconnection: ${ex.message}")
                    }
                }

                if (seconds % 30 == 0) {
                    viewModelScope.launch {
                        try {
                            processAll()
                        } catch (ex: Exception) {
                            Log.e(TAG, "An exception occurred during 30 seconds period: ${ex.message}")
                        }
                    }
                }
                if (seconds % 60 == 0) {
                    minutes++
                    seconds = 0;
                }
                _timeCounter.value = "%02d:%02d".format(minutes, seconds)
            }
        }
    }

    fun processNewlyDisconnected() {
        if (isNewDisconnection) {
            onRecord(false)
            currentSegment++
            onRecord(true)

            segmentsNotProcessedQ.offer(currentSegment - 1)
            _segmentsNotProcessed.value = segmentsNotProcessedQ

            isNewDisconnection = false
        }
    }

    suspend fun processAll() {
        onRecord(false)
        currentSegment++
        onRecord(true)

        if (myApp.isConnected) {
            if (segmentsNotProcessedQ.isEmpty()) {
                generateAll()
            } else {
                segmentsNotProcessedQ.offer(currentSegment - 1)
                _segmentsNotProcessed.value = segmentsNotProcessedQ
                processNotNewlyDisconnected()
            }
        } else {
            segmentsNotProcessedQ.offer(currentSegment - 1)
            _segmentsNotProcessed.value = segmentsNotProcessedQ
        }
    }

    suspend fun generateAll(segmentNotProcessed: Int = 0) {
        isNoBatchException = true
        try {
            generateQuestions(segmentNotProcessed)
            generateNote(segmentNotProcessed)
            generateTranscription(segmentNotProcessed)
        } catch (ex: Exception) {
            Log.e(TAG, "An exception occurred during generating all responses: ${ex.message}")
        }
    }

    suspend fun processNotNewlyDisconnected() {
        if (!isNewDisconnection) {
            while (!segmentsNotProcessedQ.isEmpty()) {
                generateAll(segmentsNotProcessedQ.poll()!!)
                _segmentsNotProcessed.value = segmentsNotProcessedQ
            }
            isNewDisconnection = true
            exceptionCases = 0
        }
    }

    fun onRecord(start: Boolean, isInterrupted: Boolean = false, isSaving: Boolean = false) =
        if (start) {
            startRecording()
        } else {
            stopRecording(isInterrupted, isSaving)
        }

    private fun getCurrentRecorder(): MediaRecorder? {
        return if (whichRecorderIsInUse == 1) recorder1 else recorder2
    }

    private fun initRecorder1() {
        recorder1 = prepareMediaRecorder()
    }

    private fun startRecording() {
        whichRecorderIsInUse = if (whichRecorderIsInUse == 1) 2 else 1
        if (whichRecorderIsInUse == 1) {
            recorder1 = prepareMediaRecorder()
        } else {
            recorder2 = prepareMediaRecorder()
        }
    }

    fun prepareMediaRecorder(): MediaRecorder {
        return MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(16 * 44100)
            setAudioSamplingRate(44100)
            setOutputFile(getFileNameWithPath(currentSegment))

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
            }

            start()
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            try {
                getCurrentRecorder()!!.apply {
                    stop()
                    release()
                }
            } catch (ex: Exception) {
                Log.e(TAG, "An exception occurred during stopping MediaRecorder: +${ex.message.toString()}")
            }
        }
    }

    private fun stopRecording(isInterrupted: Boolean = false, isSaving: Boolean) {
        if (isInterrupted) {
            insertIntoDb(isSaving)
            isRecording = false
        }
        stopRecording()
    }

    fun insertIntoDb(isSaving: Boolean) {
        if (isSaving) {
            if (question != null) {
                viewModelScope.launch(Dispatchers.IO) {
                    repositoryDatabase.insert(question!!)
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                val endDate = UtilsDate.getDateAfterAddingMinutesAndSecs(currentDateWhenStartRecording, minutes, seconds)
                val note = Note(text = notesTextSb.toString(), startDate = currentDateWhenStartRecording, endDate = endDate)
                if (note.text.isNotEmpty()) {
                    noteId = repositoryDatabase.insert(note)
                    _notes.postValue(repositoryDatabase.findNoteById(noteId))
                }
            }
        }
    }

    fun getFilePath(): String {
        val directory = "${app.externalCacheDir?.absolutePath}/${UtilsDate.getCurrentDateTimeForDirectoryName()}"
        val dir = File(directory)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return directory
    }

    fun getFileNameWithPath(currentSegment: Int, segmentNotProcessed: Int = 0): String {
        var fileName = ""
        fileName = if (segmentNotProcessed > 0) {
            "segment_${segmentNotProcessed}.m4a"
        } else {
            "segment_${currentSegment}.m4a"
        }
        return "${fileDirectory}/${fileName}"
    }

    suspend fun generateContent(segmentNotProcessed: Int = 0,
                                promptText: String,
                                onResponse: (GenerateContentResponse?) -> Unit) {
        var file: File? = null

        file = File(if (segmentNotProcessed == 0) {
            getFileNameWithPath(currentSegment - 1)
        } else {
            getFileNameWithPath(currentSegment - 1, segmentNotProcessed)
        })

        val inputStream = contentResolver.openInputStream(Uri.fromFile(file))

        if (inputStream != null) {
            processStream(inputStream, promptText, onResponse)
        } else {
            Log.e(TAG, "Audio stream is null")
        }
    }

    suspend fun processStream(inputStream: InputStream,
                              promptText: String,
                              onResponse: (GenerateContentResponse?) -> Unit) {
        inputStream.use { stream ->
            val bytes = stream.readBytes()

            val prompt = content {
                inlineData(bytes, "audio/mpeg")
                text(promptText)
            }

            var response: GenerateContentResponse? = null
            try {
                response = model.generateContent(prompt)
            } catch (ex: Exception) {
                Log.e(TAG, "An Exception occurred during generating content: ${ex.message}")
                if (isNoBatchException) {
                    segmentsNotProcessedQ.offer(currentSegment - 1)
                    _segmentsNotProcessed.value = segmentsNotProcessedQ
                    isNoBatchException = false
                }
            }

            onResponse(response)
        }
    }

    suspend fun generateQuestions(segmentNotProcessed: Int = 0) {
        generateContent(segmentNotProcessed, PROMPT_TEXT_QUESTIONS, onResponse = { response ->
            val questionsTextSb = StringBuilder()
            questionsTextSb.append(_questions.value!!.text).append(" ")
                .append(response?.text ?: NO_RESULTS_WAIT_FOR_SYNCHRONIZATION)

            val endDate = UtilsDate.getDateAfterAddingMinutesAndSecs(currentDateWhenStartRecording, minutes, seconds)
            question = Question(text = questionsTextSb.toString(), startDate = currentDateWhenStartRecording, endDate = endDate)
            _questions.value = question
        })
    }

    suspend fun generateNote(segmentNotProcessed: Int = 0) {
        generateContent(segmentNotProcessed, PROMPT_TEXT_NOTES, onResponse = { response ->
            notesTextSb.append(_notes.value!!.text).append(" ").append(response?.text ?: NO_RESULT)
        })
    }

    suspend fun generateTranscription(segmentNotProcessed: Int = 0) {
        generateContent(segmentNotProcessed, PROMPT_TEXT_TRANSCRIPTION, onResponse = { response ->
            val transcriptionTextSb = StringBuilder()
            transcriptionTextSb.append(_transcription.value).append(" ")
                .append(response?.text ?: NO_RESULTS_WAIT_FOR_SYNCHRONIZATION)
            _transcription.value = transcriptionTextSb.toString()
        })
    }

    fun updateNoteText(string: String) {
        _notes.value = notes.value!!.copy(text = string)
    }

    fun updateNoteTextInDb(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryDatabase.updateText(noteId, text)
        }
    }

    override suspend fun generateContentChatBased(textFieldText: String,
                                                  onResponse: (GenerateContentResponse?) -> Unit) {

        transcription.asFlow().collect { transcriptionVal ->
            val prompt = content {
                inlineData(transcriptionVal.toString().toByteArray(), "text/plain")
                text("$PROMPT_TEXT_CHAT $textFieldText")
            }

            var response: GenerateContentResponse? = null
            try {
                response = model.generateContent(prompt)
            } catch (ex: ServerException) {
                Log.e(TAG, "An Exception occurred during generating content: ${ex.message}")
            }

            onResponse(response)
        }
    }

    override suspend fun generateContentChatBased(text: String) {
        generateContentChatBased(text, onResponse = { response ->
            mutChatBasedResponse.value = response?.text ?: NO_RESULT_CHAT
        })
    }
}
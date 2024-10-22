package com.example.dbm.job.presentation

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.job.domain.SaveJobUseCase
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.job.presentation.objects.Photo
import com.example.dbm.job.presentation.objects.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val saveJobUseCase: SaveJobUseCase,
) : ViewModel() {
    var state by mutableStateOf(JobState())
        private set

    var questionState by mutableStateOf(QuestionState())
        private set

    var otherQuestionState by mutableStateOf(
        QuestionState(
            questionId = QuestionIds.OTHER,
            questionTxt = "Please provide a detailed description of the services you are requesting",
            response = null
        )
    )
        private set

    private val _scopeOfWorkCheckBoxState = mutableStateListOf(
        CheckBoxState(
            isChecked = false,
            text = "Structural Engineering",
            questionid = QuestionIds.STRUCTURAL_ENGINEERING
        ),
        CheckBoxState(
            isChecked = false,
            text = "Electrical Engineering",
            questionid = QuestionIds.ELECTRICAL_ENGINEERING
        ),
        CheckBoxState(
            isChecked = false,
            text = "Full Permitting Packet",
            questionid = QuestionIds.FULL_PERMITTING_PACKET
        ),
    )
    var scopeOfWorkCheckBoxStates: List<CheckBoxState> = _scopeOfWorkCheckBoxState

    private val _singlePageCheckBoxState = mutableStateListOf(
        CheckBoxState(
            isChecked = false,
            text = "Single Line Electrical",
            questionid = QuestionIds.SINGLE_LINE_ELECTRICAL
        ),
        CheckBoxState(
            isChecked = false,
            text = "Three Line Electrical",
            questionid = QuestionIds.THREE_LINE_ELECTRICAL
        ),
        CheckBoxState(
            isChecked = false,
            text = "Site Plan/Layouts",
            questionid = QuestionIds.SITE_PLAN_LAYOUTS
        )
    )
    var singlePageCheckBoxState: List<CheckBoxState> = _singlePageCheckBoxState


    private val _projectDetailsQuestionStateList = mutableStateListOf(
        QuestionState(
            questionId = QuestionIds.PROJECT_NAME,
            questionTxt = "Project/Customer Name",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.PROJECT_ADDRESS,
            questionTxt = "Project Address",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.SYSTEM_SIZE,
            questionTxt = "System Size",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.MAKE_MODEL_PANEL,
            questionTxt = "Make and Model of Panel",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.RACKING_SYSTEM,
            questionTxt = "Racking System",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.SUBMISSION_DATE,
            questionTxt = "Submission Date",
            response = null
        ),
    )
    var questionStates: MutableList<QuestionState> = _projectDetailsQuestionStateList

    private val _siteInfoQuestionStateList = mutableStateListOf(
        QuestionState(
            questionId = QuestionIds.ROOF_PITCH,
            questionTxt = "Roof Pitch",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.ROOFING_MATERIAL,
            questionTxt = "Roofing Material",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.RAFTER_SIZE_SPACING,
            questionTxt = "Rafter/Truss Size and Spacing",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.MAIN_SERVICE_PANEL_LOCATION,
            questionTxt = "Location of the Main Service Panel",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.MAIN_BREAKER_SIZE,
            questionTxt = "Size of the Main Breaker:",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.MAIN_BUS_RATING,
            questionTxt = "Rating of the Main Bus",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.PROPOSED_INVERTER_LOCATION,
            questionTxt = "Proposed Location of the Inverter",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.PROPOSED_INTERCONNECTION_METHOD,
            questionTxt = "Proposed Interconnection Method",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.UTILITY_METER_LOCATION,
            questionTxt = "Location of Utility Meter",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.LAYOUT_INFORMATION,
            questionTxt = "Layout Information",
            response = null
        ),
        QuestionState(
            questionId = QuestionIds.ADDITIONAL_DETAILS,
            questionTxt = "Additional Details",
            response = null
        )
    )
    var siteInfoQuestionStateList: MutableList<QuestionState> = _siteInfoQuestionStateList

    private val _sitePhotosStateList = mutableStateListOf(
        PhotoState(
            questionid = QuestionIds.SERVICE_PANEL_LOCATION_PIC,
            question = "Location of Service Panel",
            imageUri = null
        ),
        PhotoState(
            questionid = QuestionIds.METER_NUMBER_PIC,
            question = "Meter Number",
            imageUri = null
        ),
        PhotoState(
            questionid = QuestionIds.MAIN_BREAKER_LOCATION_PIC,
            question = "Main Breaker Location",
            imageUri = null
        ),
        PhotoState(
            questionid = QuestionIds.PANEL_DATA_PIC,
            question = "Panel Data Picture (Legible)",
            imageUri = null
        ),
        PhotoState(
            questionid = QuestionIds.OPEN_PLATE_SERVICE_PANEL_PIC,
            question = "Open Plate Service Panel",
            imageUri = null
        ),
        PhotoState(
            questionid = QuestionIds.SUPPORT_SPACING_PIC,
            question = "Support Spacing",
            imageUri = null
        ),
        PhotoState(
            questionid = QuestionIds.SUPPORT_PHOTOGRAPH,
            question = "Support Photograph",
            imageUri = null
        ),
        PhotoState(questionid = QuestionIds.OBSTACLES_PIC, question = "Obstacles", imageUri = null),
        PhotoState(
            questionid = QuestionIds.FULL_ROOF_PIC,
            question = "Full Roof View",
            imageUri = null
        )
    )
    var sitePhotoStateList: MutableList<PhotoState> = _sitePhotosStateList

    private val eventChannel = Channel<JobEvents>()
    val events = eventChannel.receiveAsFlow()
    private val questionList = mutableMapOf<QuestionIds, Question>()
    private val photoList = mutableListOf<Photo>()

    init {
        state = state.copy(
            email = userPreferences.getUserEmail(),
            name = userPreferences.getUserFullName(),
            userId = userPreferences.getUserId(),
            companyAddress = userPreferences.getCompanyAddress(),
            companyName = userPreferences.getCompanyName(),
            phoneNumber = userPreferences.getUserPhoneNumber(),
            jobId = UUID.randomUUID().toString()
        )
    }

    fun onEvent(event: JobEvents) {
        when (event) {
            is JobEvents.OnCheckMarkSelected -> {
                when (event.questionIds) {
                    QuestionIds.STRUCTURAL_ENGINEERING,
                    QuestionIds.ELECTRICAL_ENGINEERING,
                    QuestionIds.FULL_PERMITTING_PACKET -> {
                        _scopeOfWorkCheckBoxState[event.index] =
                            _scopeOfWorkCheckBoxState[event.index].copy(isChecked = event.isChecked)
                    }

                    QuestionIds.SINGLE_LINE_ELECTRICAL,
                    QuestionIds.THREE_LINE_ELECTRICAL,
                    QuestionIds.SITE_PLAN_LAYOUTS -> {
                        _singlePageCheckBoxState[event.index] =
                            _singlePageCheckBoxState[event.index].copy(isChecked = event.isChecked)
                    }

                    else -> {}
                }
                if (event.isChecked) {
                    addToList(event.questionIds, event.response, event.questionTxt)
                } else {
                    removeFromQuestionList(event.questionIds, event.response)
                }
            }

            is JobEvents.OnQuestionAnswered -> {
                when (event.questionId) {
                    QuestionIds.OTHER -> {
                        otherQuestionState = otherQuestionState.copy(
                            response = event.response
                        )
                    }

                    QuestionIds.PROJECT_NAME,
                    QuestionIds.PROJECT_ADDRESS,
                    QuestionIds.SYSTEM_SIZE,
                    QuestionIds.MAKE_MODEL_PANEL,
                    QuestionIds.MAKE_MODEL_INVERTER,
                    QuestionIds.RACKING_SYSTEM,
                    QuestionIds.SUBMISSION_DATE -> {
                        val index =
                            _projectDetailsQuestionStateList.indexOfFirst { it.questionId == event.questionId }
                        if (index != -1) {
                            _projectDetailsQuestionStateList[index] =
                                _projectDetailsQuestionStateList[index].copy(
                                    response = event.response
                                )
                        }
                    }

                    QuestionIds.ROOF_PITCH,
                    QuestionIds.ROOFING_MATERIAL,
                    QuestionIds.RAFTER_SIZE_SPACING,
                    QuestionIds.MAIN_SERVICE_PANEL_LOCATION,
                    QuestionIds.MAIN_BREAKER_SIZE,
                    QuestionIds.MAIN_BUS_RATING,
                    QuestionIds.PROPOSED_INVERTER_LOCATION,
                    QuestionIds.PROPOSED_INTERCONNECTION_METHOD,
                    QuestionIds.UTILITY_METER_LOCATION,
                    QuestionIds.LAYOUT_INFORMATION,
                    QuestionIds.ADDITIONAL_DETAILS -> {
                        val index =
                            _siteInfoQuestionStateList.indexOfFirst { it.questionId == event.questionId }
                        if (index != -1) {
                            _siteInfoQuestionStateList[index] =
                                _siteInfoQuestionStateList[index].copy(
                                    response = event.response
                                )
                        }
                    }

                    else -> {}
                }
                addToList(event.questionId, event.response, event.questionTxt)
            }

            is JobEvents.OnPhotoAdded -> {
                val index = _sitePhotosStateList.indexOfFirst { it.questionid == event.questionId }
                if (index != -1) {
                    addToPhotoListForQuestion(event.uri, index, event.questionId, event.questionTxt)
                }
            }

            is JobEvents.ToggleRemovePhotoMenu -> {
                state = state.copy(shouldShowPictureMenu = event.toggleRemovePhotoMenu)
            }

            is JobEvents.RemovePhoto -> {
                val index = _sitePhotosStateList.indexOfFirst { it.questionid == event.questionId }
                if (index != -1) {
                    removePhotoFromQuestion(uri = event.uri, questionId = event.questionId)
                }
            }

            is JobEvents.OnSaveJob -> {
                saveJob()
            }

            is JobEvents.OnBackPress -> {
                saveUnsubmittedJob()
                // save unfinished job to the DB and display on the main page
            }
        }
    }

    private fun saveUnsubmittedJob() {
        val listOfQuestion: List<Question> = questionList.values.toList()
        val job = Job(
            formId = state.jobId,
            email = state.email,
            name = state.name,
            userId = state.userId,
            companyAddress = state.companyAddress,
            companyName = state.companyName,
            phoneNumber = state.phoneNumber,
            dateCreated = state.dateCreated,
            questionsAndAnswers = listOfQuestion,
            photoList = photoList,
            wasSubmitted = false
        )
        viewModelScope.launch {
            state = state.copy(showSpinner = true)
            when (val result = saveJobUseCase.saveJobToDB(job)) {
                is Result.Error -> {
                    eventChannel.send(JobEvents.OnSaveFailed(result.error.asUiText()))
                    state = state.copy(showSpinner = false)
                }

                is Result.Success -> {
                    when (val result = saveJobUseCase.saveJobToApi(job)) {
                        is Result.Error -> {
                            eventChannel.send(JobEvents.OnSaveFailed(result.error.asUiText()))
                        }

                        is Result.Success -> {
                            eventChannel.send(JobEvents.OnBackPress)
                            state = state.copy(showSpinner = false)
                        }
                    }
                }
            }
        }
    }

    private fun saveJob() {
        val listOfQuestion: List<Question> = questionList.values.toList()
        if(state.jobId == null){
            state = state.copy(jobId = UUID.randomUUID().toString())
        }
        val job = Job(
            formId = state.jobId,
            email = state.email,
            name = state.name,
            userId = state.userId,
            companyAddress = state.companyAddress,
            companyName = state.companyName,
            phoneNumber = state.phoneNumber,
            dateCreated = state.dateCreated,
            questionsAndAnswers = listOfQuestion,
            photoList = photoList,
            wasSubmitted = true
        )
        viewModelScope.launch {
            state = state.copy(showSpinner = true)
            when (val result = saveJobUseCase.saveJobToDB(job)) {
                is Result.Error -> {
                    eventChannel.send(JobEvents.OnSaveFailed(result.error.asUiText()))
                    state = state.copy(showSpinner = false)
                }

                is Result.Success ->
                    when (val result = saveJobUseCase.saveJobToApi(job)) {
                        is Result.Error -> {
                            eventChannel.send(JobEvents.OnSaveFailed(result.error.asUiText()))
                        }

                        is Result.Success -> {
                            eventChannel.send(JobEvents.OnSaveSuccessful)
                            state = state.copy(showSpinner = false)
                        }
                    }
            }
        }
    }

    private fun removePhotoFromQuestion(questionId: QuestionIds, uri: Uri) {
        // Find the Photo object with the corresponding questionId
        val photoEntry = photoList.find { it.questionIds == questionId }


        // Remove the matching ByteArray from the photo list
        photoEntry?.let { entry ->
            photoList.remove(entry)
        }

        // Update _sitePhotosStateList if necessary
        val index = _sitePhotosStateList.indexOfFirst { it.questionid == questionId }
        if (index != -1) {
            val currentPhotoState = _sitePhotosStateList[index]
            val updatedByteArrays = currentPhotoState.imageUri?.toMutableList() ?: mutableListOf()
            updatedByteArrays.removeAll { it == updatedByteArrays }
            val updatedPhotoState = currentPhotoState.copy(imageUri = updatedByteArrays)
            _sitePhotosStateList[index] = updatedPhotoState
        }
    }


    private fun addToPhotoListForQuestion(
        uri: Uri,
        index: Int,
        questionId: QuestionIds,
        questionTxt: String
    ) {
        // Get the current PhotoState
        val currentPhotoState = _sitePhotosStateList[index]

        // Create a new list with the existing URIs, and add the new URI
        val updatedImageUriList = currentPhotoState.imageUri?.toMutableList() ?: mutableListOf()
        updatedImageUriList.add(uri) // Add the new URI

        // Update the PhotoState with the new list of URIs
        _sitePhotosStateList[index] = currentPhotoState.copy(
            imageUri = updatedImageUriList
        )

        updatedImageUriList.forEach { uri ->
            photoList.add(Photo(uri, questionId))
        }
    }

    private fun removeFromQuestionList(questionIds: QuestionIds, response: String) {
        questionList.entries.removeIf {
            it.value.answer == response && it.key == questionIds
        }
    }

    private fun addToList(questionId: QuestionIds, response: String?, questionTxt: String) {
        val question = questionList[questionId]

        if (question != null) {
            // If the question exists, update the response
            if (response != null) {
                question.answer = response
            }
        } else {
            // If the question does not exist, create a new Question object and add it to the list
            val newQuestion = Question(questionTxt, questionId, response)
            questionList[questionId] = newQuestion
        }
    }

}

    data class JobState(
        val email: String? = null,
        val name: String? = null,
        val userId: String? = null,
        val phoneNumber: String? = null,
        val companyName: String? = null,
        val companyAddress: String? = null,
        val jobId: String? = null,
        val pictureUrl: String? = null,
        val dateCreated: LocalDate = LocalDate.now(),
        val isUserSettingsSelected: Boolean = false,
        val shouldShowPictureMenu: Boolean = false,
        val showSpinner: Boolean = false
    )

    data class QuestionState(
        val questionId: QuestionIds? = null,
        var questionTxt: String? = null,
        var response: String? = null,
    )

    data class CheckBoxState(
        val isChecked: Boolean? = null,
        val text: String? = null,
        val questionid: QuestionIds? = null
    )

    data class PhotoState(
        val question: String? = null,
        val questionid: QuestionIds? = null,
        val imageUri: MutableList<Uri>? = null
    )

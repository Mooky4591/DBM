package com.example.dbm.job.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.job.presentation.objects.Job
import com.example.dbm.job.presentation.objects.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    userPreferences: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(JobState())
        private set

    var questionState by mutableStateOf(QuestionState())
        private set

    var otherQuestionState by mutableStateOf(
        QuestionState(
            questionId = QuestionIds.OTHER,
            questionTxt = "Please provide a detailed description of the services you are requesting",
            response = null)
    )
        private set

    private val _scopeOfWorkCheckBoxState = mutableStateListOf(
        CheckBoxState(isChecked = false, text = "Structural Engineering", questionid = QuestionIds.STRUCTURAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Electrical Engineering", questionid = QuestionIds.ELECTRICAL_ENGINEERING),
        CheckBoxState(isChecked = false, text = "Full Permitting Packet", questionid = QuestionIds.FULL_PERMITTING_PACKET),
    )
    var scopeOfWorkCheckBoxStates: List<CheckBoxState> = _scopeOfWorkCheckBoxState

    private val _singlePageCheckBoxState = mutableStateListOf(
        CheckBoxState(isChecked = false, text = "Single Line Electrical", questionid = QuestionIds.SINGLE_LINE_ELECTRICAL),
        CheckBoxState(isChecked = false, text = "Three Line Electrical", questionid = QuestionIds.THREE_LINE_ELECTRICAL),
        CheckBoxState(isChecked = false, text = "Site Plan/Layouts", questionid = QuestionIds.SITE_PLAN_LAYOUTS)
    )
    var singlePageCheckBoxState: List<CheckBoxState> = _singlePageCheckBoxState


    private val _projectDetailsQuestionStateList = mutableStateListOf(
        QuestionState(questionId = QuestionIds.PROJECT_NAME, questionTxt = "Project/Customer Name", response = null),
        QuestionState(questionId = QuestionIds.PROJECT_ADDRESS, questionTxt = "Project Address", response = null),
        QuestionState(questionId = QuestionIds.SYSTEM_SIZE, questionTxt = "System Size", response = null),
        QuestionState(questionId = QuestionIds.MAKE_MODEL_PANEL, questionTxt = "Make and Model of Panel", response = null),
        QuestionState(questionId = QuestionIds.RACKING_SYSTEM, questionTxt = "Racking System", response = null),
        QuestionState(questionId = QuestionIds.SUBMISSION_DATE, questionTxt = "Submission Date", response = null),
    )
    var questionStates: MutableList<QuestionState> = _projectDetailsQuestionStateList

    private val _siteInfoQuestionStateList = mutableStateListOf(
        QuestionState(questionId = QuestionIds.ROOF_PITCH, questionTxt = "Roof Pitch", response = null),
        QuestionState(questionId = QuestionIds.ROOFING_MATERIAL, questionTxt = "Roofing Material", response = null),
        QuestionState(questionId = QuestionIds.RAFTER_SIZE_SPACING, questionTxt = "Rafter/Truss Size and Spacing", response = null),
        QuestionState(questionId = QuestionIds.MAIN_SERVICE_PANEL_LOCATION, questionTxt = "Location of the Main Service Panel", response = null),
        QuestionState(questionId = QuestionIds.MAIN_BREAKER_SIZE, questionTxt = "Size of the Main Breaker:", response = null),
        QuestionState(questionId = QuestionIds.MAIN_BUS_RATING, questionTxt = "Rating of the Main Bus", response = null),
        QuestionState(questionId = QuestionIds.PROPOSED_INVERTER_LOCATION, questionTxt = "Proposed Location of the Inverter", response = null),
        QuestionState(questionId = QuestionIds.PROPOSED_INTERCONNECTION_METHOD, questionTxt = "Proposed Interconnection Method", response = null),
        QuestionState(questionId = QuestionIds.UTILITY_METER_LOCATION, questionTxt = "Location of Utility Meter", response = null),
        QuestionState(questionId = QuestionIds.LAYOUT_INFORMATION, questionTxt = "Layout Information", response = null),
        QuestionState(questionId = QuestionIds.ADDITIONAL_DETAILS, questionTxt = "Additional Details", response = null)
        )
    var siteInfoQuestionStateList: MutableList<QuestionState> = _siteInfoQuestionStateList

    private val eventChannel = Channel<JobEvents>()
    val events = eventChannel.receiveAsFlow()
    private val job = Job(null,null,null,null,null,null,null,null,null,null)
    private val questionList = mutableMapOf<QuestionIds, Question>()

    init {
        state = state.copy(
            email = userPreferences.getUserEmail(),
            name = userPreferences.getUserFullName(),
            userId = userPreferences.getUserId(),
            companyAddress = userPreferences.getCompanyAddress(),
            companyName = userPreferences.getCompanyName(),
            phoneNumber = userPreferences.getUserPhoneNumber()
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
                        val index = _projectDetailsQuestionStateList.indexOfFirst { it.questionId == event.questionId }
                        if (index != -1) {
                            _projectDetailsQuestionStateList[index] = _projectDetailsQuestionStateList[index].copy(
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
                        val index = _siteInfoQuestionStateList.indexOfFirst { it.questionId == event.questionId }
                        if (index != -1) {
                            _siteInfoQuestionStateList[index] = _siteInfoQuestionStateList[index].copy(
                                response = event.response
                            )
                        }
                    }

                    else -> {}
                }
                addToList(event.questionId, event.response, event.questionTxt)
            }
        }
    }

    private fun removeFromQuestionList(questionIds: QuestionIds, response: String) {
        questionList.entries.removeIf {
            it.value.answer == response && it.key == questionIds
        }
    }

    private fun addToList(questionId: QuestionIds, response: String, questionTxt: String?) {
        if (questionList.containsKey(questionId)) {
            val question = questionList[questionId]
            if (question != null) {
                question.answer = response
            }
        } else {
            val question = Question(questionTxt, questionId, response)
            questionList[questionId] = question
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
    val isUserSettingsSelected: Boolean = false
)

data class QuestionState(
    val questionId: QuestionIds? = null,
    var questionTxt: String? = null,
    var response: String? = null,
)

data class CheckBoxState(
    val isChecked: Boolean,
    val text: String,
    val questionid: QuestionIds
)


package com.example.dbm.job.presentation

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbm.R
import com.example.dbm.domain.user_preferences.UserPreferences
import com.example.dbm.error_handling.domain.Result
import com.example.dbm.error_handling.domain.asUiText
import com.example.dbm.job.constants.QuestionIds
import com.example.dbm.job.domain.JobRepository
import com.example.dbm.job.domain.SaveJobUseCase
import com.example.dbm.job.domain.objects.JobData
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
    userPreferences: UserPreferences,
    private val saveJobUseCase: SaveJobUseCase,
    private val jobRepository: JobRepository,
    private val context: Context
) : ViewModel() {
    var state by mutableStateOf(JobState())
        private set

    var questionState by mutableStateOf(QuestionState())
        private set

    var otherQuestionState by mutableStateOf(QuestionState())
        private set

    var scopeOfWorkCheckBoxStates: MutableList<CheckBoxState> = mutableListOf()
    var singlePageCheckBoxState: MutableList<CheckBoxState> = mutableListOf()
    var questionStates: MutableList<QuestionState> = mutableListOf()
    var siteInfoQuestionStateList: MutableList<QuestionState> = mutableListOf()
    var sitePhotoStateList: MutableList<PhotoState> = mutableListOf()
    private val _sitePhotosStateList: MutableList<PhotoState> = mutableStateListOf()
    private var _scopeOfWorkCheckBoxState: MutableList<CheckBoxState> = mutableStateListOf()
    private var _singlePageCheckBoxState: MutableList<CheckBoxState> = mutableStateListOf()
    private var _projectDetailsQuestionStateList: MutableList<QuestionState> = mutableStateListOf()
    private var _siteInfoQuestionStateList: MutableList<QuestionState> = mutableStateListOf()

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
        )
    }

    private fun setInitialStates(result: JobData?) {

        val questionMap: MutableMap<QuestionIds, String> = mutableMapOf()

        for (question in result?.questionList ?: emptyList()) {
            question.questionId?.let { questionId ->
                questionMap[questionId] = question.answer ?: ""
            }
        }


        for (questionId in QuestionIds.entries) {
            val isChecked = questionMap.containsKey(questionId)
            val answer = questionMap[questionId]
            when(questionId) {
                QuestionIds.STRUCTURAL_ENGINEERING -> addToScopeOfWork(text = context.getString(R.string.structural_engineering), questionId = questionId, isChecked = isChecked, response = answer ?: "")
                QuestionIds.ELECTRICAL_ENGINEERING -> addToScopeOfWork (text = context.getString(R.string.electrical_engineering), questionId = questionId, isChecked = isChecked, response = answer ?: "")
                QuestionIds.FULL_PERMITTING_PACKET -> addToScopeOfWork(text = context.getString(R.string.full_permitting_packet), questionId = questionId, isChecked = isChecked, response = answer ?: "")

                QuestionIds.SINGLE_LINE_ELECTRICAL -> addToSinglePage(context.getString(R.string.single_line_electrical), questionId, isChecked, answer ?: "")
                QuestionIds.THREE_LINE_ELECTRICAL -> addToSinglePage(context.getString(R.string.three_line_electrical), questionId, isChecked, answer ?: "")
                QuestionIds.SITE_PLAN_LAYOUTS -> addToSinglePage(context.getString(R.string.site_plan_layouts), questionId, isChecked, answer ?: "")

                QuestionIds.OTHER -> addToOther(context.getString(R.string.other), questionId, answer ?: "")

                QuestionIds.PROJECT_NAME -> addToProjectDetails(context.getString(R.string.project_customer_name), questionId, answer ?: "")
                QuestionIds.PROJECT_ADDRESS -> addToProjectDetails(context.getString(R.string.project_address), questionId, answer ?: "")
                QuestionIds.SYSTEM_SIZE -> addToProjectDetails(context.getString(R.string.system_size), questionId, answer ?: "")
                QuestionIds.MAKE_MODEL_PANEL -> addToProjectDetails(context.getString(R.string.make_and_model_of_panel), questionId, answer ?: "" )
                QuestionIds.MAKE_MODEL_INVERTER -> addToProjectDetails(context.getString(R.string.proposed_location_of_the_inverter), questionId, answer ?: "")
                QuestionIds.RACKING_SYSTEM -> addToProjectDetails(context.getString(R.string.racking_system), questionId, answer ?: "")
                QuestionIds.SUBMISSION_DATE -> addToProjectDetails(context.getString(R.string.submission_date), questionId, answer ?: "")

                QuestionIds.ROOF_PITCH -> addToSiteInfo(text = context.getString(R.string.roof_pitch), questionId = questionId, answer = answer ?: "")
                QuestionIds.ROOFING_MATERIAL -> addToSiteInfo(context.getString(R.string.roofing_material), questionId, answer ?: "")
                QuestionIds.RAFTER_SIZE_SPACING -> addToSiteInfo(context.getString(R.string.rafter_truss_size_and_spacing), questionId, answer ?: "")
                QuestionIds.MAIN_SERVICE_PANEL_LOCATION -> addToSiteInfo(context.getString(R.string.location_of_the_main_service_panel), questionId, answer ?: "")
                QuestionIds.MAIN_BREAKER_SIZE -> addToSiteInfo(context.getString(R.string.size_of_the_main_breaker), questionId, answer ?: "")
                QuestionIds.MAIN_BUS_RATING -> addToSiteInfo(context.getString(R.string.rating_of_the_main_bus), questionId, answer ?: "")
                QuestionIds.PROPOSED_INVERTER_LOCATION -> addToSiteInfo(context.getString(R.string.proposed_location_of_the_inverter), questionId, answer ?: "")
                QuestionIds.PROPOSED_INTERCONNECTION_METHOD -> addToSiteInfo(context.getString(R.string.proposed_interconnection_method), questionId, answer ?: "")
                QuestionIds.UTILITY_METER_LOCATION -> addToSiteInfo(context.getString(R.string.location_of_utility_meter), questionId, answer ?: "")
                QuestionIds.LAYOUT_INFORMATION -> addToSiteInfo(context.getString(R.string.layout_information), questionId, answer ?: "")
                QuestionIds.ADDITIONAL_DETAILS -> addToSiteInfo(context.getString(R.string.additional_details), questionId, answer ?: "")

                else -> {}
            }

            }
        scopeOfWorkCheckBoxStates = _scopeOfWorkCheckBoxState
        singlePageCheckBoxState = _singlePageCheckBoxState
        questionStates = _projectDetailsQuestionStateList
        siteInfoQuestionStateList = _siteInfoQuestionStateList

        val photoUriMap: MutableMap<QuestionIds, MutableList<Uri>> = mutableMapOf()

// Initialize the map with all possible QuestionIds
        QuestionIds.entries.forEach { questionId ->
            photoUriMap[questionId] = mutableListOf()
        }

// Populate photoUriMap with URIs from result.photoList
        for (photo in result?.photoList ?: emptyList()) {
            photoUriMap[photo.questionIds]?.add(photo.photo)
        }

// Define a map of QuestionIds to their respective question strings
        val questionStringMap = mapOf(
            QuestionIds.SERVICE_PANEL_LOCATION_PIC to context.getString(R.string.location_of_service_panel),
            QuestionIds.METER_NUMBER_PIC to context.getString(R.string.meter_number),
            QuestionIds.MAIN_BREAKER_LOCATION_PIC to context.getString(R.string.main_breaker_location),
            QuestionIds.PANEL_DATA_PIC to context.getString(R.string.panel_data_picture_legible),
            QuestionIds.OPEN_PLATE_SERVICE_PANEL_PIC to context.getString(R.string.open_plate_service_panel),
            QuestionIds.SUPPORT_SPACING_PIC to context.getString(R.string.support_spacing),
            QuestionIds.SUPPORT_PHOTOGRAPH to context.getString(R.string.support_photograph),
            QuestionIds.OBSTACLES_PIC to context.getString(R.string.obstacles),
            QuestionIds.FULL_ROOF_PIC to context.getString(R.string.full_roof_view),
        )

// Iterate over each QuestionId and add to the photo list, using questionStringMap for question text
        for (questionId in QuestionIds.entries) {
            val photoUriList = photoUriMap[questionId]
            val photoList = if (photoUriList.isNullOrEmpty()) null else photoUriList
            val questionText = questionStringMap[questionId]

            questionText?.let {
                addToPhotoList(questionId, question = it, photoList)
            }
        }

// Update the state list
        sitePhotoStateList = _sitePhotosStateList

    }

    private fun addToPhotoList(questionId: QuestionIds, question: String?, uri: MutableList<Uri>?) {
        _sitePhotosStateList.add(
            PhotoState(
                questionId = questionId,
                question = question,
                imageUri = uri
            )
        )
        val index = _sitePhotosStateList.indexOfFirst { it.questionId == questionId }
        if (photoList.isNotEmpty()) {
            if (uri != null) {
                for (newUri in uri) {
                    addToPhotoListForQuestion(uri = newUri, index = index, questionId = questionId)
                }
            }
        }
    }

    private fun addToOther(text: String, questionId: QuestionIds, answer: String) {
        otherQuestionState = QuestionState(questionTxt = text, questionId = questionId, response = answer)
        if (answer.isNotEmpty()) {
            addToList(questionId, answer, text)
        }
    }

    private fun addToSiteInfo(text: String, questionId: QuestionIds, answer: String) {
        _siteInfoQuestionStateList.add(
            QuestionState(
                questionTxt = text,
                questionId = questionId,
                response = answer
            )
        )
        if (answer.isNotEmpty()) {
            addToList(questionId, answer, text)
        }
    }

    private fun addToProjectDetails(text: String, questionId: QuestionIds, answer: String) {
        _projectDetailsQuestionStateList.add(
            QuestionState(
                questionTxt = text,
                questionId = questionId,
                response = answer
            )
        )
        if (answer.isNotEmpty()) {
            addToList(questionId, answer, text)
        }
    }

    private fun addToSinglePage(text: String, questionId: QuestionIds, isChecked: Boolean, response: String) {
        _singlePageCheckBoxState.add(
            CheckBoxState(
                isChecked,
                text,
                questionId
            )
        )
        if (response.isNotEmpty()) {
            addToList(questionId, response, text)
        }
    }

    private fun addToScopeOfWork(text: String, questionId: QuestionIds, isChecked: Boolean, response: String) {
        _scopeOfWorkCheckBoxState.add(
            CheckBoxState(
                isChecked,
                text,
                questionId
            )
        )
        if (response.isNotEmpty()) {
            addToList(questionId, response, text)
        }
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
                val index = _sitePhotosStateList.indexOfFirst { it.questionId == event.questionId }
                if (index != -1) {
                    addToPhotoListForQuestion(event.uri, index, event.questionId)
                }
            }

            is JobEvents.ToggleRemovePhotoMenu -> {
                state = state.copy(shouldShowPictureMenu = event.toggleRemovePhotoMenu)
            }

            is JobEvents.RemovePhoto -> {
                val index = _sitePhotosStateList.indexOfFirst { it.questionId == event.questionId }
                if (index != -1) {
                    removePhotoFromQuestion(uri = event.uri, questionId = event.questionId)
                }
            }

            is JobEvents.OnSaveJob -> {
                saveJob()
            }

            is JobEvents.OnBackPress -> {
                state = state.copy(shouldShowSaveDialog = true)
            }

            is JobEvents.OnSaveUnsubmittedJob -> {
                saveUnsubmittedJob()
            }

            is JobEvents.ToggleShouldSaveMenu -> {
                state = state.copy(shouldShowSaveDialog = event.toggleShouldSaveMenu)
                if (!event.toggleShouldSaveMenu) {
                    viewModelScope.launch {
                        eventChannel.send(JobEvents.OnBackPress)
                    }
                }
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
        if(state.jobId.isNullOrEmpty()){
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
        val index = _sitePhotosStateList.indexOfFirst { it.questionId == questionId }
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

    fun setJobId(jobId: String) {
        state = if (jobId.trim().isEmpty()){
            state.copy(
                jobId = UUID.randomUUID().toString()
            )
        } else {
            state.copy(
                jobId = jobId,
                isNewJob = false
            )
        }
        isNewJob()
    }

    private fun isNewJob(){
        if(!state.isNewJob) {
            viewModelScope.launch {
                val jobId = state.jobId ?: return@launch
                when(val result = jobRepository.getJobByJobId(jobId)) {
                    is Result.Error -> {
                        TODO()
                    }
                    is Result.Success -> {
                        setInitialStates(result.data)
                    }
                }
            }
        } else {
            setInitialStates(null)
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
        val shouldShowSaveDialog: Boolean = false,
        val showSpinner: Boolean = false,
        val isNewJob: Boolean = true
    )

    data class QuestionState(
        val questionId: QuestionIds? = null,
        var questionTxt: String? = null,
        var response: String? = null,
    )

    data class CheckBoxState(
        val isChecked: Boolean? = null,
        val text: String? = null,
        val questionId: QuestionIds? = null
    )

    data class PhotoState(
        val question: String? = null,
        val questionId: QuestionIds? = null,
        val imageUri: MutableList<Uri>? = null
    )

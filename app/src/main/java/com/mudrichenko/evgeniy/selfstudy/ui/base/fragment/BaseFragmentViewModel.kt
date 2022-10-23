package com.mudrichenko.evgeniy.selfstudy.ui.base.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenko.evgeniy.selfstudy.ui.screen.main.model.MainActivityNavigationData
import com.mudrichenko.evgeniy.selfstudy.ui.screen.main.model.MainBottomNavigationGraph

abstract class BaseFragmentViewModel : ViewModel() {

    private var _navigationEvent = MutableLiveData<SingleLiveEvent<NavigationCommand>>()
    val navigationEvent: LiveData<SingleLiveEvent<NavigationCommand>> = _navigationEvent

    protected fun navigateTo(direction: NavigationCommand.NavigateTo.Direction) {
        _navigationEvent.value = SingleLiveEvent(NavigationCommand.NavigateTo(direction))
    }

    fun navigateBack() {
        _navigationEvent.value = SingleLiveEvent(NavigationCommand.Back)
    }

    private var _mainActivityNavigationEvent = MutableLiveData<SingleLiveEvent<MainActivityNavigationData>>()
    val mainActivityNavigationEvent: LiveData<SingleLiveEvent<MainActivityNavigationData>> = _mainActivityNavigationEvent

    protected fun navigate(
        bottomNavigationGraph: MainBottomNavigationGraph,
        destinationId: Int
    ) {
        _mainActivityNavigationEvent.value = SingleLiveEvent(
            MainActivityNavigationData(
                bottomNavigationGraph = bottomNavigationGraph,
                destinationId = destinationId
            )
        )
    }

    private var _showSnackBarEvent = MutableLiveData<SingleLiveEvent<InfoSnackBarModel>>()
    val showSnackBarEvent: LiveData<SingleLiveEvent<InfoSnackBarModel>> = _showSnackBarEvent

    protected fun showSnackBar(infoSnackBarModel: InfoSnackBarModel) {
        _showSnackBarEvent.value = SingleLiveEvent(infoSnackBarModel)
    }

    protected fun <T> showSnackBar(dataResponseError: DataResponse.Error<T>) {
        _showSnackBarEvent.value = SingleLiveEvent(
            InfoSnackBarModel(
                textResId = dataResponseError.appError.getStringResource()
            )
        )
    }

}
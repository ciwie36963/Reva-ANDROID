package com.example.beardwulf.reva.domain

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.beardwulf.reva.Endpoint
import com.example.beardwulf.reva.base.InjectedViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class ExhibitorViewModel : InjectedViewModel() {

    @Inject
    lateinit var endpoint: Endpoint

    private var currentExhibitor = MutableLiveData<Exhibitor>()
    private var previousExhibitor :  Exhibitor? = null
    var isNew: Boolean = true
    /**
     * Indicates whether the loading view should be displayed.
     */
    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var subscription: Disposable
    fun getNextExhibitor(group : String) {
        isNew = true
        Log.d("GROUP",group);
        subscription = endpoint.getExhibitor(group)
                //we tell it to fetch the data on background by
                .subscribeOn(Schedulers.io())
                //we like the fetched data to be displayed on the MainTread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveExhibitorStart() }
                .doOnTerminate { onRetrieveExhibitorFinish() }
                .subscribe(
                        { result -> onRetrieveExhibitorSuccess(result) },
                        { error -> onRetrieveExhibitorError(error) }
                )
    }
    private fun onRetrieveExhibitorError(error: Throwable) {
        Log.d("ERROREX", error.toString());
    }

    private fun onRetrieveExhibitorSuccess(result: Exhibitor) {
        Log.d("OBS", "CHANGED")
        currentExhibitor.value?.let {
            Log.d("OBS", "PREVIOUS CHANGED")
            previousExhibitor = it
        }
        currentExhibitor.value = result
        isNew = false;


    }

    private fun onRetrieveExhibitorFinish() {

        loadingVisibility.value = false
    }

    private fun onRetrieveExhibitorStart() {

        loadingVisibility.value = true
    }

    /**
     * Disposes the subscription when the [InjectedViewModel] is no longer used.
     */
    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }


    fun getExhibitor(): MutableLiveData<Exhibitor> {
        return currentExhibitor
    }
    fun getPreviousExhibitor() : Exhibitor?{
        return previousExhibitor;
    }
}
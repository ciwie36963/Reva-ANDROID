package com.example.beardwulf.reva.injection.component


import com.example.beardwulf.reva.injection.module.NetworkModule
import com.example.beardwulf.reva.domain.ExhibitorViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing the inject functions for presenters.
 * Components act as the bridge between the Modules that know how to provide dependencies
 * and the actual objects that require something to be injected.
 *
 * More info can be found in [the documentation](https://google.github.io/dagger/api/2.14/dagger/Component.html)
 */
@Singleton
/**
 * All modules that are required to perform the injections into the listed objects should be listed
 * in this annotation
 */
@Component(modules = [NetworkModule::class])
interface ViewModelComponent {

    /**
     * Injects the dependencies into the specified MetarViewModel.
     * @param metarViewModel the [MetarViewModel] in which to inject the dependencies.
     */
   // fun inject(metarViewModel: MetarViewModel)

    fun inject(exhibitorViewModel: ExhibitorViewModel)
}
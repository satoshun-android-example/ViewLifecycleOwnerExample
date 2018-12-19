package com.github.satoshun.example.sample

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidInjectionModule::class,
    MainModule::class
  ]
)
interface AppComponent : AndroidInjector<App>

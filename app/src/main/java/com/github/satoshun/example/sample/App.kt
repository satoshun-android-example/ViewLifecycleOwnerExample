package com.github.satoshun.example.sample

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.create()
  }
}

package com.github.satoshun.example.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.satoshun.example.sample.databinding.MainActBinding
import dagger.Module
import dagger.android.AndroidInjection
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity() {
  @Inject lateinit var factory: ViewModelFactory<MainViewModel>

  private val viewModel: MainViewModel by viewModels { factory }

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<MainActBinding>(this, R.layout.main_act)
    viewModel.data.observe(this, Observer {
      binding.title.text = it.toString()
    })
  }
}

class MainViewModel @Inject constructor(
  private val userHandler: UserHandler,
  private val intHandler: IntHandler
) : ViewModel() {
  private val job = GlobalScope.launch {
    while (true) {
      delay(2000)
      data.postValue((data.value ?: 0) + 1)
    }
  }

  val data = MutableLiveData<Int>()

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }
}

class UserHandler @Inject constructor()
class IntHandler @Inject constructor()

@Module
interface MainModule {
  //  @ContributesAndroidInjector(modules = [MainFragmentModule::class])
  @ContributesAndroidInjector
  fun contributeMainActivity(): MainActivity
}

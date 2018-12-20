package com.github.satoshun.example.sample

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.satoshun.example.sample.databinding.MainActBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<MainActBinding>(this, R.layout.main_act)

    if (savedInstanceState == null) {
      val fragment1 = MainFragment()
      supportFragmentManager.commitNow {
        add(R.id.frame, fragment1, "main1")
      }
    }

    val fragment = supportFragmentManager.findFragmentByTag("main1")!!
    launch {
//      val fragment2 = MainFragment()
//      while (true) {
//        delay(3000)
//        supportFragmentManager.commitNow {
//          if (fragment.isAdded) {
//            replace(R.id.frame, fragment2, "main1")
//          }
//          if (fragment2.isAdded) {
//            replace(R.id.frame, fragment, "main1")
//          }
//        }
//      }

      while (true) {
        delay(3000)
        supportFragmentManager.commitNow {
          if (fragment.isDetached) attach(fragment)
          else detach(fragment)
        }
      }

//      val fragment2 = MainFragment()
//      while (true) {
//        delay(3000)
//        supportFragmentManager.commitNow {
//          if (fragment.isAdded) {
//            remove(fragment)
//            add(fragment2.id, fragment2)
//          }
//          if (fragment2.isAdded) {
//            remove(fragment2)
//            add(fragment.id, fragment)
//          }
//        }
//      }

//      while (true) {
//        delay(3000)
//        supportFragmentManager.commit {
//          if (fragment.isHidden) show(fragment)
//          else hide(fragment)
//        }
//      }
    }
  }
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModels(
  crossinline vmCreator: (() -> VM)
) = activityViewModels<VM> {
  object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return vmCreator() as T
    }
  }
}

class MainViewModel : ViewModel() {
  private val job = GlobalScope.launch {
    while (true) {
      delay(1000)
      counter.postValue((counter.value ?: 0) + 1)
    }
  }

  val counter = MutableLiveData<Int>()

  override fun onCleared() {
    super.onCleared()
    job.cancel()
  }
}

package com.github.satoshun.example.sample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
      while (true) {
        delay(3000)
        supportFragmentManager.commit {
          if (fragment.isDetached) attach(fragment)
          else detach(fragment)
        }
      }

//      while (true) {
//        delay(3000)
//        supportFragmentManager.commit {
//          if (fragment.isAdded) remove(fragment)
//          else add(R.id.frame, fragment, "main1")
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

class MainFragment : Fragment() {
  private val viewModel by activityViewModels(vmCreator = { MainViewModel() })

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Log.d("main", "createView")
    return TextView(context)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Log.d("main", "onActivityCreated")

    viewModel.counter.observe(viewLifecycleOwner, Observer {
      Log.d("main1", "$it $this")
    })

    viewModel.counter.observe(this, Observer {
      Log.e("main2", "$it $this")
    })

    // get size
    val field = LiveData::class.java.getDeclaredField("mObservers")
    field.isAccessible = true
    val v = field.get(viewModel.counter)
    Log.d("size", (v as Iterable<*>).count().toString())
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)
    Log.d("main", "hidden $hidden")
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    Log.d("main", "attached")
  }

  override fun onDetach() {
    super.onDetach()
    Log.d("main", "detached")
  }

  override fun onDestroyView() {
    super.onDestroyView()
    Log.d("main", "onDestroyView")
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d("main", "onDestroy")
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

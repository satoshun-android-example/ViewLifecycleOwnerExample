package com.github.satoshun.example.sample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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

//    viewModel.counter.observe(this, Observer {
//      Log.e("main2", "$it $this")
//    })

    viewModel.counter.observe(this, object : Observer<Int> {
      override fun onChanged(t: Int) {
        Log.e("main33", "$t")
      }
    })

    viewModel.counter.observe(this, Observer {
      Log.e("main3", "$it")
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

package com.example.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(private val bindingToInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun onViewCreatedLight()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingToInflate(inflater, container, false)
        return binding.root
    }
    open fun buttonListener(){}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedLight()
        buttonListener()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
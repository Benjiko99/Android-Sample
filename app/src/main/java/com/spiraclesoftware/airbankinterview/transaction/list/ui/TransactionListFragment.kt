package com.spiraclesoftware.airbankinterview.transaction.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.spiraclesoftware.airbankinterview.databinding.TransactionListFragmentBinding

class TransactionListFragment : Fragment() {

    private lateinit var binding: TransactionListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TransactionListFragmentBinding.inflate(inflater)
        return binding.root
    }
}

/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.feature.logger.logs.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.feature.logger.basic.BaseFragment
import com.sample.feature.logger.databinding.FragmentLogsBinding
import com.sample.feature.logger.logs.ui.contract.LogUIEvent
import com.sample.feature.logger.logs.ui.contract.LogUIIntent
import com.sample.feature.logger.logs.ui.contract.LogUIState
import com.sample.feature.logger.logs.ui.viewmodel.LogsViewModel
import com.sample.feature.logger.logs.util.FileUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Fragment that displays the database logs.
 */
@AndroidEntryPoint
class LogsFragment : BaseFragment<FragmentLogsBinding>() {

    private val viewModel: LogsViewModel by viewModels()

    @Inject
    lateinit var viewBinder: LogsViewBinder

    private lateinit var adapter: LogsViewAdapter

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLogsBinding {
        return FragmentLogsBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        super.setupViews()
        initView()
        observeUiState()
        observeEvents()
        userIntent()
    }

    private fun initView() {
        adapter = LogsViewAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    //避免重复创建实例
                    // viewBinder.bind(binding, state)
                    render(state)
                }
            }
        }
    }

    private fun render(state: LogUIState) {
        when (state) {
            is LogUIState.LogsData -> {
                adapter.logsDataSet = state.logs
                adapter.notifyDataSetChanged()
            }

            is LogUIState.Empty -> Unit
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.uiEvent
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { event ->
                    handleUIEvents(event)
                }
        }
    }


    private fun handleUIEvents(event: LogUIEvent) {
        when (event) {
            is LogUIEvent.OpenLogDir -> {
                openLogDir(event.dirUri)
            }
        }
    }

    /**
     * Context参数不建议参数传递
     */
    private fun userIntent() {
        binding.clearLogs.setOnClickListener {
            viewModel.dispatchIntent(LogUIIntent.OnClearClicked)
        }
        binding.exportLogs.setOnClickListener {
            viewModel.dispatchIntent(LogUIIntent.OnExportClicked)
        }
        binding.openDir.setOnClickListener {
            viewModel.dispatchIntent(LogUIIntent.OnOpenClicked)
        }
        binding.startLog.setOnClickListener {
            viewModel.dispatchIntent(LogUIIntent.OnStartClicked)
        }
        binding.endLog.setOnClickListener {
            viewModel.dispatchIntent(LogUIIntent.OnEndClicked)
        }
    }

    private fun openLogDir(uri: Uri) {
       /* val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)*/
        FileUtil().openFileManager(requireContext()) {

        }
    }

}


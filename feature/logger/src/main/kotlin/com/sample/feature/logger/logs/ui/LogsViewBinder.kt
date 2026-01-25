package com.sample.feature.logger.logs.ui

import com.sample.core.basic.ui.view.ViewStateBinder
import com.sample.feature.logger.logs.ui.contract.LogUIState
import javax.inject.Inject

class LogsViewBinder @Inject constructor() : ViewStateBinder<FragmentLogsBinding, LogUIState> {
    override fun bind(
        binding: FragmentLogsBinding,
        state: LogUIState
    ) {

        when (state) {
            is LogUIState.Empty -> {

            }

            is LogUIState.LogsData -> {
                binding.recyclerView.adapter = LogsViewAdapter(state.logs)
            }
        }
    }
}
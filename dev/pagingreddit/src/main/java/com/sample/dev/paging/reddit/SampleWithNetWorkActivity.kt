/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.sample.dev.paging.reddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.dev.paging.lib.reddit.repository.RedditPostRepository
import com.sample.dev.paging.reddit.databinding.ActivityNetworkSampleBinding
import com.sample.dev.paging.reddit.ui.RedditActivity

/**
 * chooser activity for the demo.
 */
class SampleWithNetWorkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNetworkSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.withDatabase.setOnClickListener {
            show(RedditPostRepository.Type.DB)
        }
        binding.networkOnly.setOnClickListener {
            show(RedditPostRepository.Type.IN_MEMORY_BY_ITEM)
        }
        binding.networkOnlyWithPageKeys.setOnClickListener {
            show(RedditPostRepository.Type.IN_MEMORY_BY_PAGE)
        }
    }

    private fun show(type: RedditPostRepository.Type) {
        val intent = RedditActivity.intentFor(this, type)
        startActivity(intent)
    }
}

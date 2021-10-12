/*
 * Copyright (c) 2021 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.mobile.android.ui.viewbinding

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> AppCompatDialog.viewBinding() = DialogViewBindingDelegate(T::class.java, this)

class DialogViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val dialog: AppCompatDialog
) : ReadOnlyProperty<AppCompatDialog, T> {
    private var binding: T? = null

    override fun getValue(thisRef: AppCompatDialog, property: KProperty<*>): T {
        binding?.let { return it }

        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        binding = inflateMethod.invoke(null, LayoutInflater.from(dialog.context)).cast<T>()

        return binding!!
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> Any.cast(): T = this as T
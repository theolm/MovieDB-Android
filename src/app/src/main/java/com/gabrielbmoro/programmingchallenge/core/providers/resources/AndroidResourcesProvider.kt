package com.gabrielbmoro.programmingchallenge.core.providers.resources

import android.content.res.Resources
import androidx.annotation.ArrayRes

class AndroidResourcesProvider constructor(
    private val resources: Resources
) : ResourcesProvider {

    override fun getArray(@ArrayRes arrayRes: Int): Array<String> {
        return resources.getStringArray(arrayRes)
    }

    override fun getString(stringRes: Int): String {
        return resources.getString(stringRes)
    }
}
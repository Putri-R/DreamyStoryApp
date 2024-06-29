package com.myapp.dreamystory

import com.myapp.dreamystory.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt = $i",
                "name + $i",
                "description = $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(stories)
        }
        return items
    }
}
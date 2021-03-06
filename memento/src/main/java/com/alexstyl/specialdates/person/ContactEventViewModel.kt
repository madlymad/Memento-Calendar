package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date

data class ContactEventViewModel(val evenName: String, val dateLabel: String, val date: Date) : PersonDetailItem {
    override val value: String
        get() = dateLabel
    override val label: String
        get() = evenName
}

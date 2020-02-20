package com.example.mynotesapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note (
    var id: Int = 0,
    var title: String? = null,
    var descrription: String? = null,
    var date: String? = null
) : Parcelable
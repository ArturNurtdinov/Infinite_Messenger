package com.topaz.easymessenger.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val username: String, val profileImageURL: String) : Parcelable {
    constructor() : this("", "", "")
}
package com.onemb.messengeros.model

import android.os.Parcel
import android.os.Parcelable

data class ConversationArgs(val senderName: String?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(senderName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConversationArgs> {
        override fun createFromParcel(parcel: Parcel): ConversationArgs {
            return ConversationArgs(parcel)
        }

        override fun newArray(size: Int): Array<ConversationArgs?> {
            return arrayOfNulls(size)
        }
    }
}

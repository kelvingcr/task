package br.com.kelvingcr.task.model

import android.os.Parcelable
import br.com.kelvingcr.task.ui.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task (
    var id: String = "",
    var description: String = "",
    var status: Int = 0
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}
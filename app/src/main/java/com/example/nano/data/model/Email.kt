package com.example.nano.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.Date

@Entity
data class Email(
    @PrimaryKey val eid: Int,
    val sender: Int,
    val recipients: List<Int>,
    val subject: String,
    val body: String,
    val attachments: List<EmailAttachment>,
    var isImportant: Boolean,
    var isStarred: Boolean,
    var mailbox: MailboxType,
    val createdAt: Date,
    val updateAt: Date? = null,
    val viewAt: Date? = null,
    val threads: List<Int>
)

data class EmailWrapper(
    val emailList: List<Email>,
    val isLocalData: Boolean
)

enum class MailboxType {
    INBOX, DRAFTS, SENT, SPAM, TRASH
}

@JsonClass(generateAdapter = true)
data class EmailAttachment(
    val resUrl: String,
    val contentDesc: String
)
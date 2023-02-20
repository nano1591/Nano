package com.example.nano.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Email(
    @PrimaryKey val eid: Long,
    val sender: Long,
    val recipients: List<Long>,
    val subject: String,
    val body: String,
    val attachments: List<EmailAttachment>,
    var isImportant: Boolean,
    var isStarred: Boolean,
    var mailbox: MailboxType,
    val createdAt: Date,
    val updateAt: Date? = null,
    val viewAt: Date? = null,
    val threads: List<Long>
)

data class EmailWrapper(
    val emailList: List<Email>,
    val isLocalData: Boolean
)

enum class MailboxType {
    INBOX, DRAFTS, SENT, SPAM, TRASH
}

data class EmailAttachment(
    val resUrl: String,
    val contentDesc: String
)
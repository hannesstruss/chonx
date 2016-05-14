package chonx.bot.telegram.types

// TODO incomplete
data class Message(val message_id: Int,
                   val from: User?,
                   val date: Int,
                   val chat: Chat,
                   val forward_from: User?,
                   val forward_from_chat: Chat?,
                   val forward_date: Int?,
                   val reply_to_message: Message?,
                   val text: String?,
                   // Bunch more optional things
                   val contact: Contact?
                   )


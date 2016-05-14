package chonx.bot.telegram.types

data class KeyboardButton(val text: String,
                          val request_contact: Boolean? = null,
                          val request_location: Boolean? = null)

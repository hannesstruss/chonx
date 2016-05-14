package chonx.bot.telegram.requests

import chonx.bot.telegram.types.ReplyKeyboardMarkup

data class SendMessage(val chat_id: Int,
                       val text: String,
                       val parse_mode: String? = null,
                       val disable_web_page_preview: Boolean? = null,
                       val disable_notification: Boolean? = null,
                       val reply_to_message_id: Int? = null,
                       val reply_markup: ReplyKeyboardMarkup? = null)

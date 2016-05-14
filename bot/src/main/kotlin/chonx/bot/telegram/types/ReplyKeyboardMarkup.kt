package chonx.bot.telegram.types

data class ReplyKeyboardMarkup(val keyboard: List<List<KeyboardButton>>,
                               val resize_keyboard: Boolean? = null,
                               val one_time_keyboard: Boolean? = null,
                               val selective: Boolean? = null)

package chonx.bot.telegram

import chonx.bot.AuthException
import chonx.bot.telegram.requests.SendMessage
import chonx.bot.telegram.types.*
import retrofit2.Call

class Telegram(private val api: TelegramApi) {
  fun getMe(): User = api.getMe().executeOrFail()

  fun getUpdates(offset: Long = 0, timeout: Int = 0): List<Update> =
      api.getUpdates(offset, timeout).executeOrFail()

  fun sendMessage(chatId: Int, text: String, keyboard: ReplyKeyboardMarkup? = null): Message {
    return api.sendMessage(SendMessage(chatId, text, reply_markup = keyboard)).executeOrFail()
  }

  private fun <T> Call<Result<T>?>.executeOrFail(): T {
    val response = this.execute()
    val body = response.body()
    if (response.code() == 301 || response.code() == 302) {
      val req = response.raw().request()
      throw AuthException("${req.method()} ${req.url()} -> ${response.code()}")
    }

    return body!!.result
  }
}

package chonx.bot

import chonx.bot.telegram.types.Result
import chonx.bot.telegram.types.Update
import chonx.bot.telegram.types.User
import retrofit2.Call

class Telegram(private val api: TelegramApi, private val token: String) {
  fun getMe(): User = api.getMe(token).executeOrFail()

  fun getUpdates(): List<Update> = api.getUpdates(token).executeOrFail()

  fun sendMessage(userId: Int, text: String) {
    api.sendMessage(token, userId, text).execute()
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

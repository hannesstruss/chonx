package chonx.bot

import retrofit2.Call

class Telegram(private val api: TelegramApi, private val botToken: String) {
  fun getMe(): User = api.getMe(botToken).executeOrFail()

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

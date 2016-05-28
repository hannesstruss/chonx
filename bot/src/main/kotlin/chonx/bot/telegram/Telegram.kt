package chonx.bot.telegram

import chonx.bot.AuthException
import chonx.bot.telegram.requests.SendMessage
import chonx.bot.telegram.types.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class Telegram(private val api: TelegramApi) {
  companion object {
    fun fromToken(botToken: String): Telegram {
      val okClient = OkHttpClient.Builder()
          .followRedirects(false)
          .readTimeout(1, TimeUnit.MINUTES)
          .connectTimeout(1, TimeUnit.MINUTES)
          .build()

      val api = Retrofit.Builder()
          .baseUrl("https://api.telegram.org/bot${botToken}/")
          .client(okClient)
          .addConverterFactory(ScalarsConverterFactory.create())
          .addConverterFactory(MoshiConverterFactory.create())
          .build()
          .create(TelegramApi::class.java)

      return Telegram(api)
    }
  }

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

package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.TelegramApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun main(args: Array<String>) {
  val okClient = OkHttpClient.Builder().followRedirects(false).build()

  val botToken = System.getProperty("chonx.bot")
  val api = Retrofit.Builder()
      .baseUrl("https://api.telegram.org/bot${botToken}/")
      .client(okClient)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(TelegramApi::class.java)

  val telegram = Telegram(api)
  telegram.getUpdates().forEach { println(it) }
}


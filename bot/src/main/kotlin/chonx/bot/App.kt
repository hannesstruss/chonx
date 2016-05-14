package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.TelegramApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
  val okClient = OkHttpClient.Builder()
      .followRedirects(false)
      .readTimeout(1, TimeUnit.MINUTES)
      .connectTimeout(1, TimeUnit.MINUTES)
      .build()

  val botToken = System.getProperty("chonx.bot")
  val api = Retrofit.Builder()
      .baseUrl("https://api.telegram.org/bot${botToken}/")
      .client(okClient)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(TelegramApi::class.java)

  val telegram = Telegram(api)

  println("Listening for updates...")

  val echobot = EchoBot(telegram)
  Updates(telegram).updates()
      .toBlocking()
      .forEach { update ->
        println(update.message?.text)
        echobot.onUpdate(update)
      }
}


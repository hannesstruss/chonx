package chonx.bot.telegram

import chonx.bot.telegram.types.Message
import chonx.bot.telegram.types.Result
import chonx.bot.telegram.types.Update
import chonx.bot.telegram.types.User
import retrofit2.Call
import retrofit2.http.*

/**
 * Since Telegram wants colons in their paths, we use the ./ workaround throughout
 * this interface.
 * See: https://github.com/square/retrofit/issues/1253#issuecomment-158127707
 */
interface TelegramApi {
  @GET("getMe")
  fun getMe(): Call<Result<User>?>

  @GET("getUpdates")
  fun getUpdates(): Call<Result<List<Update>>?>

  @POST("sendMessage")
  @FormUrlEncoded
  fun sendMessage(@Field("chat_id") chatId: Int,
                  @Field("text") text: String): Call<Result<Message>?>
}

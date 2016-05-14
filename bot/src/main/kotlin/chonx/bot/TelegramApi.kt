package chonx.bot

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
  @GET("./bot{token}/getMe")
  fun getMe(@Path("token") token: String): Call<Result<User>?>

  @GET("./bot{token}/getUpdates")
  fun getUpdates(@Path("token") token: String): Call<Result<List<Update>>?>

  @POST("./bot{token}/sendMessage")
  @FormUrlEncoded
  fun sendMessage(@Path("token") token: String,
                  @Field("chat_id") chatId: Int,
                  @Field("text") text: String): Call<Any?>
}

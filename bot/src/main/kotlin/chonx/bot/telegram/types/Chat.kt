package chonx.bot.telegram.types

data class Chat(val id: Int,
                val type: String,
                val title: String?,
                val username: String?,
                val first_name: String?,
                val last_name: String?)

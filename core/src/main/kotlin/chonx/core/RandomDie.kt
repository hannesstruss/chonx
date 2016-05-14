package chonx.core

import java.util.*

class RandomDie : Die {
  private val random = Random()
  override fun roll() = random.nextInt(6) + 1
}

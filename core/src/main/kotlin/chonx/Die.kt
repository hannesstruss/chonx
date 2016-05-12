package chonx

import java.util.*

class Die {
  val random = Random()

  fun roll() = random.nextInt(7) - 1
}

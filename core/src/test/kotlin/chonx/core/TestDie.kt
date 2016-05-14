package chonx.core

class TestDie(var result: Int = 0) : Die {
  override fun roll() = result
}

package chonx.core

class TestDie(var result: Int = 1) : Die {
  override fun roll() = result
}

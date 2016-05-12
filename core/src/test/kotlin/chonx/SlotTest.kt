package chonx

import chonx.Slot.*
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test

class SlotTest {
  @Test fun testUpperHalf() {
    pointsOf(THREES, 1, 2, 3, 2, 1) shouldBe 3
    pointsOf(TWOS, 2, 1, 5, 6, 4) shouldBe 2
    pointsOf(THREES, 3, 3, 5, 6, 4) shouldBe 6
    pointsOf(FOURS, 4, 4, 5, 6, 4) shouldBe 12
    pointsOf(FIVES, 2, 1, 5, 5, 4) shouldBe 10
    pointsOf(SIXES, 6, 6, 6, 6, 4) shouldBe 24
  }

  @Test fun testThreeOfAKind() {
    pointsOf(THREE_OF_A_KIND, 3, 3, 3, 2, 2) shouldBe 13
    pointsOf(THREE_OF_A_KIND, 1, 2, 3, 4, 5) shouldBe 0
  }

  @Test fun testFourOfAKind() {
    pointsOf(FOUR_OF_A_KIND, 1, 1, 2, 1, 1) shouldBe 6
    pointsOf(FOUR_OF_A_KIND, 1, 2, 5, 4, 3) shouldBe 0
  }

  @Test fun testFullHouse() {
    pointsOf(FULL_HOUSE, 2, 2, 2, 5, 5) shouldBe 25
    pointsOf(FULL_HOUSE, 4, 4, 6, 6, 6) shouldBe 25
    pointsOf(FULL_HOUSE, 1, 2, 3, 4, 5) shouldBe 0
    pointsOf(FULL_HOUSE, 5, 5, 5, 5, 5) shouldBe 0
  }

  @Test fun testSmallStraight() {
    pointsOf(SMALL_STRAIGHT, 1, 2, 3, 4, 6) shouldBe 30
    pointsOf(SMALL_STRAIGHT, 6, 3, 2, 1, 4) shouldBe 30
    pointsOf(SMALL_STRAIGHT, 1, 2, 3, 4, 5) shouldBe 30
    pointsOf(SMALL_STRAIGHT, 1, 2, 3, 5, 6) shouldBe 0
    pointsOf(SMALL_STRAIGHT, 1, 1, 2, 3, 3) shouldBe 0
  }

  @Test fun testLargeStraight() {
    pointsOf(LARGE_STRAIGHT, 1, 2, 3, 4, 5) shouldBe 40
    pointsOf(LARGE_STRAIGHT, 5, 3, 2, 1, 4) shouldBe 40
    pointsOf(LARGE_STRAIGHT, 1, 2, 3, 5, 6) shouldBe 0
    pointsOf(LARGE_STRAIGHT, 1, 1, 2, 3, 3) shouldBe 0
  }

  @Test fun testChance() {
    pointsOf(CHANCE, 1, 1, 1, 1, 1) shouldBe 5
    pointsOf(CHANCE, 1, 2, 4, 5, 6) shouldBe 18
    pointsOf(CHANCE, 6, 6, 6, 6, 6) shouldBe 30
  }

  @Test fun testFiveOfAKind() {
    pointsOf(FIVE_OF_A_KIND, 1, 1, 1, 1, 1) shouldBe 50
    pointsOf(FIVE_OF_A_KIND, 6, 6, 6, 6, 6) shouldBe 50
    pointsOf(FIVE_OF_A_KIND, 2, 3, 3, 3, 3) shouldBe 0
  }

  private infix fun Int.shouldBe(value: Int) {
    assertThat(this).isEqualTo(value)
  }

  private fun pointsOf(slot: Slot, die1: Int, die2: Int, die3: Int, die4: Int, die5: Int) =
    slot.points(DiceRoll(die1, die2, die3, die4, die5))
}

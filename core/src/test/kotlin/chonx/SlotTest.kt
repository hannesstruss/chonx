package chonx

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Test

class SlotTest {
  @Test fun testUpperHalf() {
    assertThat(Slot.ACES.points(DiceRoll(1, 2, 3, 2, 1))).isEqualTo(2)
    assertThat(Slot.TWOS.points(DiceRoll(2, 1, 5, 6, 4))).isEqualTo(2)
    assertThat(Slot.THREES.points(DiceRoll(3, 3, 5, 6, 4))).isEqualTo(6)
    assertThat(Slot.FOURS.points(DiceRoll(4, 4, 5, 6, 4))).isEqualTo(12)
    assertThat(Slot.FIVES.points(DiceRoll(2, 1, 5, 5, 4))).isEqualTo(10)
    assertThat(Slot.SIXES.points(DiceRoll(6, 6, 6, 6, 4))).isEqualTo(24)
  }

  @Test fun testThreeOfAKind() {
    assertThat(Slot.THREE_OF_A_KIND.points(DiceRoll(3, 3, 3, 2, 2))).isEqualTo(13)
    assertThat(Slot.THREE_OF_A_KIND.points(DiceRoll(1, 2, 3, 4, 5))).isEqualTo(0)
  }

  @Test fun testFourOfAKind() {
    fail()
  }

  @Test fun testFullHouse() {
    fail()
  }
}

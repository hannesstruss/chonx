package chonx.core

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class MoveInProgressTest {
  @Rule @JvmField val expect = ExpectedException.none()

  private val testDie: TestDie = TestDie(4)
  private val move = MoveInProgress.start(Player("Hannes"), testDie)

  @Test fun `should start with initialized dies`() {
    assertThat(move.dice()).containsExactly(4, 4, 4, 4, 4)
  }

  @Test fun `should roll all dice again if none was locked`() {
    testDie.result = 3

    assertThat(move.roll().dice()).containsExactly(3, 3, 3, 3, 3)
  }

  @Test fun `should roll only unlocked dice`() {
    val lockedMove = move.lock(0).lock(3)
    testDie.result = 3

    assertThat(lockedMove.roll().dice()).containsExactly(4, 3, 3, 4, 3)
  }

  @Test fun `should be possible to unlock dice again`() {
    val lockedMove = move.lock(0).lock(2).lock(4).unlock(2)
    testDie.result = 3

    assertThat(lockedMove.roll().dice()).containsExactly(4, 3, 3, 3, 4)
  }

  @Test fun `should only allow rolling if there are rolls left`() {
    expect.expect(IllegalStateException::class.java)
    move.roll().roll().roll().roll()
  }
}

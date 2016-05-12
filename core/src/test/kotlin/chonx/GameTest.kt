package chonx

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GameTest {
  val hannes = Player("hannes")
  val felix = Player("felix")
  val players = listOf(hannes, felix)

  @Test fun shouldMoveToNextPlayerAfterMove() {
    val game = Game.new(players)

    assertThat(game.currentPlayer).isEqualTo(hannes)

    val nextGame = game.move(DiceRoll(1, 2, 5, 4, 2), Slot.ACES)

    assertThat(nextGame.currentPlayer).isEqualTo(felix)

    val thirdGame = nextGame.move(DiceRoll(1, 5, 4, 2, 2), Slot.CHANCE)

    assertThat(thirdGame.currentPlayer).isEqualTo(hannes)
  }

  @Test fun shouldCalculateScore() {
    val game = Game.new(players)

    val nextGame = game.move(DiceRoll(1, 1, 1, 1, 1), Slot.CHANCE)

    assertThat(nextGame.score(hannes)).isEqualTo(5)
  }

  @Test fun shouldCalculateUpperHalfBonus() {
    val score = Game.new(listOf(hannes))
        .move(DiceRoll(1, 1, 1, 1, 1), Slot.ACES)
        .move(DiceRoll(2, 2, 2, 2, 2), Slot.TWOS)
        .move(DiceRoll(3, 3, 3, 3, 3), Slot.THREES)
        .move(DiceRoll(4, 4, 4, 4, 4), Slot.FOURS)
        .move(DiceRoll(5, 5, 5, 5, 5), Slot.FIVES)
        .move(DiceRoll(6, 6, 6, 6, 6), Slot.SIXES)
        .score(hannes)

    assertThat(score).isEqualTo(140)
  }
}

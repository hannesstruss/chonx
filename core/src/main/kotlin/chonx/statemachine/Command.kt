package chonx.statemachine

import chonx.core.Player
import chonx.core.Slot

sealed class Command {
  class AddPlayer(val player: String) : Command()
  class BeginGame() : Command()
  class RollDice() : Command()
  class LockDice(val indices: List<Int>) : Command()
  class UnlockDice(val indices: List<Int>) : Command()
  class AcceptDice() : Command()
  class PickSlot(val slot: Slot) : Command()
}

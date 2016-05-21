package chonx.cli2

import statemachine.Command
import statemachine.StateMachine

fun main(args: Array<String>) {
  val state = StateMachine.new()

  state
      .handle(Command.AddPlayer("Hannes"))
      .handle(Command.AddPlayer("Dieter"))
      .handle(Command.BeginGame())


}


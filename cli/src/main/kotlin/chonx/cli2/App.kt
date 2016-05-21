package chonx.cli2

fun main(args: Array<String>) {
  val state = StateMachine.new()

  state
      .handle(Command.AddPlayer("Hannes"))
      .handle(Command.AddPlayer("Dieter"))
      .handle(Command.BeginGame())


}


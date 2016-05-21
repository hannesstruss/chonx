package statemachine

class Reducer<P : Phase, C : Command>(
    val phaseClass: Class<P>,
    val commandClass: Class<C>,
    val reduceFunc: (P, C) -> Phase
) {
  @Suppress("UNCHECKED_CAST")
  fun reduce(phase: Phase, command: Command): Phase {
    return reduceFunc(phase as P, command as C)
  }
}

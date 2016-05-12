package chonx

public fun <T> List<T>.pairs(): List<Pair<T, T>> {
  return this.mapIndexed { index, i -> if (index + 1 < this.size) Pair(i, this[index + 1]) else null }.filterNotNull()
}

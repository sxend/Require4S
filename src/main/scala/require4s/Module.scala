package require4s

/**
 * Created by sxend on 14/04/28.
 */
trait Module[M] {
  def export: M
}

object Module {
  def export[M](f: => M): Module[M] = new Module[M] {
    override def export: M = f
  }
}
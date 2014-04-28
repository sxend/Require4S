package require4s

/**
 * Created by sxend on 14/04/28.
 */
trait Module[M] {
  def apply(): M
}

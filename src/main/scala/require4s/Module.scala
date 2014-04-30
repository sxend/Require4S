package require4s

/**
 * Created by sxend on 14/04/28.
 */
trait Module[M] {
  self =>
  def export: M

  val id: String = self.getClass.getName
}

object Module {
  def wrap[M](module: Module[M], f: => M): Module[M] = new Module[M] {
    override val id = module.id

    override def export: M = f
  }
}
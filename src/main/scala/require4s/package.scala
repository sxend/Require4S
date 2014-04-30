
/**
 * Created by sxend on 14/04/28.
 */
package object require4s {
  self =>

  lazy val require: Require = initRequire()
  lazy val global: Require = require

  private def initRequire() = {
    def initModules() = {
      Map[String, Any]()
    }
    var modules = initModules()
    new Require {
      override def apply[M](module: Module[M]): M = {
        modules.getOrElse(module.id, {
          define(module)
        }).asInstanceOf[M]
      }

      override def define[M](module: Module[M]): M = {
        val m = module.export
        modules = modules updated(module.id, m)
        m
      }

      override def flush(): Unit = {
        modules = initModules()
      }

    }
  }

  trait Require {
    def apply[M](module: Module[M]): M

    def define[M](module: Module[M]): M

    def flush()

  }

}

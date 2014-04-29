

/**
 * Created by sxend on 14/04/28.
 */
package object require4s {
  self =>

  import scalaz.Memo

  private[this] lazy val container: Container = {
    def initAlias() = {
      Map[Module[_], Module[_]]()
    }
    var alias = initAlias()
    def initCache() = Memo.immutableHashMapMemo {
      module: Module[_] => {
        alias.getOrElse(module, module).export
      }
    }
    var cache = initCache()
    new Container {
      override def apply[M](module: Module[M]): M = {
        cache(module).asInstanceOf[M]
      }

      override def aliasing[M](origin: Module[M], target: Module[M]): Unit = {
        alias = alias updated(origin, target)
      }

      override def flushAll(): Unit = {
        alias = initAlias()
        cache = initCache()
      }

      override def flushCache(): Unit = {
        cache = initCache()
      }
    }
  }

  def require[M](module: Module[M]): M = {
    container(module)
  }

  def alias[M](origin: Module[M], target: Module[M]): Unit = {
    self.synchronized {
      container.aliasing(origin, target)
    }
  }

  def flushAll() = self.synchronized {
    container.flushAll()
  }

  def flushCache() = self.synchronized {
    container.flushCache()
  }


  private trait Container {
    def apply[M](module: Module[M]): M

    def aliasing[M](origin: Module[M], target: Module[M])

    def flushAll()

    def flushCache()
  }

}

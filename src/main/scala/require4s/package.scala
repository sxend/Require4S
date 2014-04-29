

/**
 * Created by sxend on 14/04/28.
 */
package object require4s {
  self =>

  lazy val require: Require = {
    import scalaz.Memo
    def initAlias() = {
      Map[Module[_], Module[_]]()
    }
    var al = initAlias()
    def initCache() = Memo.immutableHashMapMemo {
      module: Module[_] => {
        al.getOrElse(module, module).export
      }
    }
    var cache = initCache()
    new Require {
      override def apply[M](module: Module[M]): M = {
        cache(module).asInstanceOf[M]
      }

      override def alias[M](origin: Module[M], target: Module[M]): Unit = {
        al = al updated(origin, target)
      }

      override def flushAll(): Unit = {
        al = initAlias()
        cache = initCache()
      }

      override def flushCache(): Unit = {
        cache = initCache()
      }

      override def flushAlias(): Unit = {
        al = initAlias()
      }
    }
  }

  trait Require {
    def apply[M](module: Module[M]): M

    def alias[M](origin: Module[M], target: Module[M])

    def flushAll()

    def flushCache()

    def flushAlias()
  }

}

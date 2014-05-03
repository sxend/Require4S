Require4S
=========

```scala:sample.scala
import require4s._

object Sample {

  def main(args: Array[String]): Unit = {
    {
      val foo = require[Foo]
      println(foo.fooCall()) // default-foo : default-bar
    }
    require.refresh() // binding and instances all-refreshed
    require.define[Bar, MockBar] // Class Foo binding overridden

    {
      val foo = require[Foo]
      println(foo.fooCall()) // default-foo : mock-bar
    }

  }
}

trait Foo {
  def fooCall(): String
}

@Module(classOf[Foo])
class DefaultFoo extends Foo {
  override def fooCall() = {
    val bar = require[Bar]
    "default-foo" + " : " + bar.barCall()
  }
}

trait Bar {
  def barCall(): String
}

@Module(classOf[Bar])
class DefaultBar extends Bar {
  override def barCall(): String = "default-bar"
}

class MockBar extends Bar {
  override def barCall(): String = "mock-bar"
}
```

LICENSE
=======

MIT
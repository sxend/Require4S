Require4S
=========

```scala:sample.scala
import require4s._

object Sample {

  def main(args: Array[String]): Unit = {
    {
      val foo = require(Foo)
      println(foo.fooCall()) // default-foo : default-bar
    }

    require4s.flushAll()
    require4s.alias(Bar, new Module[Bar] {
      override def apply(): Bar = new MockBar
    })

    {
      val foo = require(Foo)
      println(foo.fooCall()) // default-foo : mock-bar
    }

  }
}


object Foo extends Module[Foo] {
  override def apply(): Foo = new DefaultFoo
}

trait Foo {
  def fooCall():String
}

class DefaultFoo extends Foo {
  override def fooCall() = {
    val bar = require(Bar)
    "default-foo" +" : "+ bar.barCall()
  }
}

object Bar extends Module[Bar] {
  override def apply(): Bar = new DefaultBar
}

trait Bar {
  def barCall():String
}

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
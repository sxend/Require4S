package test.require4s

import require4s._

/**
 * Created by sxend on 14/04/29.
 */
object Sample {

  def main(args: Array[String]): Unit = {
    {
      val foo = require(Foo)
      println(foo.fooCall())
    }

    require4s.flushAll()
    require4s.alias(Bar, new Module[Bar] {
      override def apply(): Bar = new MockBar
    })

    {
      val foo = require(Foo)
      println(foo.fooCall())
    }

  }
}

object Foo extends Module[Foo] {
  override def apply(): Foo = new DefaultFoo
}

trait Foo {
  def fooCall()
}

class DefaultFoo extends Foo {
  override def fooCall() = {
    val bar = require(Bar)
    "default-foo" + bar.barCall()
  }
}

object Bar extends Module[Bar] {
  override def apply(): Bar = new DefaultBar
}

trait Bar {
  def barCall()
}

class DefaultBar extends Bar {
  override def barCall() = "default-bar"
}

class MockBar extends Bar {
  override def barCall(): Unit = "mock-bar"
}
package test.require4s

import require4s._

/**
 * Created by sxend on 14/04/29.
 */
object Sample {

  def main(args: Array[String]): Unit = {
    {
      val foo = require(Foo)
      println(foo.fooCall()) // default-foo : default-bar
    }

    require.flushAll()
    require.alias(Bar, Module.export[Bar](new MockBar))

    {
      val foo = require(Foo)
      println(foo.fooCall()) // default-foo : mock-bar
    }

  }
}


object Foo extends Module[Foo] {
  override def export = new DefaultFoo
}

trait Foo {
  def fooCall(): String
}

class DefaultFoo extends Foo {
  override def fooCall() = {
    val bar = require(Bar)
    "default-foo" + " : " + bar.barCall()
  }
}

object Bar extends Module[Bar] {
  override def export = new DefaultBar
}

trait Bar {
  def barCall(): String
}

class DefaultBar extends Bar {
  override def barCall(): String = "default-bar"
}

class MockBar extends Bar {
  override def barCall(): String = "mock-bar"
}
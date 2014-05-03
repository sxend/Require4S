package test.require4s

import require4s._

/**
 * Created by sxend on 14/04/29.
 */
object Sample {

  def main(args: Array[String]): Unit = {
    require.define[Bar, DefaultBar]

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

class DefaultBar extends Bar {
  override def barCall(): String = "default-bar"
}

class MockBar extends Bar {
  override def barCall(): String = "mock-bar"
}
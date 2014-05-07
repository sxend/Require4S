import com.google.inject.util.Modules
import com.google.inject.{Binder, Stage, AbstractModule, Guice}
import org.reflections._
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util._
import scala.reflect.ClassTag
import scala.collection.JavaConversions._

/**
 * Created by sxend on 14/04/28.
 */
package object require4s {
  self =>

  lazy val require: Require = initRequire()

  private[this] def classCheckAndBind[A, B <: A](binder: Binder, from: ClassTag[A], to: ClassTag[B]) = {
    val f = from.runtimeClass.asInstanceOf[Class[A]]
    val t = to.runtimeClass.asInstanceOf[Class[B]]
    if (f != t) {
      binder.bind[A](f).to(t)
    } else {
      binder.bind[A](f)
    }
  }

  private def initRequire[A, B <: A]() = {

    val module: com.google.inject.Module = new AbstractModule {
      override def configure(): Unit = {
        new Reflections(new ConfigurationBuilder()
          .addUrls(ClasspathHelper.forPackage(""))
          .setScanners(new TypeAnnotationsScanner()))
          .getTypesAnnotatedWith(classOf[Module])
          .foreach(m => {
          classCheckAndBind(binder,
            ClassTag[A](m.getAnnotation(classOf[Module]).value()),
            ClassTag[B](m)
          )
        })
      }
    }

    def initInjector(module: com.google.inject.Module) = {
      Guice.createInjector(Stage.PRODUCTION, module)
    }
    var injector = initInjector(module)
    new Require {
      override def apply[M: ClassTag](implicit tag: ClassTag[M]): M = {
        injector.getInstance(tag.runtimeClass.asInstanceOf[Class[M]])
      }

      override def define[M: ClassTag, E <: M : ClassTag](implicit from: ClassTag[M], to: ClassTag[E]): Unit = {
        injector = initInjector(Modules.`override`(module).`with`(new AbstractModule {
          override def configure(): Unit = {
            classCheckAndBind(binder, from, to)
          }
        }))
      }

      override def prepare[M: ClassTag, E <: M : ClassTag](implicit from: ClassTag[M], to: ClassTag[E]): Definable[M, E] = {
        Definable[M, E](from = from, to = to)
      }

      override def defineAll(definables: Definable[_, _]*): Unit = {
        injector = initInjector(Modules.`override`(module).`with`(definables.map(_.modularize)))
      }

      override def refresh(): Unit = {
        injector = initInjector(module)
      }
    }
  }

  trait Require {
    def apply[A: ClassTag](implicit tag: ClassTag[A]): A

    def define[A: ClassTag, B <: A : ClassTag](implicit from: ClassTag[A], to: ClassTag[B]): Unit

    def refresh(): Unit

    def defineAll(definables: Definable[_, _]*): Unit

    def prepare[A: ClassTag, B <: A : ClassTag](implicit from: ClassTag[A], to: ClassTag[B]): Definable[A, B]
  }

  case class Definable[A, B <: A](from: ClassTag[A], to: ClassTag[B]) {

    private[require4s] def modularize: com.google.inject.Module = {
      new AbstractModule {
        override def configure(): Unit = {
          classCheckAndBind(binder, from, to)
        }
      }
    }
  }

}
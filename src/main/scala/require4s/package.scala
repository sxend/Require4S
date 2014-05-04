import com.google.inject.util.Modules
import com.google.inject.{Stage, AbstractModule, Guice}
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

  private def initRequire[A]() = {
    val module: com.google.inject.Module = new AbstractModule {
      override def configure(): Unit = {
        new Reflections(new ConfigurationBuilder()
          .addUrls(ClasspathHelper.forPackage(""))
          .setScanners(new TypeAnnotationsScanner()))
          .getTypesAnnotatedWith(classOf[Module])
          .foreach(m => {
          bind[A](m.getAnnotation(classOf[Module]).value().asInstanceOf[Class[A]]).to(m.asInstanceOf[Class[A]])
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
            bind[M](from.runtimeClass.asInstanceOf[Class[M]]).to(to.runtimeClass.asInstanceOf[Class[E]])
          }
        }))
      }

      override def refresh() = {
        injector = initInjector(module)
      }
    }
  }

  trait Require {
    def apply[A: ClassTag](implicit tag: ClassTag[A]): A

    def define[A: ClassTag, B <: A : ClassTag](implicit from: ClassTag[A], to: ClassTag[B]): Unit

    def refresh(): Unit
  }

}
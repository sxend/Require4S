import com.google.inject.{AbstractModule, Guice}
import org.reflections._
import org.reflections.scanners.{ResourcesScanner, SubTypesScanner, TypeAnnotationsScanner}
import org.reflections.util._
import scala.reflect.ClassTag
import scala.collection.JavaConversions._

/**
 * Created by sxend on 14/04/28.
 */
package object require4s {
  self =>

  lazy val require: Require = initRequire()

  private def initRequire() = {
    def initInjector() = {
      Guice.createInjector(new AbstractModule {
        override def configure(): Unit = {
          val reflections = new Reflections(new ConfigurationBuilder()
            .addUrls(ClasspathHelper.forPackage("test"))
            .setScanners(new ResourcesScanner(),
              new TypeAnnotationsScanner(),
              new SubTypesScanner()))
          reflections
            .getTypesAnnotatedWith(classOf[Module])
            .foreach(m => {
            bind(m.getAnnotation(classOf[Module]).value()).to(m)
          })
        }
      })
    }
    var injector = initInjector()
    new Require {
      override def apply[A: ClassTag](implicit tag: ClassTag[A]): A = {
        injector.getInstance(tag.runtimeClass.asInstanceOf[Class[A]])
      }

      override def define[A: ClassTag, B <: A : ClassTag](implicit from: ClassTag[A], to: ClassTag[B]): Unit = {
        injector = injector.createChildInjector(new AbstractModule {
          override def configure(): Unit = {
            bind[A](from.runtimeClass.asInstanceOf[Class[A]]).to(to.runtimeClass.asInstanceOf[Class[B]])
          }
        })
      }

      override def flush() = {
        injector = initInjector()
      }
    }
  }

  trait Require {
    def apply[A: ClassTag](implicit tag: ClassTag[A]): A

    def define[A: ClassTag, B <: A : ClassTag](implicit from: ClassTag[A], to: ClassTag[B]): Unit

    def flush(): Unit
  }

}
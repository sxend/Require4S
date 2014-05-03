import com.google.inject.{AbstractModule, Guice}
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
    def initInjector(basePackage: String = "") = {
      Guice.createInjector(new AbstractModule {
        override def configure(): Unit = {
          val reflections = new Reflections(new ConfigurationBuilder()
            .addUrls(ClasspathHelper.forPackage(basePackage))
            .setScanners(new TypeAnnotationsScanner()))
          val binder = this.binder()
          reflections
            .getTypesAnnotatedWith(classOf[Module])
            .foreach(m => {
            ModuleBinder.bind[A](binder, m.getAnnotation(classOf[Module]).value().asInstanceOf[Class[A]], m.asInstanceOf[Class[A]])
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

      override def refresh(basePackage: String) = {
        injector = initInjector(basePackage)
      }
    }
  }

  trait Require {
    def apply[A: ClassTag](implicit tag: ClassTag[A]): A

    def define[A: ClassTag, B <: A : ClassTag](implicit from: ClassTag[A], to: ClassTag[B]): Unit

    def refresh(basePackage: String = ""): Unit
  }

}
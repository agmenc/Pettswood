import org.pettswood.specification.concepts.HelloWorld
import org.pettswood.{DomainBridge, Mixin}

class MyFirstMixin(domain: DomainBridge) extends Mixin(domain) {
  domain.learn("Hello", () => new HelloWorld())
}
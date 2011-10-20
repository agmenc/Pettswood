import org.pettswood.DomainBridge
import org.pettswood.specification.concepts._

class Bootstrap {
  // TODO - Pass class reference, and instantiate a new one each time we need it
  // TODO - add many name/value pairs at once
  DomainBridge.learn("Pettswood", () => new Pettswood( /* dependencies */ ))
  DomainBridge.learn("Maths", () => new Maths( /* dependencies */ ))
  DomainBridge.learn("Mirror", () => new Mirror( /* dependencies */ ))
}
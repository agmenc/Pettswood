import org.pettswood.specification.concepts._
import org.pettswood.{Grouper, DomainBridge}

class Bootstrap(domain: DomainBridge) extends Grouper(domain) {
  // TODO - add many name/value pairs at once
  domain.learn("Pettswood", () => new Pettswood( /* dependencies */ ))
  domain.learn("Results", () => new Results(domain.summary))
  domain.learn("Maths", () => new Maths( /* dependencies */ ))
  domain.learn("Mirror", () => new Mirror( /* dependencies */ ))
}
import org.pettswood.specification.concepts._
import org.pettswood.{Mixin, DomainBridge}

class Cryptography(domain: DomainBridge) extends Mixin(domain) {
  domain.learn("Maths", () => new Maths(/* dependencies */))
  domain.learn("Mirror", () => new Mirror(/* dependencies */))
}
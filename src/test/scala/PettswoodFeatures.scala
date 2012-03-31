import org.pettswood.specification.concepts._
import org.pettswood.{Mixin, DomainBridge}

class PettswoodFeatures(domain: DomainBridge) extends Mixin(domain) {
  domain.learn("Pettswood", () => new Pettswood())
  domain.learn("Nested Tables Demo", () => new NestedTablesDemo())
  domain.learn("Results", () => new Results(domain.summary))
}
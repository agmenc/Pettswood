import org.pettswood.specification.concepts._
import org.pettswood.{DomainBridge, Mixin}

class PettswoodFeatures(domain: DomainBridge) extends Mixin(domain) {
  domain.learn("Pettswood", () => new Pettswood())
  domain.learn("Nested Tables Demo", () => new NestedTablesDemo())
  domain.learn("Html In Tables Demo", () => new HtmlInTablesDemo())
  domain.learn("Results", () => new TimedResults(domain.summary))

  domain.learn("Show Count", () => new ShowCount(domain.state))
  domain.learn("Increment", () => new Increment(domain.state))
}

package org.ensime.indexer

import org.ensime.indexer.ClassName._
import org.ensime.util.ParboiledParser
import org.parboiled.errors.{ ErrorUtils, ParsingException }
import org.parboiled.scala._

/**
 * Parser for Java Descriptors as defined in Section 4.3 of
 * http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
 */
object DescriptorParser extends ParboiledParser[Descriptor] {
  private val TypeParser = local(ReportingParseRunner(Type))

  def parseType(desc: String): DescriptorType = {
    val parsingResult = TypeParser.get.run(desc)
    parsingResult.result.getOrElse {
      throw new ParsingException(
        "Invalid :\n" + ErrorUtils.printParseErrors(parsingResult)
      )
    }
  }

  protected val Top: Rule1[Descriptor] = rule("Top") {
    "(" ~ zeroOrMore(Type) ~ ")" ~ Type
  } ~~> { (params, ret) => Descriptor(params, ret) }

  private lazy val Type: Rule1[DescriptorType] = rule("Type") {
    Class | Primitive | Array
  }

  private lazy val Array: Rule1[DescriptorType] = rule("Array") {
    ch('[') ~ (Class | Primitive | Array)
  } ~~> { c => ArrayDescriptor(c) }

  private lazy val Class: Rule1[DescriptorType] = rule("Class") {
    "L" ~ Package ~ Name ~ ";"
  } ~~> { (p, n) => ClassName(p, n) }

  private lazy val Package: Rule1[PackageName] = rule("Package") {
    zeroOrMore(oneOrMore("a" - "z" | "A" - "Z" | "_" | "0" - "9").save ~ "/")
  } ~~> { PackageName.apply }

  private lazy val Name: Rule1[String] = rule("Name") {
    oneOrMore(noneOf(";/()"))
  } save

  private lazy val Primitive: Rule1[DescriptorType] = rule("Primitive") {
    Boolean | Byte | Char | Short | Int | Long | Float | Double | Void
  }

  private lazy val Boolean: Rule1[ClassName] = rule { ch('Z') as PrimitiveBoolean }
  private lazy val Byte: Rule1[ClassName] = rule { ch('B') as PrimitiveByte }
  private lazy val Char: Rule1[ClassName] = rule { ch('C') as PrimitiveChar }
  private lazy val Short: Rule1[ClassName] = rule { ch('S') as PrimitiveShort }
  private lazy val Int: Rule1[ClassName] = rule { ch('I') as PrimitiveInt }
  private lazy val Long: Rule1[ClassName] = rule { ch('J') as PrimitiveLong }
  private lazy val Float: Rule1[ClassName] = rule { ch('F') as PrimitiveFloat }
  private lazy val Double: Rule1[ClassName] = rule { ch('D') as PrimitiveDouble }
  private lazy val Void: Rule1[ClassName] = rule { ch('V') as PrimitiveVoid }

}

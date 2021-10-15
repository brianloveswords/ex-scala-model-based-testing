package main

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop._

val KEY_SPACE: Int = 20

enum Op:
  case Insert(k: Int, v: Int)
  case Get(k: Int)

given Arbitrary[Op] = Arbitrary {
  val insert = for
    k <- Gen.choose(0, KEY_SPACE)
    v <- Gen.posNum[Int]
  yield Op.Insert(k, v)

  val get =
    for k <- Gen.choose(0, KEY_SPACE)
    yield Op.Get(k)

  Gen.oneOf(insert, get)
}

class TreeTest extends CommonSuite:
  property("implementation matches model") {
    forAll { (ops: Seq[Op]) =>
      var impl = Tree[Int, Int]()
      var model = Map[Int, Int]()

      ops.foreach {
        case Op.Insert(k, v) =>
          impl = impl.updated(k, v)
          model = model.updated(k, v)

        case Op.Get(k) =>
          assert(impl.get(k) == model.get(k))
      }
    }
  }

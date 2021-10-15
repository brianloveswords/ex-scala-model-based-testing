package main

import cats.implicits.*
import cats.kernel.Eq
import munit.CatsEffectSuite
import munit.Location
import munit.ScalaCheckEffectSuite
import munit.internal.console.StackTraces
import munit.internal.difflib.ComparisonFailExceptionHandler
import munit.internal.difflib.Diffs

trait CommonSuite extends CatsEffectSuite with ScalaCheckEffectSuite:
  // NOTE(brianloveswords): stolen form munit innards so I could override
  // equality operator with Eq.eqv
  private def munitComparisonHandler(
      actualObtained: Any,
      actualExpected: Any
  ): ComparisonFailExceptionHandler =
    new ComparisonFailExceptionHandler {
      override def handle(
          message: String,
          unusedObtained: String,
          unusedExpected: String,
          loc: Location
      ): Nothing = failComparison(message, actualObtained, actualExpected)(loc)
    }

  def assertEq[A: Eq, B: Eq](
      obtained: A,
      expected: B,
      clue: => Any = "values are not the same"
  )(implicit loc: Location, ev: B <:< A): Unit =
    StackTraces.dropInside {
      if (!(obtained === expected))
        Diffs.assertNoDiff(
          munitPrint(obtained),
          munitPrint(expected),
          munitComparisonHandler(obtained, expected),
          munitPrint(clue),
          printObtainedAsStripMargin = false
        )
        // try with `.toString` in case `munitPrint()` produces identical formatting for both values.
        Diffs.assertNoDiff(
          obtained.toString(),
          expected.toString(),
          munitComparisonHandler(obtained, expected),
          munitPrint(clue),
          printObtainedAsStripMargin = false
        )
        failComparison(
          s"values are not equal even if they have the same `toString()`: $obtained",
          obtained,
          expected
        )
    }

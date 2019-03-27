package example

// asssuming scala 2.12+

class A {
  def doSth: Either[A.Error, String] = ???
}

object A {
  sealed trait Error extends Product with Serializable
  final case class MyError(msg: String) extends Error
}

//class B(a: A) {
//  def doSthElse: Either[B.Error, String] =
//    a.doSth.flatMap(doSubComputation) // doesn't compile :( A.Error and B.Error are not related :(
//
//  private def doSubComputation(s: String): Either[B.Error, String] = ???
//}
//
//object B {
//  sealed trait Error
//  case object AnotherError extends Error
//}

import cats._
import cats.implicits._

class B(a: A) {
  def doSthElse: Either[B.Error, String] =
    a.doSth
      .leftMap(B.AError(_)) // I have to wrap all A errors as B.Error
      .flatMap(doSubComputation)

  private def doSubComputation(s: String): Either[B.Error, String] = ???
}

object B {
  sealed trait Error extends Product with Serializable
  case object AnotherError extends Error
  final case class AError(error: A.Error) extends Error // I need wrapper class
}

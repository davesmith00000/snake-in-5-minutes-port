import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.18.0`, millindigo._

object snakeIn5Minutes extends ScalaJSModule with MillIndigo {
  def scalaVersion   = "3.6.2"
  def scalaJSVersion = "1.18.1"

  val indigoVersion = "0.18.0"

  val indigoOptions: IndigoOptions =
    IndigoOptions.defaults
      .withTitle("Snake in 5 minutes - Made with Indigo")
      .withWindowSize(400, 400)
      .withBackgroundColor("black")
      .withAssetDirectory(os.RelPath.rel / "assets")
      .excludeAssets {
        case p if p.endsWith(os.RelPath.rel / ".gitkeep") => true
        case _                                            => false
      }

  val indigoGenerators: IndigoGenerators =
    IndigoGenerators.None

  def ivyDeps =
    Agg(
      ivy"io.indigoengine::indigo::$indigoVersion"
    )

}

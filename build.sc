import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.17.0`, millindigo._

object snakeIn5Minutes extends ScalaJSModule with MillIndigo {
  def scalaVersion   = "3.5.0"
  def scalaJSVersion = "1.17.0"

  def buildGame() =
    T.command {
      T {
        compile()
        fastLinkJS()
        indigoBuild()()
      }
    }

  def buildGameFull() =
    T.command {
      T {
        compile()
        fullLinkJS()
        indigoBuildFull()()
      }
    }

  def runGame() =
    T.command {
      T {
        compile()
        fastLinkJS()
        indigoRun()()
      }
    }

  def runGameFull() =
    T.command {
      T {
        compile()
        fullLinkJS()
        indigoRunFull()()
      }
    }

  val indigoVersion = "0.17.0"

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

import indigo.*
import indigo.syntax.*
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame") object Snake extends IndigoSandbox[Unit, SnakeModel]:

  val config: GameConfig          = GameConfig.default.withViewport(400, 400).withFrameRateLimit(15)
  val assets: Set[AssetType]      = Set()
  val animations: Set[Animation]  = Set()
  val fonts: Set[FontInfo]        = Set()
  val shaders: Set[ShaderProgram] = Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[SnakeModel] =
    Outcome(SnakeModel(Point(10, 10), 20, Point(15, 15), Point.zero, Nil, 5))

  def updateModel(context: Context[Unit], model: SnakeModel): GlobalEvent => Outcome[SnakeModel] =
    case KeyboardEvent.KeyDown(Key.ARROW_LEFT) =>
      Outcome(model.copy(direction = Point(-1, 0)))

    case KeyboardEvent.KeyDown(Key.ARROW_RIGHT) =>
      Outcome(model.copy(direction = Point(1, 0)))

    case KeyboardEvent.KeyDown(Key.ARROW_UP) =>
      Outcome(model.copy(direction = Point(0, -1)))

    case KeyboardEvent.KeyDown(Key.ARROW_DOWN) =>
      Outcome(model.copy(direction = Point(0, 1)))

    case FrameTick =>
      val nexthead: Point =
        val f = (z: Int) => if (z < 0) model.gridSize - 1 else if (z > model.gridSize - 1) 0 else z
        ((pt: Point) => Point(f(pt.x), f(pt.y)))(model.head + model.direction)

      val nextApple: Point =
        if model.apple == model.head then
          Point(context.frame.dice.roll(model.gridSize) - 1, context.frame.dice.roll(model.gridSize) - 1)
        else model.apple

      val nextTail =
        if (model.trail.contains(model.head)) 5 else if (model.apple == model.head) model.tail + 1 else model.tail

      val nextTrail = model.head :: model.trail.dropRight(Math.max(0, model.trail.length - model.tail))

      Outcome(model.copy(head = nexthead, apple = nextApple, tail = nextTail, trail = nextTrail))

    case _ => Outcome(model)

  def present(context: Context[Unit], model: SnakeModel): Outcome[SceneUpdateFragment] =
    val boxSize = Rectangle(1, 1, 18, 18)
    Outcome(
      SceneUpdateFragment(
        Shape.Box(boxSize, Fill.Color(RGBA.Red)).moveTo(model.apple * model.gridSize) :: model.trail
          .map(coords => Shape.Box(boxSize, Fill.Color(RGBA.fromHexString("00ff1b"))).moveTo(coords * model.gridSize))
          .toBatch
      )
    )

final case class SnakeModel(head: Point, gridSize: Int, apple: Point, direction: Point, trail: List[Point], tail: Int)

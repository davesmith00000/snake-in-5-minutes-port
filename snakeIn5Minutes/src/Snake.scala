import indigo._
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object Snake extends IndigoSandbox[Unit, SnakeModel]:

  val config: GameConfig         = GameConfig.default.withViewport(400, 400).withFrameRate(15)
  val assets: Set[AssetType]     = Set()
  val animations: Set[Animation] = Set()
  val fonts: Set[FontInfo]       = Set()
  val shaders: Set[Shader]       = Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[SnakeModel] =
    Outcome(SnakeModel(Point(10, 10), 20, Point(15, 15), Point.zero, Nil, 5))

  def updateModel(context: FrameContext[Unit], model: SnakeModel): GlobalEvent => Outcome[SnakeModel] =
    case KeyboardEvent.KeyDown(Key.LEFT_ARROW) =>
      Outcome(model.copy(velocity = Point(-1, 0)))

    case KeyboardEvent.KeyDown(Key.RIGHT_ARROW) =>
      Outcome(model.copy(velocity = Point(1, 0)))

    case KeyboardEvent.KeyDown(Key.UP_ARROW) =>
      Outcome(model.copy(velocity = Point(0, -1)))

    case KeyboardEvent.KeyDown(Key.DOWN_ARROW) =>
      Outcome(model.copy(velocity = Point(0, 1)))

    case FrameTick =>
      val nextPosition: Point =
        (
            (pt: Point) =>
              Point(
                if (pt.x < 0) model.gridSize - 1 else if (pt.x > model.gridSize - 1) 0 else pt.x,
                if (pt.y < 0) model.gridSize - 1 else if (pt.y > model.gridSize - 1) 0 else pt.y
              )
        )(model.position + model.velocity)

      val nextApple: Point =
        if model.apple == model.position then
          Point(context.dice.roll(model.gridSize) - 1, context.dice.roll(model.gridSize) - 1)
        else model.apple

      val nextTail =
        if model.trail.contains((model.position)) then 5
        else if model.apple == model.position then model.tail + 1
        else model.tail

      val nextTrail =
        model.position :: model.trail.dropRight(Math.max(0, model.trail.length - model.tail))

      Outcome(model.copy(position = nextPosition, apple = nextApple, tail = nextTail, trail = nextTrail))

    case _ =>
      Outcome(model)

  val apple       = Shape.Box(Rectangle(1, 1, 18, 18), Fill.Color(RGBA.Red))
  val tailSegment = Shape.Box(Rectangle(1, 1, 18, 18), Fill.Color(RGBA.fromHexString("00ff1b")))

  def present(context: FrameContext[Unit], model: SnakeModel): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        apple.moveTo(model.apple * model.gridSize) :: model.trail.map(coords =>
          tailSegment.moveTo(coords * model.gridSize)
        )
      )
    )

final case class SnakeModel(
    position: Point,
    gridSize: Int,
    apple: Point,
    velocity: Point,
    trail: List[Point],
    tail: Int
)

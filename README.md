# Snake In 5 Minutes (Port)

This version of Snake is a port of a version of Snake written in JavaScript in _5 minutes_, checkout the video - you've got 5 minutes right?

[https://www.youtube.com/watch?v=xGmXxpIj6vs](https://www.youtube.com/watch?v=xGmXxpIj6vs)

Not only that but the author, [Chris DeLeon](https://twitter.com/ChrisDeLeon) of [HomeTeam GameDev](https://twitter.com/HomeTeamGameDev), also kindly [posted the source code](https://pastebin.com/Z3zhb7cY) - all 69 lines of it! I've included it below for reference.

The JS below is not supposed to be good code as Chris points out in his video, it's not designed to be readable or extensible or bug free or anything like that - it's just bashing out a prototype in 5 minutes.

***But it's still bloody impressive!***

Anyway, ignoring the insane time factor, I found the terseness of it really interesting and wondered what it would look like if it was ported to [Indigo](https://indigoengine.io/), the result is this repo.

## Play the two versions now

Played with the arrow keys on a keyboard.

- Indigo version - [Play](https://davesmith00000.github.io/snake-in-5-minutes-port/index.html)
- Original JS version - [Play](https://davesmith00000.github.io/snake-in-5-minutes-port/snake-js.html)

### Input Lag

One difference you might notice between the two is that the Indigo version appears to have input lag, or conversely, the JS version feels more responsive ...and that's because it is.

In the JS version, the velocity values are updated outside of the game loop instantly on keyboard event. You can do that sort of thing with a mutable model, but do watch out for [accidental concurrency](https://www.youtube.com/watch?v=DfLvDFxcAIA).

Indigo on the other hand is only processing those events at the next frame update every 15th of a second - 66 milliseconds - a huge amount of time between pressing the key and responding! Speculatively, the solution would probably be to increase the games frame rate to 60 FPS, but throttle snake model updates to happen every 15 FPS.

## Details

| Version | Lines of code  | File size |
| ------------- | ------------- | ------------- |
| Original JS  | 69 | 1 Kb |
| Indigo  | 59 | 584 Kb (LOL!) |

Regarding the file size, in this case the comparison is silly. The JS version consists of code that does nothing other than run this Snake game, whereas the Scala / Indigo based version has an entire game engine bundled into it. A more interesting comparison is with the [old version of snake running on Indigo's website](https://indigoengine.io/snake.html), which clocks in at 675 KB. It _is_ 91 KB larger but is also a much more complete, considered, and tested version of the same game with ***29 Scala files and 1511 lines of code*** (according to [CLOC](http://cloc.sourceforge.net/)) - that's 17 times more code not including tests.

Porting it wasn't completely straight forward, the main things to do were:

1. Decoupling the view from the model - not so hard just required a bit of care.
2. Adapting the logic to an immutable functional style.
3. Some renaming and better use of types.

I did spend considerably more than five minutes porting this from JS. :-)

## Running it

If you clone the repo you can run it yourself. Requirements are: JDK, Mill, and Electron.

Run the following command from your terminal in the repo root:

```bash
./mill snakeIn5Minutes.indigoRun
```

## Original HTML/JS source code

```javascript
<canvas id="gc" width="400" height="400"></canvas>
<script>
window.onload=function() {
	canv=document.getElementById("gc");
	ctx=canv.getContext("2d");
	document.addEventListener("keydown",keyPush);
	setInterval(game,1000/15);
}
px=py=10;
gs=tc=20;
ax=ay=15;
xv=yv=0;
trail=[];
tail = 5;
function game() {
	px+=xv;
	py+=yv;
	if(px<0) {
		px= tc-1;
	}
	if(px>tc-1) {
		px= 0;
	}
	if(py<0) {
		py= tc-1;
	}
	if(py>tc-1) {
		py= 0;
	}
	ctx.fillStyle="black";
	ctx.fillRect(0,0,canv.width,canv.height);

	ctx.fillStyle="lime";
	for(var i=0;i<trail.length;i++) {
		ctx.fillRect(trail[i].x*gs,trail[i].y*gs,gs-2,gs-2);
		if(trail[i].x==px && trail[i].y==py) {
			tail = 5;
		}
	}
	trail.push({x:px,y:py});
	while(trail.length>tail) {
	trail.shift();
	}

	if(ax==px && ay==py) {
		tail++;
		ax=Math.floor(Math.random()*tc);
		ay=Math.floor(Math.random()*tc);
	}
	ctx.fillStyle="red";
	ctx.fillRect(ax*gs,ay*gs,gs-2,gs-2);
}
function keyPush(evt) {
	switch(evt.keyCode) {
		case 37:
			xv=-1;yv=0;
			break;
		case 38:
			xv=0;yv=-1;
			break;
		case 39:
			xv=1;yv=0;
			break;
		case 40:
			xv=0;yv=1;
			break;
	}
}
</script>
```

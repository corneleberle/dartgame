import 'dart:html';
import 'dart:async';
import 'dart:convert';
import 'dart:math';
import 'messages.dart';


const String SHOT_COLOR = "orange";
const String SURFACE_COLOR = "black";
const String GROUND_COLOR = "lightgrey";
const String CANNON_COLOR = "red";
const String HIT_COLOR = "yellow";

const num GRAVITY_FACTOR = 0.2;
const num POWER_FACTOR = 1;
const num WIND_FACTOR = 0.1;
const num HIT_TOLERANCE = 0.03;

final Element angleValue = querySelector("#angleValue");
final Element powerValue = querySelector("#powerValue");
final InputElement angleSlider = querySelector("#angleSlider");
final InputElement angleMinus = querySelector("#angleMinus");
final InputElement anglePlus = querySelector("#anglePlus");
final InputElement powerSlider = querySelector("#powerSlider");
final InputElement powerMinus = querySelector("#powerMinus");
final InputElement powerPlus = querySelector("#powerPlus");
final InputElement triggerButton = querySelector("#trigger");
final InputElement myNameText = querySelector("#myName");
final InputElement connectButton = querySelector("#connect");
final ImageElement windsockImage = querySelector("#windsock");

//final CanvasRenderingContext2D context = (querySelector("#canvas") as CanvasElement).context2D;
final CanvasElement masterCanvas = querySelector("#canvas") as CanvasElement;
final int CANVAS_WIDTH = masterCanvas.width;
final int CANVAS_HEIGHT = masterCanvas.height;

final int SCALE_X = CANVAS_WIDTH;
final int SCALE_Y = CANVAS_HEIGHT;
final int OFFSET_X = 0;
final int OFFSET_Y = CANVAS_HEIGHT;

// Different layers to draw on.
// For performance reasons the landscape shall only be drawn once, whereas cannon, fligth path, etc. are redrawn very often
final CanvasElement cannonLeftCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);
final CanvasElement cannonRightCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);
final CanvasElement leftFlightpathCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);
final CanvasElement rightFlightpathCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);
final CanvasElement landscapeCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);
final CanvasElement windsockCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);
final CanvasElement endCanvas = new CanvasElement(width: CANVAS_WIDTH, height: CANVAS_HEIGHT);

WebSocket webSocket = null;
List<num> landscape = new List(1000);

final myCannon = new Cannon();
final enemyCannon = new Cannon();
Cannon leftCannon;
Cannon rightCannon;
num wind = 0.0;

void main() {
  angleSlider.onChange.listen((e) => updateCannon(myCannon));
  angleMinus.onClick.listen((e) => updateSlider(angleSlider, -1));
  anglePlus.onClick.listen((e) => updateSlider(angleSlider, 1));
  powerSlider.onChange.listen((e) => updateCannon(myCannon));
  powerMinus.onClick.listen((e) => updateSlider(powerSlider, -1));
  powerPlus.onClick.listen((e) => updateSlider(powerSlider, 1));
  triggerButton.onClick.listen((e) => sendShotRequest(myCannon));
  connectButton.onClick.listen(connect);
}

void updateSlider(InputElement slider, num delta) {
  slider.valueAsNumber += delta;
  slider.maxLength = 1000;
  updateCannon(myCannon);
}

void updateCannon(Cannon cannon) {
  cannon.angle = PI / 2 - (angleSlider.valueAsNumber / 180 * PI); 
  cannon.power = powerSlider.valueAsNumber / 1000;
  
  num angleDegree = cannon.angle / PI * 180;
  if (cannon == rightCannon) {
    angleDegree = 180 - angleDegree;
  }
  angleValue.text = angleDegree.toStringAsFixed(0) + "°";
  powerValue.text = powerSlider.value;

  drawCannon(cannon);
}


void drawCannon(Cannon cannon) {
  CanvasElement tempCanvas;
  if (cannon == leftCannon) {
    tempCanvas = cannonLeftCanvas;
  } else {
    tempCanvas = cannonRightCanvas;
  }
  tempCanvas.context2D
      ..clearRect(0, 0, tempCanvas.width, tempCanvas.height);
  tempCanvas.context2D
      ..beginPath()
      ..lineWidth = 5
      ..strokeStyle = CANNON_COLOR
      ..arc(scaleX(cannon.pos.x), scaleY(cannon.pos.y), 5, 0, PI, true)
      ..closePath()
      ..stroke()
      ..fillStyle = CANNON_COLOR
      ..font = "18px Arial"
      ..textAlign = "center"
      ..fillText(cannon.remainingShots.toString(), scaleX(cannon.pos.x), scaleY(cannon.pos.y) + 20);
  tempCanvas.context2D
      ..save()
      ..lineWidth = 5
      ..fillStyle = CANNON_COLOR
      ..strokeStyle = CANNON_COLOR
      ..translate(scaleX(cannon.pos.x), scaleY(cannon.pos.y))
      ..rotate(-cannon.angle)
      ..beginPath()
      ..moveTo(0, 0)
      ..lineTo(scaleX(1 + cannon.power)/20, 0)
      ..closePath()
      ..stroke()
      ..restore();

  redrawCanvas();
}

void drawShotCurve(Cannon cannon, CanvasElement canvas) {
  num time = 0;
  num x = 0;
  num y = 0;
  bool flying = true;
    
  canvas.context2D
      ..clearRect(0, 0, canvas.width, canvas.height);
  
  num i = 0;
  do {
    time = i * 0.001;
    
    // Normalized position
    x = cannon.pos.x + (POWER_FACTOR * cannon.power * time * cos(cannon.angle) + WIND_FACTOR * wind * time);
    y = cannon.pos.y + (POWER_FACTOR * cannon.power * time * sin(cannon.angle) - GRAVITY_FACTOR * time * time) * SCALE_X / SCALE_Y;
    
    if (x<=0 || x>=1 || y<0) {
      flying = false;
    } else if (hitGround(x,y)) {
      flying = false;
      drawCircle(canvas, scaleX(x), scaleY(y), HIT_COLOR, CANVAS_WIDTH * HIT_TOLERANCE);

      if ((x - myCannon.pos.x).abs() < HIT_TOLERANCE) {
        disableControls();
        drawEnd("Loser");
        triggerButton.disabled = true;
      } else if ((x - enemyCannon.pos.x).abs() < HIT_TOLERANCE) {
        disableControls();
        drawEnd("Winner");
      } 
        
    }
    
    if (i % 25 == 0) {
      drawCircle(canvas, scaleX(x), scaleY(y), SHOT_COLOR, 1);
    }
    
    i += 1;
  } while (flying);
  
  redrawCanvas();
}

void disableControls() {
  angleMinus.disabled = true;
  angleSlider.disabled = true;
  anglePlus.disabled = true;
  
  powerMinus.disabled = true;
  powerSlider.disabled = true;
  powerPlus.disabled = true;
  
  triggerButton.disabled = true;
}

void drawEnd(String text) {
  int rectWith = 300;
  int rectHeight = 100;
  
  endCanvas.context2D
    ..beginPath()
    ..rect(CANVAS_WIDTH / 2 - rectWith / 2, CANVAS_HEIGHT / 2 - rectHeight / 2, rectWith, rectHeight)
    ..fillStyle = "lightgrey"
    ..fill()
    ..lineWidth = 5
    ..strokeStyle = "black"
    ..stroke()
    ..fillStyle = "black"
    ..font = "bold 72px Arial"
    ..textAlign = "center"
    ..fillText(text, CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 25);
}

void sendShotRequest(Cannon cannon) {
  ShotRequestMessage message = new ShotRequestMessage(cannon.angle, cannon.power);
  String payload = message.toJson();
  webSocket.sendString(payload);
}

/// Draw a small circle representing the bomb.
void drawCircle(CanvasElement canvas, num x, num y, String color, num radius) {
  canvas.context2D
      ..beginPath()
      ..lineWidth = 2
      ..fillStyle = color
      ..strokeStyle = color
      ..arc(x, y, radius, 0, 2*PI, false)
      ..fill()
      ..closePath()
      ..stroke();
}

void drawLandscape(List<num> mapProfile) {
  landscapeCanvas.context2D
      ..beginPath()
      ..lineWidth = 2
      ..fillStyle = GROUND_COLOR
      ..strokeStyle = SURFACE_COLOR
      ..moveTo(0, scaleY(mapProfile.elementAt(0)));
  
  for(int i = 1; i < mapProfile.length; i++){
    landscapeCanvas.context2D.lineTo(scaleX(1/mapProfile.length * i), scaleY(mapProfile.elementAt(i)));
  }
  
  //finish
  landscapeCanvas.context2D
      ..lineTo(scaleX(1.0), scaleY(mapProfile.last))
      ..lineTo(scaleX(1.0), scaleY(0.0))
      ..lineTo(scaleX(0.0), scaleY(0.0))
      ..fill()
      ..closePath()
      ..stroke();
}

void drawWindsock(num force) {
  final CanvasElement tempCanvas = new CanvasElement(width: 200, height: 200);
  tempCanvas.context2D
      ..translate(100, 100)
      ..rotate(-force * PI / 2)
      ..drawImage(windsockImage, -windsockImage.width/2, 0);

  windsockCanvas.context2D
      ..drawImage(tempCanvas, 0, -90);
}

void redrawCanvas() {
  masterCanvas.context2D
      ..clearRect(0, 0, masterCanvas.width, masterCanvas.height)
      ..drawImage(landscapeCanvas, 0, 0)
      ..drawImage(cannonLeftCanvas, 0, 0)
      ..drawImage(cannonRightCanvas, 0, 0)
      ..drawImage(windsockCanvas, 0, 0)
      ..drawImage(leftFlightpathCanvas, 0, 0)
      ..drawImage(rightFlightpathCanvas, 0, 0)
      ..drawImage(endCanvas, 0, 0);
}

bool hitGround(num x, num y) {
  int position = (x * landscape.length).truncate();
  return y < landscape.elementAt(position);
}

num scaleX(num x) {
  return OFFSET_X + SCALE_X * x;
}

num scaleY(num y) {
  return OFFSET_Y - SCALE_Y * y;
}


void printEnemyName(String name) {
  final Element enemyNameText = querySelector("#enemyName");
  enemyNameText.text = "vs. " + name;
}


void outputMsg(String msg) {
  var output = querySelector('#output');
  var text = msg;
  if (!output.text.isEmpty) {
    text = "${output.text}\n- - - - - -\n${text}";
  }
  output.text = text;
}

void connect(MouseEvent event) {
  if (myNameText.value.isEmpty) {
    window.alert("Please enter your name.");
    return;
  }
  
  webSocket = new WebSocket('ws://${Uri.base.host}:8080/dartgame/controller');  
  
  webSocket.onOpen.listen((e) {
    ConnectMessage connectMessage = new ConnectMessage(myNameText.value);
    String payload = connectMessage.toJson();
    webSocket.sendString(payload);
    
    // Disable connect button and name text input element
    connectButton.disabled = true;
    myNameText.disabled = true;
  });
  
  webSocket.onMessage.listen((MessageEvent e) {
    Map message = JSON.decode(e.data);
    if(message != null && message.isNotEmpty){
      if(message["messageType"] == MessageTypesEnum.MESSAGE_TYPE_INIT){
        List<double> landscape2 = message["landscape"];
        landscape = landscape2;
        
        String enemyName;
        wind = message["wind"] / 1000;
        Point cannonLeftPos = getCannonPos(landscape, message["canonLeftX"]);
        Point cannonRightPos = getCannonPos(landscape, message["canonRightX"]);
        
        if (message["playerType"] == "LEFT") {
          enemyName = message["playerNames"]["RIGHT"];
          myCannon.pos = cannonLeftPos;
          myCannon.remainingShots = message["remainingShots"]["LEFT"];
          enemyCannon.pos = cannonRightPos;
          enemyCannon.remainingShots = message["remainingShots"]["RIGHT"];
          leftCannon = myCannon;
          rightCannon = enemyCannon;
        } else {
          enemyName = message["playerNames"]["LEFT"];
          myCannon.pos = cannonRightPos;
          myCannon.remainingShots = message["remainingShots"]["RIGHT"];
          enemyCannon.pos = cannonLeftPos;
          enemyCannon.remainingShots = message["remainingShots"]["LEFT"];
          leftCannon = enemyCannon;
          rightCannon = myCannon;
        }
        printEnemyName(enemyName);
        drawLandscape(landscape);
        drawWindsock(wind);
        updateCannon(myCannon);
        updateCannon(enemyCannon);
      }
      
      if(message["messageType"] == MessageTypesEnum.MESSAGE_TYPE_SHOT){
        double angle = message["angle"];
        double power = message["power"];
        
        if (message["shooter"] == "LEFT") {
          leftCannon.angle = angle;
          leftCannon.power = power;
          leftCannon.remainingShots = message["remainingShots"]["LEFT"];
          drawCannon(leftCannon);
          drawShotCurve(leftCannon, leftFlightpathCanvas);
        } else {
          rightCannon.angle = angle;
          rightCannon.power = power;
          rightCannon.remainingShots = message["remainingShots"]["RIGHT"];
          drawCannon(rightCannon);
          drawShotCurve(rightCannon, rightFlightpathCanvas);
        }
      }
      if(message["messageType"] == MessageTypesEnum.MESSAGE_TYPE_CONNECT){
        outputMsg("Not Supported by Client!");
      }
    }
    outputMsg(e.data);
  });
}


class Cannon {
  Point pos = new Point(0,0);
  double angle = 0.0;
  double power = 0.0;
  num remainingShots = 0;
  String player;
  
  Cannon () { }
}

Point getCannonPos(List<double> landscape, int posX) {
  double x = posX / landscape.length;
  double y = landscape[posX];
  return new Point(x, y);
}


// === Rubbish ================================================================

void fillLandscapeWithTestData(List<num> landscape) {
  num y = 0.0;
  final int max = landscape.length;
  for (int i=0; i < max; i++) {
    num x = i / max;
    landscape[i] = 0.1 + 0.4*sin(x*PI) + 0.2*x;
  }
}

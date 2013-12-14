import 'dart:html';
import 'dart:async';
import 'dart:convert';
import 'dart:math';
import 'messages.dart';


const String SHOT_COLOR = "orange";
const String GROUND_COLOR = "grey";
const String CANNON_COLOR = "red";
const int SEED_RADIUS = 1;
const num TAU = PI * 2;

const num WIND_FACTOR = 0.1;
const num GRAVITY_FACTOR = 0.5;

final InputElement angleSlider = querySelector("#angle");
final InputElement powerSlider = querySelector("#power");
final InputElement triggerButton = querySelector("#trigger");
final InputElement connectButton = querySelector("#connect");
final ImageElement windsockImage = querySelector("#windsock");

final CanvasRenderingContext2D context =
  (querySelector("#canvas") as CanvasElement).context2D;
final int CANVAS_WIDTH = context.canvas.width;
final int CANVAS_HEIGHT = context.canvas.height;

final int SCALE_X = CANVAS_WIDTH;
final int SCALE_Y = CANVAS_HEIGHT;
final int OFFSET_X = 0;
final int OFFSET_Y = CANVAS_HEIGHT;

WebSocket webSocket = null;
List<num> landscape = new List(1000);


final myCannon = new Cannon();
final enemyCannon = new Cannon();
Cannon leftCannon;
Cannon rightCannon;
num wind = 0.0;

void main() {
  angleSlider.onChange.listen((e) => updateCannon(myCannon));
  powerSlider.onChange.listen((e) => updateCannon(myCannon));
  triggerButton.onClick.listen((e) => sendShotRequest(myCannon));
  connectButton.onClick.listen(connect);  
}


void fillLandscapeWithTestData(List<num> landscape) {
  num y = 0.0;
  final int max = landscape.length;
  for (int i=0; i < max; i++) {
    num x = i / max;
    landscape[i] = 0.1 + 0.4*sin(x*PI) + 0.2*x;
  }
}


void updateCannon(Cannon cannon) {
  cannon.angle = int.parse(angleSlider.value) / 1000 * PI; 
  cannon.power = int.parse(powerSlider.value) / 1000;
  drawCannon(cannon);
}


void drawCannon(Cannon cannon) {
  double deltaX = 0.1 * (1 + cannon.power) * cos(cannon.angle);
  double deltaY = 0.1 * (1 + cannon.power) * sin(cannon.angle);
  context..beginPath()
         ..lineWidth = 2
         ..fillStyle = CANNON_COLOR
         ..strokeStyle = CANNON_COLOR
         ..moveTo(scaleX(cannon.pos.x), scaleY(cannon.pos.y))
         ..lineTo(scaleX(cannon.pos.x + deltaX), scaleY(cannon.pos.y + deltaY))
         ..closePath()
         ..stroke();
}

void drawShotCurve(Cannon cannon) {
  num time = 0;
  num x = 0;
  num y = 0;
  bool flying=true;
  
  while (flying) {
    // Normalized position
    x = cannon.pos.x + (2 * cannon.power * time * cos(cannon.angle) + WIND_FACTOR * wind * time);
    y = cannon.pos.y + (2 * cannon.power * time * sin(cannon.angle) - GRAVITY_FACTOR * time * time);
    
    drawLine(scaleX(x), scaleY(y), SHOT_COLOR);

    if (x<=0 || x>=1 || y<0 || hitGround(x,y)) {
      flying = false;
    }
    time += 0.001;
  }
}

void sendShotRequest(Cannon cannon) {
  ShotRequestMessage message = new ShotRequestMessage(cannon.angle, cannon.power);
  String payload = message.toJson();
  webSocket.sendString(payload);
}

/// Draw a small circle representing the bomb.
void drawLine(num x, num y, String color) {
  context..beginPath()
         ..lineWidth = 2
         ..fillStyle = color
         ..strokeStyle = color
         ..arc(x, y, SEED_RADIUS, 0, TAU, false)
         ..fill()
         ..closePath()
         ..stroke();
}

void drawMapProfile(List<num> mapProfile) {
  context..beginPath()
    ..lineWidth = 2
    ..fillStyle = GROUND_COLOR
    ..strokeStyle = GROUND_COLOR
    ..moveTo(0, scaleY(mapProfile.elementAt(0)));
  
  for(int i = 0; i < mapProfile.length; i++){
    context.lineTo(scaleX(1/mapProfile.length * i), scaleY(mapProfile.elementAt(i)));
  }
  
  //finish
  context..lineTo(scaleX(1.0), scaleY(mapProfile.last))
         ..lineTo(scaleX(1.0), scaleY(0.0))
         ..lineTo(scaleX(0.0), scaleY(0.0))
         ..fill()
         ..closePath()
         ..stroke();
}

void drawWindsock(num force) {
  final CanvasElement tempCanvas = new CanvasElement(width: 200, height: 200);
  CanvasRenderingContext2D tempContext = tempCanvas.context2D;
  tempContext..translate(100, 100)
             ..rotate(-force * PI / 2)
             ..drawImage(windsockImage, -windsockImage.width/2, 0);
  context..drawImage(tempCanvas, 0, -90);
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




void outputMsg(String msg) {
  var output = querySelector('#output');
  var text = msg;
  if (!output.text.isEmpty) {
    text = "${output.text}\n${text}";
  }
  output.text = text;
}

void connect(MouseEvent event) {
  //webSocket = new WebSocket('ws://localhost:8080/dartgame/echo');
  webSocket = new WebSocket('ws://localhost:8080/dartgame/controller');
  
  
  webSocket.onOpen.listen((e) {
    ConnectMessage connectMessage = new ConnectMessage("Spieler 1");
    String payload = connectMessage.toJson();
    webSocket.sendString(payload);
  });
  
  webSocket.onMessage.listen((MessageEvent e) {
    Map message = JSON.decode(e.data);
    if(message != null && message.isNotEmpty){
      if(message["messageType"] == MessageTypesEnum.MESSAGE_TYPE_INIT){
        List<double> landscape2 = message["landscape"];
        landscape = landscape2;
        
        wind = message["wind"] / 1000;
        Point cannonLeftPos = getCannonPos(landscape, message["canonLeftX"]);
        Point cannonRightPos = getCannonPos(landscape, message["canonRightX"]);
        
        if (message["playerType"] == "LEFT") {
          myCannon.pos = cannonLeftPos;
          enemyCannon.pos = cannonRightPos;
          leftCannon = myCannon;
          rightCannon = enemyCannon;
        } else {
          myCannon.pos = cannonRightPos;
          enemyCannon.pos = cannonLeftPos;
          leftCannon = enemyCannon;
          rightCannon = myCannon;
        }
        
        context.clearRect(0, 0, SCALE_X, SCALE_Y);
        drawMapProfile(landscape);
        drawWindsock(wind);
        updateCannon(myCannon);
      }
      
      if(message["messageType"] == MessageTypesEnum.MESSAGE_TYPE_SHOT){
        double angle = message["angle"];
        double power = message["power"];
        
        if (message["shooter"] == "LEFT") {
          drawShotCurve(leftCannon);
        } else {
          drawShotCurve(rightCannon);
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
  
  Cannon () { }
}

Point getCannonPos(List<double> landscape, int posX) {
  double x = posX / landscape.length;
  double y = landscape[posX];
  return new Point(x, y);
}

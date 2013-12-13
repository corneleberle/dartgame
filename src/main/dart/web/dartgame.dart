import 'dart:html';
import 'dart:async';
import 'dart:convert';
import 'dart:math';

WebSocket webSocket = null;



const String SHOT_COLOR = "orange";
const String GROUND_COLOR = "grey";
const int SEED_RADIUS = 1;
const num TAU = PI * 2;
const num GRAVITY = 1;

final InputElement windSlider = querySelector("#wind");
final InputElement angleSlider = querySelector("#angle");
final InputElement powerSlider = querySelector("#power");
final InputElement triggerButton = querySelector("#trigger");
final InputElement input = querySelector("#connect");

final Element debugText = querySelector("#debug");

final CanvasRenderingContext2D context =
  (querySelector("#canvas") as CanvasElement).context2D;
final int CANVAS_WIDTH = context.canvas.width;
final int CANVAS_HEIGHT = context.canvas.height;

final int SCALE_X = CANVAS_WIDTH;
final int SCALE_Y = CANVAS_HEIGHT;
final int OFFSET_X = 0;
final int OFFSET_Y = CANVAS_HEIGHT;

List<double> landscape = new List(1000);




void main() {
  final myCannon = new Cannon();

  //windSlider.onChange.listen((e) => drawWind());
  angleSlider.onChange.listen((e) => updateCannon(myCannon));
  powerSlider.onChange.listen((e) => updateCannon(myCannon));
  triggerButton.onClick.listen((e) => drawShotCurve(myCannon));
  
  // connect
  input.onClick.listen(connect);
  
  // init
  myCannon.pos = new Point(0.13, 0.3);
  //fillLandscape(landscape);

}




void fillLandscape(List<double> mapProfile) {
  double y = 0.0;
  final int max = mapProfile.length;
  for (int i=0; i < max; i++) {
    double x = i / max;
    mapProfile[i] = 0.1 + 0.4*sin(x*PI) + 0.2*x;
  }
}


void updateCannon(Cannon cannon) {
  cannon.angle = int.parse(angleSlider.value) / 1000 * PI; 
  cannon.power = int.parse(powerSlider.value) / 1000; 
}

void drawShotCurve(Cannon cannon) {
  num time = 0;
  num x = 0;
  num y = 0;
  bool flying=true;
  
  num wind = int.parse(windSlider.value) / 1000; 
  num angle = int.parse(angleSlider.value) / 1000 * PI; 
  num power = int.parse(powerSlider.value) / 1000; 
      

  
  while (flying) {
    // Normalized position
    x = cannon.pos.x + (2*power*time * cos(angle) + wind*time);
    y = cannon.pos.y + (2*power*time * sin(angle) - GRAVITY/2*time*time);
    
    drawLine(scaleX(x), scaleY(y), SHOT_COLOR);

    if (x<=0 || x>=1 || y<0 || hitGround(x,y)) {
      flying = false;
    }
    time += 0.001;
  }
  debugText.text = "${time}%:  x=${x} / y=${y}";
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

void drawMapProfile(List<double> mapProfile) {
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

bool hitGround(num x, num y) {
  int position = (x * landscape.length).truncate();
  return y <= landscape.elementAt(position);
}

double scaleX(double x) {
  return OFFSET_X + SCALE_X*(x);
}
double scaleY(double y) {
  return OFFSET_Y - SCALE_Y*(y);
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
    ConnectMessage connectMessage = new ConnectMessage()
      ..sent = new DateTime.now()
      ..sender = "Spieler 1"
      ..messageType = AbstractMessage.MESSAGE_TYPE_CONNECT
      ..name = "Spieler 1";
    
    String payload = connectMessage.toJson();
    webSocket.sendString(payload);
  });
  
  webSocket.onMessage.listen((MessageEvent e) {
    Map data = JSON.decode(e.data);
    List<double> landscape2 = data["landscape"];
    landscape = landscape2;
    
    context.clearRect(0, 0, SCALE_X, SCALE_Y);
    drawMapProfile(landscape);

  });
}


abstract class AbstractMessage {
  static const MESSAGE_TYPE_CONNECT = "CONNECT";
  
  DateTime sent;
  
  String sender;
  
  String messageType;
}

class ConnectMessage extends AbstractMessage {
  String name;
  
  String toJson() {
    var mapData = new Map();
    mapData["sent"] = sent.toString();
    mapData["sender"] = sender;
    mapData["messageType"] = messageType;
    mapData["name"] = name;

    return JSON.encode(mapData);
  }
}




class Cannon {
  Point pos;
  double angle;
  double power;
  
  Cannon () { }
}



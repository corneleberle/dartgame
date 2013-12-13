import 'dart:html';
import 'dart:async';
import 'dart:convert';

void main() {
  InputElement input = querySelector("#connect");
  input.onClick.listen(connect);
  
  querySelector("#send")
    ..onClick.listen(send);
}

WebSocket webSocket = null;

outputMsg(String msg) {
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
    outputMsg('Received message: ${e.data}');
  });
}

void send(MouseEvent event) {
  InputElement text = querySelector('#text');
  webSocket.send(text.value);
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



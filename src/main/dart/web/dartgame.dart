import 'dart:html';
import 'dart:async';

void main() {
  querySelector("#connect")
    ..onClick.listen(connect);
  
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
  webSocket = new WebSocket('ws://localhost:8080/dartgame/chat');
  
  webSocket.onOpen.listen((e) {
    outputMsg("connected");
  });
  
  webSocket.onMessage.listen((MessageEvent e) {
    outputMsg('Received message: ${e.data}');
  });
}

void send(MouseEvent event) {
  InputElement text = querySelector('#text');
  webSocket.send(text.value);
}



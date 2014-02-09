import 'dart:convert';


class MessageTypesEnum{
  static const MESSAGE_TYPE_CONNECT = "CONNECT";
  static const MESSAGE_TYPE_INIT = "INIT";
  static const MESSAGE_TYPE_SHOT_REQUEST = "SHOT_REQUEST";
  static const MESSAGE_TYPE_SHOT = "SHOT";
}

class PlayerTypeEnum{
  static const LEFT = "LEFT";
  static const RIGHT = "RIGHT";
}

abstract class AbstractMessage {
  String messageType;
  DateTime sent;
  
  AbstractMessage(this.sent, this.messageType);
  
  String toJson() {
    return JSON.encode(getBaseMap());
  }
  
  Map getBaseMap() {
    Map mapData = new Map();
    mapData["messageType"] = messageType;
    mapData["sent"] = sent.toString();
    return mapData;    
  } 
  
  String toString() {
    return this.messageType + ":\n" + this.toJson();
  }
}

class ConnectMessage extends AbstractMessage {
  String name;
  
  ConnectMessage(this.name) : super(new DateTime.now(), MessageTypesEnum.MESSAGE_TYPE_CONNECT);
  
  ConnectMessage.custom(this.name, DateTime sent, String messageType) : super(sent, messageType);
  
  Map getBaseMap() {
    Map mapData = super.getBaseMap();
    mapData["name"] = name;
    return mapData; 
  }
}

class InitMessage extends AbstractMessage {
  double duration;
  int numberOfBombs;
  String playerType;
  List<double> landscape;
  
  InitMessage(this.duration, this.numberOfBombs, this.playerType, this.landscape) : super(new DateTime.now(), MessageTypesEnum.MESSAGE_TYPE_INIT);
  
  InitMessage.custom(this.duration, this.numberOfBombs, this.playerType, this.landscape, DateTime sent, String messageType) : super(sent, messageType);
  
  Map getBaseMap() {
    Map mapData = super.getBaseMap();
    mapData["duration"] = duration;
    mapData["numberOfBombs"] = numberOfBombs;
    mapData["playerType"] = playerType;
    mapData["landscape"] = landscape;
    return mapData; 
  }
}

class ShotRequestMessage extends AbstractMessage {
  double angle;
  double power;
  
  ShotRequestMessage(this.angle, this.power) : super(new DateTime.now(), MessageTypesEnum.MESSAGE_TYPE_SHOT_REQUEST);
  
  ShotRequestMessage.custom(this.angle, this.power, DateTime sent, String messageType) : super(sent, messageType);
  
  Map getBaseMap() {
    Map mapData = super.getBaseMap(); 
    mapData["angle"] = angle;
    mapData["power"] = power;
    return mapData; 
  }
}

class ShotResultMessage extends AbstractMessage {
  int shotId;
  bool strike;
  
  ShotResultMessage(this.shotId, this.strike) : super(new DateTime.now(), MessageTypesEnum.MESSAGE_TYPE_SHOT_REQUEST);
  
  ShotResultMessage.custom(this.shotId, this.strike, DateTime sent, String messageType) : super(sent, messageType);
  
  Map getBaseMap() {
    Map mapData = super.getBaseMap();
    mapData["shotId"] = shotId;
    mapData["strike"] = strike;
    return mapData; 
  }
}


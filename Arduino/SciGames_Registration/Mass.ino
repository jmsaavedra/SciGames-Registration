
#define  MASS_SCALE   A7

float rawMass = 0;
int massLbs = 0;
int massKg = 0;

int bufferSize = 150;
int adjuster = 0;

void initMass(){
  delay(50);
  adjuster = calibrateMass(); 
}

void getMass(){
  float thisMass;
  thisMass = mass_reader();

  //send values
}

int mass_reader(){
  long r = 0;
  long test = 0;
  for(int i=0; i<bufferSize; i++){
    r = r + (analogRead(MASS_SCALE) + adjuster);
    test = test + (analogRead(MASS_SCALE));
  }
  test = test/bufferSize;
  Serial.print("debug: ");
  Serial.println(test);

  rawMass = r/bufferSize;
  rawMass = map(rawMass,0,1023,0,255);
  Serial.print("rawMass mapped to byte: ");
  Serial.println(rawMass);

  //massLbs = (0.4839*rawMass) - 216.96;
  //Serial.print("massLbs: ");
  //Serial.println(massLbs);

  massKg = (0.8527*rawMass) - 96.1;
  Serial.print("massKg: ");
  Serial.println(massKg);

  Serial.println("---------------\n");
  analogWrite(LED1_BLUE, abs(massLbs)/4);
  analogWrite(LED1_GREEN, (230-abs(massLbs))/4);

  if(massKg < 0){
    massKg = 0;
  }
  
  haveInfoToSend = true;
  numBytesToSend = 2;
  infoToSend[0] = 111;
  //infoToSend[1] = massLbs;
  infoToSend[1] = massKg;
  MASS_GO = false;
  return massLbs;
}

void massLeds(){
    long r = 0;
  long test = 0;
  for(int i=0; i<bufferSize; i++){
    r = r + (analogRead(MASS_SCALE) + adjuster);
    test = test + (analogRead(MASS_SCALE));
  }
  test = test/bufferSize;
  Serial.print("debug: ");
  Serial.println(test);

  rawMass = r/bufferSize;
  rawMass = map(rawMass,0,1023,0,255);
  Serial.print("rawMass mapped to byte: ");
  Serial.println(rawMass);

  //massLbs = (0.4839*rawMass) - 216.96;
  //Serial.print("massLbs: ");
  //Serial.println(massLbs);

  massKg = (0.8527*rawMass) - 96.1;
  Serial.print("massKg: ");
  Serial.println(massKg);

  Serial.println("---------------\n");
  int b = 255-(massKg*3);
  if(b<1) b=0;
  int g = massKg*3;
  if(g>250) g=250;
  analogWrite(LED1_BLUE,  b);
  analogWrite(LED1_GREEN, g);

  
}

int calibrateMass(){
  Serial.print("CALIBRATING: ");
  digitalWrite(LED1_RED, LOW);
  int magicNumber = 442;
  int thisAdjustment = 0;
  float uncalibMeasure = 0;
  long r2 = 0;
  int cBuffer = 25;

  for(int j=0; j<cBuffer; j++){
    long r = 0;
    // for(int j=0; j<10; j++){
    for(int i=0; i<bufferSize; i++){
      r = r + analogRead(MASS_SCALE);
      analogWrite(LED1_RED, i);
      delay(1);
    }
    r2 = r2 + r/(bufferSize);
    Serial.print(">");
    Serial.print(r/(bufferSize));
    delay(100);
  }
  Serial.print("DONE\nr2 measure: ");
  Serial.println(r2/cBuffer); 
  uncalibMeasure = r2/cBuffer;
  Serial.print("uncalibrated measure: ");
  Serial.println(uncalibMeasure);
  if(uncalibMeasure > magicNumber){
    thisAdjustment = uncalibMeasure - magicNumber;
  } 
  else thisAdjustment = magicNumber - uncalibMeasure;
  //thisAdjustment = abs(440 - uncalibMeasure);
  Serial.print("ADJUSTER SET TO: ");
  Serial.println(thisAdjustment);
  Serial.println("*********** \n\n");
  delay(1500);
  analogWrite(LED1_RED, 0);
  analogWrite(LED1_GREEN, 80);
  return thisAdjustment;
}


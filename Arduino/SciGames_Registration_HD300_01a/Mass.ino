
float rawMass = 0;
int massKg = 0;
int finalMass = 0;

char inByte[14];
int bCount = 0; //byte count

void initMass(){
    Serial1.begin(9600);
  while (!Serial1)
    ;  
}

void getMass(){
  float thisMass;

  thisMass = mass_reader();

  //send values
}

int mass_reader(){


  haveInfoToSend = true;
  numBytesToSend = 2;
  infoToSend[0] = 111; //secret number for the app
  //infoToSend[1] = massLbs;
  infoToSend[1] = finalMass;
  MASS_GO = false;
  return finalMass;
}

void readScale(){
   int bytesRead = 0;
  
    if (Serial1.available() > 0) {
      bytesRead = Serial1.readBytesUntil(':',inByte, 14);
    }

    Serial.println();
    char thisMass[4]; //2,3,4,5
    for(int i=2; i<6; i++){
      thisMass[i-2] = inByte[i];
      //Serial.write(inByte[i]);
    }
    Serial.print("thisMass string: ");
    Serial.println(thisMass);

    finalMass = atoi(thisMass);

    Serial.print("finalMass int: ");
    Serial.println(finalMass);
    Serial.println("\n------\n");
  
}

void massLeds(){

  //  int b = 255-(massKg*3);
  //  if(b<1) b=0;
  //  int g = massKg*3;
  //  if(g>250) g=250;
  //  analogWrite(LED1_BLUE,  b);
  //  analogWrite(LED1_GREEN, g);
}



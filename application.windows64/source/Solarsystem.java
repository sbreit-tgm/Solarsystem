import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Solarsystem extends PApplet {





Planet mr, vn, er, ms, jp, st, ur, np;
PeasyCam cam;

float test = 1.0f;
boolean timeout = false;
boolean texture= false;
boolean light= false;
/**
 * Method to setup the sciene
 * It also creates the planet objects with the specifik parameters
 *
 */
public void setup() {
  
    
  cam = new PeasyCam(this,500);
  strokeWeight(0.5f);
  colorMode(HSB, 360, 100, 100);


  // https://www.geo.de/geolino/forschung-und-technik/4917-rtkl-weltraum-unser-sonnensystem
  mr = new Planet("Mercury", 0.48f, 0.57f, 10, color(130, 100, 90));
  vn = new Planet("Venus", 0.12f, 1.0f, 40, color(50, 100, 90));
  er = new Planet("Earth", 0.12f, 1.49f, 80, color(200, 100, 90));  
  ms = new Planet("Mars", 0.67f, 2.27f, 120, color(360, 100, 90));
  jp = new Planet("Jupiter", 14.29f, 7.79f, 160, color(40, 100, 90));  
  st = new Planet("Saturn", 12.05f, 14.33f, 200, color(26, 100, 90));
  ur = new Planet("Uran", 51.1f, 28, 240, color(170, 100, 90));  
  np = new Planet("Neptun", 49, 44, 280, color(180, 100, 90));
}
/**
 * This Method is called when any key is Released
 * In the variable key is saved which one is released.
 * l released lights are added
 * f released lets the planets rotate faster
 * s released lets the planets rotate slower
 * p released pauses/starts the simulation
 * esc released program is exited
 * t releases textured is added/removed
 *
 */
public void keyReleased() {
    // turns on the light
    if (key == 'l'){
      addLights();
      
    }
    // lets the planets rotate faster
    if (key == 'f'){
      test=test+test/4;
      draw();
    }
    // lets the planets rotate slower
    if (key == 's'){
      test=test-test/4;
      draw();
    }
    // Stops and starts the simulation
    if (key == 'p'){
      if (timeout== false){
         test=0;
         timeout= true;
         
      }
      else{
        test=1;
        timeout= false;
      }
      draw();
    }
    // Exits the program when esc is pressed
    if (key==27){
      exit();
    }
    if (key=='t'){
      if (texture){
        
        texture=false;
      }
      else{
        texture=true;
      }
      
    }
      
      
    
}
/**
 * Adds gneric Light to the science (for shaddow effect)
 */
public void addLights() {
  lights();
}
/**
 * Adds the specific velocity to the planets and then calls the display fkt for each planet and the sun
 * When texture shall be added the name of the jpeg is given as parameter in the display fkt.
 */
public void draw() {
  background(0);
 // addLights();
  // So that the translate doesn't have any effect on other methods
  pushMatrix();
  //translate (width/2, height/2);
  //Source https://www.geo.de/geolino/forschung-und-technik/4917-rtkl-weltraum-unser-sonnensystem
  mr.motion(test/0.88f);  
  vn.motion(test/0.108f);  
  er.motion(test/0.365f);     
  ms.motion(test/0.687f);  
  jp.motion(test/11.9f); 
  st.motion(test/29.5f);
  ur.motion(test/83.8f);    
  np.motion(test/164.8f); 
  if (texture==true){
    drawSunTexture();
    mr.display("mercury");
    vn.display("venus");
    er.display("earth");
    ms.display("mars");
    jp.display("jupiter");
    st.display("saturn");
    ur.display("uranus");
    np.display("neptun");
  }
  else{
    drawSun();
    mr.display();
    vn.display();
    er.display();
    ms.display();
    jp.display();
    st.display();
    ur.display();
    np.display();
  }
  

  popMatrix();
}

/**
 * Method draws the sun as a yellow ball with no stroke
 */
public void drawSun() {
  // No Lines across the 3D Model
  noStroke(); 
  fill(51, 100, 90);
  sphere (40);
  // x,y center 30 30 diameter width and height
  // ellipse(0,0,30,30);
}
/**
 * Draws the Sun with the texture of the loaded picture sun.jpg
 */
public void drawSunTexture(){
  noStroke();
  PImage img = loadImage("sun.jpg");
  PShape globe;
  globe=createShape(SPHERE,40);
  globe.setTexture(img);
  shape(globe);

  
  
}
  
/**
 * Class Planet that holds all methods to set draw and display a planet
 */
class Planet {
  // Atributes
  // Datatype for storing shapes. 
  PShape globe;
  float adaptedSize;    
  float ang;
  float vel;
  float x, y;
  float realRadius; //real radius of orbit
  float adaptedRadius; //adapted radius
  float realSize; //diameter
  float startRadius;
  String name;
  int colorPlanet;
  int colorOrbit;
  
 /**
  * Constructor of the class Planet
  * @param name Name of the planet (needed for the nametag)
  * @param realSize the Size of the planet
  * @param ang the angel at which it should start to rotate
  * @param color the color which the planet should get
  */
  Planet(String name, float realSize, float realRadius, float ang, int colorPlanet) {
    
    this.name = name;
    this.realSize = realSize;  
    this.realRadius = realRadius;  
    this.ang = ang;
    this.colorPlanet = colorPlanet;
    startRadius = 50;      
    colorOrbit = color(360, 0, 100);
    // Re-maps a number from one range to another. https://processing.org/reference/map_.html
    // adapted for the Orbit later
    adaptedRadius = map(realRadius, 0, 30, startRadius, 450);    
    adaptedSize = map(realSize, 0.4f, 11, 5, 20);    

    x = adaptedRadius;
    y = 0;
    noStroke();
    noFill();
    globe= createShape(SPHERE,realRadius);
  }

  /**
   * Draws the Orbit of the planet
   */
  public void drawOrbit() {
    strokeWeight(0.1f);
    stroke(colorOrbit);
    noFill();
    ellipse(0, 0, adaptedRadius*2, adaptedRadius*2);
  }  
  /**
   * Draws the Orbit and then displayes the planet
   *
   */
  public void display() {
    // So that the translate only is for this method
    pushMatrix();
    drawOrbit();

    drawTitle();

    strokeWeight(0.5f);
    stroke(0);
    fill(colorPlanet);
    // Set the center to x y to then add the sphere
    translate(x, y);
    // No Lines across the 3D Model
    noStroke(); 
    sphere(adaptedSize);
    shape(globe);
    //ellipse(x, y, adaptedSize, adaptedSize); 
    // end the Matrix so that the translate doesn't have any effekt on the other methods
    popMatrix();
  }
  /**
   * Draws the Orbit and then displayes the planet with the texture of the jpeg given as Text
   *
   */
  public void display(String text){
     noStroke();
    PImage img = loadImage(text+".jpg");
    //Datatype for storing shapes. 
    PShape globe;
    // So that the translate only is for this method
    pushMatrix();
    drawOrbit();
    // Addet the
    drawTitle();

    strokeWeight(0.5f);
    stroke(0);
    fill(colorPlanet);
    // Set the center to x y to then add the sphere
    translate(x, y);
    // No Lines across the 3D Model
    noStroke(); 
    
    //ellipse(x, y, adaptedSize, adaptedSize); 
    // end the Matrix so that the translate doesn't have any effekt on the other methods
   
    globe=createShape(SPHERE,adaptedSize);
    globe.setTexture(img);
    shape(globe);
    popMatrix();
    }
  /*
   * Calculates the x,y kordinate and the angel  with the given velocity
   *
   */
  public void motion(float vel) {
    x = cos(radians(ang)) * adaptedRadius;
    y = sin(radians(ang)) * adaptedRadius;
    ang = ang + (vel/2);
  }
  /**
   * Adds 0.2 to the ang increase the "velocity"
   */
  public void faster(){
    ang= ang+0.2f;
  }
  /**
   * Adds 0.2 to the ang decrease the "velocity"
   */
  public void slower(){
     ang= ang-0.2f;
  }
  /**
   * Adds the name of the planet next to the planet
   */
  public void drawTitle() {
    fill(360, 0, 100);
    text(name, x, y-30);
  }
 
  
}

  public void settings() {  size(900, 900, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Solarsystem" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

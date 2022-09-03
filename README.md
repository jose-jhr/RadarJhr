# RadarJhr

En este tutorial te enseñare como implementar un radar con 3 sensores de ultrasonido usando arduino y android studio.
para ello primero debemos importar nuestra libreria de bluetooth que ya vimos anteriormente en nuestro video de youtube a continuacion te anexo el enlace del codigo y el video de youtube, 



enlace github code https://github.com/jose-jhr/blueJhrLibrary
enlace video de youtube https://www.youtube.com/watch?v=ubrTw9pXkXM&t=8s

usare la  misma forma de yotube con la propiedad visible y gone con el fin de evitarme crear una Activity nueva.
una vez ya tengas implementada la configuracion de la libreria bluetooth que nos permitira conectarnos a Arduino, procederemos a importar los archivos necesarios para la implementación.

POSDATA, SI USAS JAVA TIENES QUE IMPLEMENTAR KOTLIN DEBIDO A QUE LA LIBRERIA USA KOTLIN, SIMPLEMENTE IMPORTAS KOTLIN Y DEBERIA FUNCIONAR ;)

link del video explicativo.
https://youtu.be/7joviLPG7eE

![arduinoSensores](https://user-images.githubusercontent.com/66834393/188247732-60586a49-c671-4ac9-878c-1418eb30adc5.png)

![image](https://user-images.githubusercontent.com/66834393/185570326-4a079501-8e54-43ff-9bb5-27eb23f98854.png)

 ```
id 'kotlin-android-extensions'
 ```

1) importamos la libreria RadarJhr

 ```
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
 
  ```
  	dependencies {
	        implementation 'com.github.jose-jhr:RadarJhr:1.0.1'
		    implementation 'com.github.jose-jhr:blueJhrLibrary:0.1.0'
	}
 ```
 
 libreria bluetooth implementacion.
 https://github.com/jose-jhr/blueJhrLibrary
 
![image](https://user-images.githubusercontent.com/66834393/173492533-d3c0f3e5-85bf-4b57-9890-2fd7709891af.png)
![image](https://user-images.githubusercontent.com/66834393/173492579-5f19d094-3cc4-48a6-902a-793ab19e6899.png)


2) vamos a importar la vista radar desde el XML, en este caso indicare como quedaria con la metodologia de cambio de vistas por visibilidad.
si pegas este codigo solo veras una listview, y es por que sera la primera vista que aparezca, tambien puedes cambiarla a gone y el linear layout que contiene el radar ponerle el atributo visible, se vera como se indica en la figura que sigue del siguiente código.

 ```xml
 <?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">


    <ListView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/listDeviceBluetooth"
        android:visibility="visible"
        >
    </ListView>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:id="@+id/viewConn"
        android:visibility="gone"
        >

        <com.ingenieriajhr.radarjhr.RadarJhr
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:background="@color/black"
            android:id="@+id/radar"
            android:layout_gravity="center"
            android:layout_marginTop="10dp"
            >
        </com.ingenieriajhr.radarjhr.RadarJhr>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/txtConsola">
        </TextView>

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_marginTop="10dp"
            android:text="Recibir"
            android:id="@+id/btnRx"
            >
        </Button>

    </LinearLayout>


</LinearLayout>
  ```

![image](https://user-images.githubusercontent.com/66834393/185566347-1a622898-01f9-4e3e-bb50-319b566ffebc.png)

3) inicializamos las opciones que esta vista contiene tales como colores, tamaño de letra, tamaño de los puntos capturados por el radar, etc.

 ```kotlin
 //maxima distancia que lograra capturar el sensor dentro de la vista en Centimetros
        radar.maxDistancia(400f)
        //velocidad animacion del radar
        radar.velAnimacion(30)
        //tamaño de las letras que se tienen en los puntos
        radar.tamLetras(20f)
        //tamaño de los puntos capturados
        radar.radioObj(0.05f)
        //color de los puntos que indica el radar
        radar.colorCircle(255, 255, 255)
        //color de las lineas del radar
        radar.colorRadar(0, 255, 0)
        //color de la animacion de las lineas del radar
        radar.colorLinea(0, 255, 0)
        //true para que aparezcan los puntos en el radar
        radar.obj1Visible(true)
        //animacion del radar se muestra y desaparece a medida que se aumenta el valor de alpha
        radar.maxAnimationAlpha = 0
        //iniciamos la animacion del radar, si no se inicia ni los puntos ni la animacion iniciara
        radar.initAnimation(true)
  ```
  
 4) Recibimos la información con ayuda de la implementación listener de la libreriaBluetoothJhr anteriormente mencionada.
 
 debido a que desde arduino recibiremos los datos en forma de string separador cada uno por una coma, con la funcion split convertimos estos datos en un array 
 
 ```kotlin
 private fun rxReceived() {
            blue.loadDateRx(object:BluJhr.ReceivedData{
                override fun rxDate(rx: String) {
                    println("------------------------$rx---------------------")
		    //convierto el string de llegada en un array.
                    val date = rx.split(",")
		    //ya que envio los datos en orden primero envio 45 luego 90 y luego 135
                    var angle = 45
		    //recorro el array 
                    for(i in date){
		    	//le envio los datos a la vista, atributos, angulo y distancia
                        radar.detecto(angle,i.toFloat())
			//adiciono en un textView con el fin de visualizar los datos que no se grafiquen.
                        txtConsola.text = txtConsola.text.toString()+" Angulo: $angle,  Distancia: ${i}\n"
			//sumo el angulo para el proximo dato
                        angle+=45

                    }
                }
            })
        }
 ```
 
 5) en este ejemplo recibo los datos cada vez que pulse un botono debajo del radar, con el fin de tener mas control sobre los datos de llegada.
  ```kotlin
  	
	btnRx.setOnClickListener {
		//arduino cada vez que me reciba una e, me retornara los datos de los sensores.
                blue.bluTx("e")
		//reset puntos que se hayan dibujado
                radar.resetPoints()
		//reinicio la consola que visualiza los datos de llegada.
                txtConsola.text = ""
            }
	
   ```
 

6) a continuacion muestro el codigo de arduino.

```C++
const int Trigger1 = 2;   //Pin digital 2 para el Trigger del sensor
const int Echo1 = 3;   //Pin digital 3 para el echo del sensor

const int Trigger2 = 4;
const int Echo2 = 5;

const int Trigger3 = 6;
const int Echo3 = 7;


void setup() {
  Serial3.begin(9600);//iniciailzamos la comunicación
  initPines(Trigger1,Echo1);
  initPines(Trigger2,Echo2);
}

void initPines(int trigger, int echo){
  pinMode(trigger, OUTPUT); //pin como salida
  pinMode(echo, INPUT);  //pin como entrada
  digitalWrite(trigger, LOW);//Inicializamos el pin 
}

void loop() {
  if(Serial3.available()>0){
    char rxDate = Serial3.read();
    if(rxDate == 'e'){
      String distance1 = String(distance(Trigger1,Echo1));
      String distance2 = String(distance(Trigger2,Echo2));
      String distance3 = String(distance(Trigger3,Echo3));
      String sendAndroid = distance1+","+distance2+","+distance3;
      Serial3.println(sendAndroid);
    }
  }
}

void printDistance(int distance,String sensorName){
  Serial.print("Distancia: "+sensorName+"  ");
  Serial.print(distance);      //Enviamos serialmente el valor de la distancia
  Serial.print("cm");
  Serial.println();
}

/**
 * Return distance 
 */
float distance(int trigger, int echo){
  long t; //timepo que demora en llegar el eco
  long d; //distancia en centimetros

  // put your main code here, to run repeatedly:
  digitalWrite(trigger, HIGH);
  delayMicroseconds(10);          //Enviamos un pulso de 10us
  digitalWrite(trigger, LOW);
  
  t = pulseIn(echo, HIGH); //obtenemos el ancho del pulso
  d = t/59;             //escalamos el tiempo a una distancia en cm
  return d;
}


```

CONEXIÓN ARDUINO MUDULO BLUETOOTH.
![image](https://user-images.githubusercontent.com/66834393/185570783-ee232df7-d3a7-4057-979d-a75017b46354.png)



7) recibo los datos provenientes de arduino, en este caso especial use 3 sensores, como se ve a continuación.

![bluetooth](https://user-images.githubusercontent.com/66834393/188247722-4c248611-9b94-4d19-9523-24a624d77632.png)


A continuacion muestro el codigo completo en kotlin, el codigo xml es el mismo del paso 2.

Gracias por ver este tutorial, agradeceria mucho que te suscribas a mi canal INGENIERIA JHR https://www.youtube.com/ingenieriajhr gracias.

```kotlin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ingenieriajhr.blujhr.BluJhr
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

        lateinit var blue: BluJhr
        var permisosOnBluetooth = false
        var requiredPermissions = listOf<String>()
        var devicesBluetooth = ArrayList<String>()


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            blue = BluJhr(this)
            blue.onBluetooth()
            //maxima distancia que lograra capturar el sensor dentro de la vista en Centimetros
            radar.maxDistancia(400f)
            //velocidad animacion del radar
            radar.velAnimacion(30)
            //tamaño de las letras que se tienen en los puntos
            radar.tamLetras(20f)
            //tamaño de los puntos capturados
            radar.radioObj(0.05f)
            //color de los puntos que indica el radar
            radar.colorCircle(255, 255, 255)
            //color de las lineas del radar
            radar.colorRadar(0, 255, 0)
            //color de la animacion de las lineas del radar
            radar.colorLinea(0, 255, 0)
            //true para que aparezcan los puntos en el radar
            radar.obj1Visible(true)
            //animacion del radar se muestra y desaparece a medida que se aumenta el valor de alpha
            radar.maxAnimationAlpha = 0
            //iniciamos la animacion del radar, si no se inicia ni los puntos ni la animacion iniciara
            radar.initAnimation(true)

            listDeviceBluetooth.setOnItemClickListener { adapterView, view, i, l ->
                if (devicesBluetooth.isNotEmpty()) {
                    blue.connect(devicesBluetooth[i])
                    blue.setDataLoadFinishedListener(object : BluJhr.ConnectedBluetooth {
                        override fun onConnectState(state: BluJhr.Connected) {
                            when (state) {
                                BluJhr.Connected.True -> {
                                    Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT)
                                        .show()
                                    listDeviceBluetooth.visibility = View.GONE
                                    viewConn.visibility = View.VISIBLE
                                    rxReceived()
                                }

                                BluJhr.Connected.Pending -> {
                                    Toast.makeText(applicationContext, "Pending", Toast.LENGTH_SHORT)
                                        .show()

                                }

                                BluJhr.Connected.False -> {
                                    Toast.makeText(applicationContext, "False", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                BluJhr.Connected.Disconnect -> {
                                    Toast.makeText(applicationContext, "Disconnect", Toast.LENGTH_SHORT)
                                        .show()
                                    listDeviceBluetooth.visibility = View.VISIBLE
                                    viewConn.visibility = View.GONE
                                }

                            }
                        }
                    })

                }
            }

            btnRx.setOnClickListener {
                blue.bluTx("e")
                radar.resetPoints()
                txtConsola.text = ""
            }
        }

    private fun rxReceived() {
        blue.loadDateRx(object:BluJhr.ReceivedData{
            override fun rxDate(rx: String) {
                println("------------------------$rx---------------------")
                //convierto el string de llegada en un array.
                val date = rx.split(",")
                //ya que envio los datos en orden primero envio 45 luego 90 y luego 135
                var angle = 45
                //recorro el array
                for(i in date){
                    //le envio los datos a la vista, atributos, angulo y distancia
                    radar.detecto(angle,i.toFloat())
                    //adiciono en un textView con el fin de visualizar los datos que no se grafiquen.
                    txtConsola.text = txtConsola.text.toString()+" Angulo: $angle,  Distancia: ${i}\n"
                    //sumo el angulo para el proximo dato
                    angle+=45

                }
            }
        })
    }



        /**
         * pedimos los permisos correspondientes, para android 12 hay que pedir los siguientes BLUETOOTH_SCAN y BLUETOOTH_CONNECT
         * en android 12 o superior se requieren permisos adicionales
         */
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            if (blue.checkPermissions(requestCode,grantResults)){
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
                blue.initializeBluetooth()
            }else{
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
                    blue.initializeBluetooth()
                }else{
                    Toast.makeText(this, "Algo salio mal", Toast.LENGTH_SHORT).show()
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }



        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (!blue.stateBluetoooth() && requestCode == 100){
                blue.initializeBluetooth()
            }else{
                if (requestCode == 100){
                    devicesBluetooth = blue.deviceBluetooth()
                    if (devicesBluetooth.isNotEmpty()){
                        val adapter = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,devicesBluetooth)
                        listDeviceBluetooth.adapter = adapter
                    }else{
                        Toast.makeText(this, "No tienes vinculados dispositivos", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }

}

```




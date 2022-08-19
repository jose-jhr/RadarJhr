# RadarJhr

En este tutorial te enseñare como implementar un radar con 3 sensores de ultrasonido usando arduino y android studio.
para ello primero debemos importar nuestra libreria de bluetooth que ya vimos anteriormente en nuestro video de youtube a continuacion te anexo el enlace del codigo y el video de youtube, 

enlace github code https://github.com/jose-jhr/blueJhrLibrary
enlace video de youtube https://www.youtube.com/watch?v=ubrTw9pXkXM&t=8s

usare la  misma forma de yotube con la propiedad visible y gone con el fin de evitarme crear una Activity nueva.
una vez ya tengas implementada la configuracion de la libreria bluetooth que nos permitira conectarnos a Arduino, procederemos a importar los archivos necesarios para la implementación.

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
	}
 ```
 
![image](https://user-images.githubusercontent.com/66834393/173492533-d3c0f3e5-85bf-4b57-9890-2fd7709891af.png)
![image](https://user-images.githubusercontent.com/66834393/173492579-5f19d094-3cc4-48a6-902a-793ab19e6899.png)

# SmartHomeBackend
A project created and developed in the minor final module of the minor subject Rehabilitation Technology (major subject Computer Science) at the Technical University of Dortmund under the AGPL-3.0 license. 
The backend implements a rest interface that was defined in an OpenApi definition. The definition can be found in the associated wiki repo.
It connects the frontend to the devices and controls them by communicating with the devices via the Openhab Api interface and the Homeekt. 
Furthermore, it stores the data in a database.
## Config 
The following lists the configuration settings found in the application.properties file, as well as the dependencies and external libraries found in the build.gradle.kt file: 
+ java verion: 17 
+ external libraries: sharedLibrary & homeekt 
+ dependencies: Json & Spring Boot & OKHTTP3 

A postgres database instance is required. 
A url and password for the database must be configured in the application.properties. 
The Homee devices must be configured in the application.yaml file. 

##  Integrate new devices 
###  Integrate OpenHab device 
find information in [OpenHab wiki](https://www.openhab.org/docs/tutorial/first_steps.html) . 
It is necessary to save a token in the application.yaml file. <br>
If you have an item that should only be displayed (such as "battery charge"), it is necessary to set a tag "makeString". <br>
It is necessary to rename the devices according to the following pattern: <br>
{                              <br>
"name":"device name",<br>
"icon":"icon name",<br>
"description":"description",<br>
"rangeWithButtons":True or False,<br>
"rangeMaxText":"An", //only useful, when rangeWithButtons == True<br>
"rangeMinText":"Aus"<br>
}


### Integrate Homee device 
see homee wiki <br>
It is necessary to save a url, username and password in the application.yaml file.
Add json in application.properties

## short information on Docker
There is a Dockerfile with which it is possible to run a container on a server. It is possible to configure it via Docker instead of configuring it in the application.properties file. By default, the container runs on port 9000.

# SmartHomeBackend
Ein Projekt, welches im Nebenfachabschlussmodul vom Nebenfach Rehabilitationstechnologie (Hauptfach Informatik) an der Technischen Universität Dortmund unter der AGPL-3.0 Lizenz erstellt und entwickelt wurde. <br>
Das Backend setzt eine Rest-Schnittstelle um, welche in einer OpenApi Definition definiert wurde. Die Definition ist im zugehörigen Wiki-Repo zu finden.
Es verbindet das Frontend mit den Geräten, und steuert diese, indem es über die Openhab Api Schnittstelle und der Homeekt mit den Geräten kommuniziert. <br>
Desweiteren, speichert es die Daten in einer Datenbank.



## Config
Im folgenden werden die Konfigurationseinstellungen aufgelistet, die in der application.properties Datei zu finden sind, sowie die Dependencies und externen Libraries die in der build.gradle.kt Datei zu finden sind :
+ java verion: 17
+ externe libraries: sharedLibrary & homeekt
+ dependencies: Json & Spring Boot & OKHTTP3

Eine postgres Datenbank Instanz wird benötigt. <br>
In der application.properties muss eine url und ein Passwort für die Datenbank konfiguriert werden. <br>
In der application.yaml Datei müssen die Homee Geräte konfiguriert werden.


## neue Geräte einbinden

### OpenHab Gerät einrichten

#### um ein neues Gerät über OpenHab einzubinden, kann man Informationen dazu im  [OpenHab wiki](https://www.openhab.org/docs/tutorial/first_steps.html) finden.
Es ist notwendig, ein OpenHab token zu erstellen und in der application.yaml Datei zu speichern.
Wenn man ein Item hat, welches nur angezeigt weren soll (wie beispielsweise "Batterieladung), ist es notwendig einen Tag "makeString" zu setzen.
Es ist notwendig, die Geräte nach folgendem Muster umzubenennen: <br>
{                              <br>
"name":"device name",<br>
"icon":"icon name",<br>
"description":"description",<br>
"rangeWithButtons":True or False,<br>
"rangeMaxText":"An", //only useful, when rangeWithButtons == True<br>
"rangeMinText":"Aus"<br>
}

### Homee Gerät einbinden

siehe homee wiki
In der application.yaml Datei muss url, username und passwort gespeichert werden.

in application.properties json hinzufügen

## kurzinformationen über Docker

Es existiert ein Dockerfile, mit dem es möglich ist, einen Container auf einem Server laufen zu lassen. <br>
Es ist möglich, die Konfiguration über Docker zu konfigurieren, anstatt es in der application.properties Datei zu konfigurieren. <br>
Standardmäßig läuft der Container auf Port 9000.

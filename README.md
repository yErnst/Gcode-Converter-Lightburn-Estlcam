# gcode-converter-Lightburn-Estlcam

Dieses Programm ermöglicht mit Lightburn erstellten G-code, in für Estlcam verständlichen G-code zu konvertieren.

Es ist kein wechsel zwischen Estlcam Firmware und GRBL auf dem Arduino notwendig.

# Änderungen in Version 3
GUI mit vorschau.
![1 1](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/0f02f19f-9ba0-401a-b45b-2069a051c1c8)

# Änderungen in Version 2
-Fix: NC Program funktioniert unter Estlcam V12 nicht.

-Fix: E-Notation im NC Program behoben.

# Einstellungen Lightburn:

1.Erstellen eines neuen Geräts

![1](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/74018829-f556-494d-983b-23293b4dc04c)

2. Benenne das Gerät und gib den verfahrweg der Achsen deiner Maschine an.
   
![2](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/1977afff-3765-4f34-918f-fc5a90c894b0)

4. Setze den Nullpunkt auf "Hinten Links".
   
![3](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/2f414420-ba8a-4558-8ea4-9fc36ec23be3)

# Verwendung und Vorraussetzungen des Converters (V1 und V2)

Zum ausführen des Gcode-Converter muss Java installiert sein. (getestet mit Java 8 und 17)

Starte den Gcode-Converter mit einem Doppelklick und wählen den mit Lightburn erstellen Gcode aus.

![4](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/cd1ab7a0-adb7-4941-8ca2-cc25d26b1184)

Nach klicken auf Öffnen wird eine 2. Datei mit der zusätzlichen Dateiendung .g90.nc im selben Verzeichniss erstellt. Diese Datei kann nun in Estlcam verwendet werden.

![5](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/c105ec8c-ce0a-4eeb-a9f4-b29db1ea3ab5)

# Setzen des Projektordners beim starten des Programms (V1 und V2)
Erstelle eine Verknüpfung und hänge den Pfard zum startordner wie folgt an. (Der Pfard muss zwischen " geschrieben werden)

![6](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/75cc57ab-fc01-4adf-91b8-1fec4ab56de0)
Bei V3 wie bei V1 und V2 nur mit -p= vor dem Pfard
Weitere Komandozeilenparameter finden Sie im Program unter Hilfe->CMD Help

# Beispielprojekt
Lightburn

![7](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/cfd3659e-11ff-48a2-84e5-d0f783b5d1cc)

Vor verwendung des Converters

![8](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/36ae508f-7209-4f1b-9bb7-e86844b70245)

Nach verwendung des Converters

![9](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/d2ab0c8c-1605-4bc7-9b8d-e059a4d134fd)

Es ist empfehlenswert in Lightburn die Fill Funktion mit overscanning zu verwenden.

# Weiteres

Die Anwendung kann Fehler enthalten. Verwendung auf eigene Gefahr.
G02 und G03 befehle sind nicht unterstützt. (Sollte nach den wenigen bisherigen Tests auch nicht notwendig sein)
Es wurde gcode nur ohne Z verfahrwege getestet.

# gcode-converter-Lightburn-Estlcam

Dieses Programm ermöglicht mit Lightburn erstellten G-code, in für Estlcam verständlichen G-code zu konvertieren.

Es ist kein wechsel zwischen Estlcam Firmware und GRBL auf dem Arduino notwendig.

# Änderungen in Version 3.2
Neue Messfunktionen hinzugefügt.

![1 1](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/0f02f19f-9ba0-401a-b45b-2069a051c1c8)

# Einstellungen Lightburn:

1.Erstellen eines neuen Geräts

![1](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/74018829-f556-494d-983b-23293b4dc04c)

2. Benenne das Gerät und gib den Verfahrweg der Achsen deiner Maschine an.
   
![2](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/1977afff-3765-4f34-918f-fc5a90c894b0)

4. Setze den Nullpunkt auf "Hinten Links".
   
![3](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/2f414420-ba8a-4558-8ea4-9fc36ec23be3)

# Verwendung und Vorraussetzungen

Zum ausführen des Gcode-Converter muss Java installiert sein. (getestet mit Java 8, 17 und 23)

Für den vollen Funktionsunfang muss Windows und midestens Java 17 verwendet werden.

Starte den Gcode-Converter mit einem Doppelklick und öffne im Linken rand (Projektexplorer) den mit Lightburn erstellen Gcode.

Der Standard Projektordner, ist der Ordner, in dem die .jar liegt. (Zum Ändern siehe weiter unten)

Alternativ kann man den gcode von Lightburn auch über Datei->Öffnen aufmachen.

![grafik](https://github.com/user-attachments/assets/ebd00533-a89c-496f-9599-45f0e86dccec)

Öffne den Gcode durch einen klick auf "Öffnen in Estlcam". (Estkcam muss als Standart für die .nc endung gesetzt sein)

# Setzen des Projektordners beim starten des Programms
Erstelle eine Verknüpfung und hänge den Pfard zum Projektordner wie folgt an. (Der Pfard muss zwischen " geschrieben werden)

![grafik](https://github.com/user-attachments/assets/ae7552dd-8637-4126-8484-789f39b8eab2)

Weitere Komandozeilenparameter finden Sie im Program unter Hilfe->CMD Help

# Beispielprojekt
Lightburn

![7](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/cfd3659e-11ff-48a2-84e5-d0f783b5d1cc)

Vor verwendung des Converters

![8](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/36ae508f-7209-4f1b-9bb7-e86844b70245)

Nach verwendung des Converters

![9](https://github.com/yErnst/Gcode-Converter-Lightburn-Estlcam/assets/144956031/d2ab0c8c-1605-4bc7-9b8d-e059a4d134fd)

Es ist empfehlenswert in Lightburn die Fill Funktion mit overscanning zu verwenden.
Nach meinen erfahrungswerten ist eine versatzkorrektur von 0.08 in Lightburn einzustellen. (kann abweichen)

![grafik](https://github.com/user-attachments/assets/890e72f8-25e0-4285-980b-036d573e1abd)

# Weiteres

Die Anwendung kann Fehler enthalten. Verwendung auf eigene Gefahr.
G02 und G03 befehle sind nicht unterstützt. (Sollte nach den bisherigen Tests auch nicht notwendig sein)
Z Achsen verfahrwege sind nicht möglich, da durch den "Lasergravur" befehl am anfang des Gcodes, Estlcam alle Z bewegungen ignoriert.

# gcode-converter-Lightburn-Estlcam

Dieses Programm ermöglicht, mit Lightburn erstellten G-code in für Estlcam verständlichen G-code zu konvertieren.

# Einstellungen Lightburn:

1.Erstellen eines neuen Geräts

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/382c05a5-3689-4318-965e-833277b1e75e)

2. Benenne das Gerät und gib den verfahrweg der Achsen deiner Maschine an.
   
![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/3ebefc18-3d39-49d8-b927-a778a1f7d932)

4. Setze den Nullpunkt auf "Hinten Links".
   
![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/33eed461-7163-44a8-8cc6-3d419f648483)


# Verwendung und Vorraussetzungen des Converters

Zum ausführen des Gcode-Converter muss Java installiert sein. (getestet mit Java 8 und 17)

Starte den Gcode-Converter mit einem Doppelklick und wählen den mit Lightburn erstellen Gcode aus.

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/73be58b2-1f17-49d8-803b-4f00ebd44e19)

Nach klicken auf Öffnen wird eine 2. Datei mit der zusätzlichen Dateiendung .g90.nc im selben Verzeichniss erstellt. Diese Datei kann nun in Estlcam verwendet werden.

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/cd5f586b-344e-4868-ba2e-c3dc905f425a)

# Setzen des Projektordners beim starten des Programms
Erstelle eine Verknüpfung und hänge den Pfard zum startordner wie folgt an. (Der Pfard muss zwischen " geschrieben werden)

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/d05e7f55-b755-4c30-86a1-bd164569d180)

# Beispielprojekt
Lightburn

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/7d5997b7-45fc-4934-9ff1-dc776ca443ea)

Vor verwendung des Converters

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/d59ee035-fc70-4a6a-b27a-9a07c5d045ca)


Nach verwendung des Converters

![grafik](https://github.com/yErnst/gcode-converter-Lightburn-Estlcam/assets/144956031/b61bd66c-ce19-43f3-91c4-0e889c53beb8)

Es ist empfehlenswert in Lightburn die Fill Funktion mit overscanning zu verwenden.

# Weiteres

Die Anwendung kann Fehler enthalten. Verwendung auf eigene Gefahr.
G02 und G03 befehle sind nicht unterstützt. (Sollte nach den wenigen bisherigen Tests auch nicht notwendig sein)
Es wurde gcode nur ohne Z verfahrwege getestet.

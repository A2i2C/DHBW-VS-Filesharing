# File-Sharing

## Projekt starten
1. Docker Compose starten mit docker-compose up -d
2. Auf ```localhost:4200``` gehen
## Ablauf
* Um zu beginnen, wählen Sie einen Benutzer aus, um mit sich selbst Dateien auszutauschen, oder wählen Sie einen zweiten Benutzer aus.
* Hinzufügen von sich selbst oder einem anderen Benutzer mit der Schaltfläche auf der linken Seite.
* Dateien mit dem Plus hochladen, Dateien mit dem Pfeil herunterladen oder mit dem Papierkorb löschen.

## Wichtige Hinweise
* Wenn ein Benutzer hinzugefügt wird, um ein Sharing zu starten, muss kurz gewartet werden, bevor der nächste Benutzer hinzugefügt werden kann. Dies ist auf die Dauer der API-Aufrufe zurückzuführen. Das Programm funktioniert trotzdem, es erscheint nur eine Fehlermeldung, dass der Benutzer nicht gefunden werden konnte, weil die Metadaten noch nicht in der Datenbank sind.
* Bei einem Neustart von Docker Compose müssen die Verzeichnisse ```data``` (unter Database) und ```minio``` gelöscht werden, um missgünste zu vermeiden.

# File-Sharing

## Wichtige Hinweise
* Wenn ein Benutzer hinzugefügt wird, um ein Sharing zu starten, muss kurz gewartet werden, bevor der nächste Benutzer hinzugefügt werden kann. Dies ist auf die Dauer der API-Aufrufe zurückzuführen. Das Programm funktioniert trotzdem, es erscheint nur eine Fehlermeldung, dass der Benutzer nicht gefunden werden konnte, weil die Metadaten noch nicht in der Datenbank sind.
* Bei einem Neustart von Docker Compose müssen die Verzeichnisse ```data``` und ```minio``` gelöscht werden, da sonst die Daten ungültig sind und es zu Problemen kommen kann.

# File-Sharing

## Projekt starten
1. Docker Compose starten
2. Auf ```localhost:4200``` gehen

## Wichtige Hinweise
* Wenn ein Benutzer hinzugefügt wird, um ein Sharing zu starten, muss kurz gewartet werden, bevor der nächste Benutzer hinzugefügt werden kann. Dies ist auf die Dauer der API-Aufrufe zurückzuführen. Das Programm funktioniert trotzdem, es erscheint nur eine Fehlermeldung, dass der Benutzer nicht gefunden werden konnte, weil die Metadaten noch nicht in der Datenbank sind.
* Bei einem Neustart von Docker Compose müssen die Verzeichnisse ```data``` (unter Database) und ```minio``` gelöscht werden, um missgünste zu vermeiden.
* Falls bei einem Shard MinIO Instanzen abstürzen funktioniert dieser Shard auch weiterhin bis alle MinIO-Instanzen, von einem Shard, nicht mehr funktionieren.

# AppTime

Android app for time tracking

//TODO get safer UUID
//TODO export and import in json
//TODO handler for toast or snackbar info(needs access to activity)
//TODO introduce i18n
//TODO limit groups in groups to depth=5, activity stack limit!!
//TODO move timeentries
//TODO mark item as standard for this group

//TODO edit name in dialog not directly

//TODO about dialog

//TODO dialog before remove

//TODO unit tests for data objects, DbWorkers

//TODO nice chronometer format, without performance issues (customUI element with timertask)

//TODO mainview limit considered start dates --> only show last month
//TODO settingDB

//reminder, if an hour running, auto stop?

Release 1.2------------------------

//TODO deutsche übersetzung

//Screen der die Zeit aufteilen kann von einem timeEntry
//Konzept überlegen, wenn auf Gruppe Start geklickt wird timeEntry markieren

AccountItem starten nach geolocation:
 geofences

AccountItem starten wenn kalendereintrag
 -custom field within calendar item? or Account or Work Calendar and private calendar every item(or only groups) can have such a calendar

//Kommentar zu jedem TimeEntry - (db erweiterung!!)

//faster start stop (dbworker get called too often)
 besseres konzept fuer die property change events bzw vereinheitlichen

//implement remove in agendaView

//API Anbindung mit Server, integration mit nextcloud oder komplett standalone
verbindung mit calendar(auf app zu lokalem calendar und auf server mit nextcloud calendar)(caldav)
 algorithmus entwickeln
  Konflikte?

 synchronisierung von account items
 berechnung und erstellung nur lokal
 auswertung am server

 endpoints:
   createItem
   createTimeEntry

am server wird es abgespeichert wie in der client db


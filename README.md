# Reva beursapplicatie

De reva beursapplicatie is een androidapplicatie ontworpen om studenten uit verschillende scholen op een interactieve manier te laten deelnemen aan de REVA beurs. De applicatie is een quiz die toelaat om vragen te beantwoorden van verschillende exposanten over bepaalde categoriën gerelateerd aan de beurs. Ze kunnen deze exposant lokaliseren door een map die aantoont waar de exposant zich exact bevindt.

## Het opstellen en opstarten van de applicatie

Deze instructies zullen u helpen bij het opstarten van de applicatie lokaal op uw emulator of op uw persoonlijk android device

### Voorwaarden

Voor het downloaden en installeren van de repository is het noodzakelijk een git installatie te hebben. Deze is downloadbaar op [Github](https://git-scm.com/downloads)

### Stap 1 - Clonen repository

Indien Git geïnstalleerd is zal de repository met het project moeten gecloned worden. Hiervoor moet er in de command line het volgende commando uitgevoerd worden

```
git clone https://github.com/HoGent-Projecten3/projecten3-1819-android-groep8-reva.git
```

### Stap 2 - Sync gradle file with project and build project

Na het clonen van de repository is het vereist om de gradle file the synchroniseren met het project. Hiervoor moet er vanuit de command line eerst genavigeerd worden naar de project-folder met het volgende commando:

```
cd projecten3-1819-android-groep8-reva/
```

Vervolgens kan men de gradle file synchroniseren met het project adhv de volgende knop.

![snip20181216_1](https://user-images.githubusercontent.com/6177799/50057937-ef588980-0171-11e9-9bb1-e98bcfc0db07.png)

Hierna kan men het project builden adhv de volgende knop

![snip20181216_4](https://user-images.githubusercontent.com/6177799/50057949-026b5980-0172-11e9-9cd4-c4892a65562e.png)

### Stap 3 - Opstarten android app

Om de applicatie op te starten wordt klikt men op de volgende knop

![snip20181216_5](https://user-images.githubusercontent.com/6177799/50057950-026b5980-0172-11e9-82d7-c39a4e689890.png)

Hier heeft men de keuze om een geconnecteerd apparaat te kiezen of één van de beschikbare emulators. Indien er nog geen emulator op de computer staat kan men kiezen voor de optie "Create New Virtual Device".

![snip20181216_8](https://user-images.githubusercontent.com/6177799/50057951-026b5980-0172-11e9-8599-0abaa1c1eec5.png)

## Mappenstructuur

Alle code van het project is te vinden in de folder `src/app/` wanneer men zich in de hoofddirectory bevindt. In deze folder is de volgende onderverling gemaakt: 

| Map         | Functionaliteit                                              |
| ----------- | :----------------------------------------------------------- |
| androidTest | Deze map bevat alle testen die zijn uitgevoerd om te kijken of de applicatie 100% functioneel is. |
| main        | Zie volgende tabel                                           |

De map main werd onderverdeeld in volgende submappen

| Map    | Functionaliteit                                              |
| ------ | ------------------------------------------------------------ |
| assets | Deze map bevat verschillende bestanden die nodig zijn om bepaalde objecten op het scherm te tonen. |
| java   | Deze map bevat de businesslogica/het hart van de applicatie. Hierin staat alle code die de applicatie functioneel maakt. |
| res    | Deze map bevat de layout files voor de applicatie. Deze files presenteren maw de applicatie. |

## Authors

Zes derdejaars studenten Toegepaste Informatica - Programmeren - Mobiele Applicaties

- Alexander Willems - [ciwie36963](https://github.com/ciwie36963)
- Cedric Arickx - [Ganondus](https://github.com/ganondus)
- Jens De Wulf - [Jensdewulf](https://github.com/Jensdewulf/)

- Jelle Geers - [JelleGeers](https://github.com/jellegeers)
- Karel Heyndrickx - [KarelHeyndrickx](https://github.com/karelheyndrickx)
- Matthias De Fré - [MatthiasDeFre](https://github.com/MatthiasDeFre)

## APK file

Link to apk file: https://uploadfiles.io/lxr9v
(The link is valid for 30 days)

## License

Dit project is eigendom van Projecten III - team 8 - 2018 van HoGent.

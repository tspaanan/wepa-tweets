# WEPA-Tweets

Helsingin yliopiston tietojenkäsittelytieteen laitoksen syksyllä 2021 järjestämän kurssin 'Web-palvelinohjelmointi Java' harjoitustyö.

## Sovelluksen ominaisuudet

[Linkki harjoitustyön vaatimusmäärittelyyn](https://web-palvelinohjelmointi-21.mooc.fi/projekti)

Vaatimusmäärittelyn yhteydessä todetaan yhteenvetona kyseessä olevan "seuraus- ja kuvasovellus eli tuttavallisemmin vanhan kansan Twitter".

## Sovelluksen testikäyttö

Sovellusta voi testikäyttää seuraavassa osoitteessa: [linkki](https://tspaanan-wepa-tweets.herokuapp.com/)

## Sovelluksen suorittaminen paikallisesti

Ohjelmisto on konfiguroitu siten, että sen voi ajaa seuraavalla komennolla:
```
mvn spring-boot:run
```
Tuotantoympäristössä, johon sovellus käynnistyy, on alussa tyhjä tietokanta.

Sivupalkissa kannattaa ensin rekisteröityä (ja kirjautua sisään). Tämän jälkeen sivupalkista löytyy linkki omalle profiilisivulle, jossa voi kirjoittaa uusia viestejä. Viesteihin voi puolestaan kirjoittaa kommentteja, ja niistä voi tykätä. Profiilisivulta löytyy myös linkki omaan kuva-albumiin, jonne voi ladata kuvia. Yhden kuvista voi asettaa profiilikuvaksi.

Kun käyttäjiä on useampia, toisia käyttäjiä voi seurata (tai blokata nämä seuraamasta).

## Sovelluksen automaattinen testaaminen

Automaattiset testit toimivat tällä hetkellä seuraavasti:
* ota src/main/java/projekti/ProductionSecurityConfiguration.java -tiedostossa rivin 17 annotaatio @Profile("production") käyttöön
* tämän jälkeen automaattiset testit voi ajaa seuraavalla komennolla:
```
mvn test
```

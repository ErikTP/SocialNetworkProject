# SocialNetworkProject / Social Network Management

SocialNetworkProject är en Spring Boot projekt, skapad enligt MVC-paradigmet (Model-view-Controller) och Java-mallmotorn
Thymeleaf som bearbetar fram dess HTML, CSS och JavaScript. Projektet har som syfte att servera som en social nätverksapplikation
som både är webbaserad och inriktad till nätföreningar av människor som delar intressen och aktiviteter, eller som är intresserade
av att utforska andras intressen och aktiviteter. Likt andra sociala nätverksapplikationer erbjuds användaren möjligheten att 
kunna registrera sitt konto, logga in på sitt konto, skapa profil med bild, skapa inlägg, läsa inlägg, radera inlägg och att 
redigera sitt användarkonto.

---

## Starta/Ladda ner webbapplikation

Implementera webbapplikationen lokalt:

* Klona projektets befintliga Repository från den webbaserade programvarulagringen [GitHub](https://github.com/ErikTP/SocialNetworkProject), till önskad lokal 
  mappdestination. 

```
  HTTPS klonkod:
    * https://github.com/ErikTP/SocialNetworkProject.git

  Git-versioner som hanterar projektet:
    * GitHub Desktop
    * GitKraken - Git GUI
    * Git Bash - Git CLI
```
  
* Skapa databas inom [MySQL Workbench CE](https://www.mysql.com/) genom att koppla till MySQL servern localhost via användarnamn och lösenord.

```
  DATABAS LÖSNINGAR:

  MySQL:
    * Klicka på databas ikonen för att skapa ett nytt schema i den anslutna servern
    * Skriv namn till shemat och klicka på tillämpa 

  POM.xml:
    * Generera en MySQL dependency injektion inom pom.xml vid avsaknad:  
    
    <dependency>      
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    
  IntelliJ IDEA koppling till MySQL:
    * Klicka på "DATABASE" som är lokaliserad till höger på kanten
    * Klicka på plusikonen "NEW" in till "DATASOURCE" för val av datakälla
    * Inom val av datakällor klickar du på MySQL för koppling
```
  
* Inom **application.properties** anger du samma användarnamn och lösenord för MySQL, för att kunna koppla SocialNetworkProject
  till MySQL databasen:

```
    Application.properties:
      spring.datasource.url=jdbc:mysql://localhost:3306/socialnetworkprojects?useSSL=false&serverTimezone=UTC
      spring.datasource.username=root
      spring.datasource.password=Boliviano931
```

* Kör projektet med IntelliJ (IDE) och navigera via din webbläsare till URL localhost:8080.
```
  Starta webbapplikation: 
    * Klicka på "RUN" ikonen längst upp för att köra projektet
    * Navigera till URL localhost:8080
```

---

## Dependencies
Dependency injektioner inom projektets pom.xml fil som bygger fram webbapplikationen. 

* Spring Boot - Back-End ramverk som konfigurerar och skapar mikrotjänster.
    * Spring Boot Starter    
    * Spring Boot Starter Test
  

* Spring MVC - Spring ramverk som hjälper till att hantera HTTP-förfrågningar och svar
    * Spring Boot Starter Web
    * Spring Boot Starter Thymeleaf
    * Spring Boot DevTools


* Spring JPA - Implementerar JPA repository för att addera dataåtkomstlagret i applikationer
  * Spring Boot Starter Data JPA
  * MySQL Connector Java


* Spring Security - Anpassningsbar säkerhetsramverk för autentisering och auktorisering
  * Spring Boot Starter Security
  * Spring Security Test
  * Thymeleaf Extras SpringSecurity5

---

## Byggverktyg
* Maven - Administration av dependencies
* HTML - Thymeleaf
* CSS - Bootstrap v.5.13.0
* JavaScript - JQuery v.3.4.1
---  

## Java-version/SDK
* Java/JDK version - Java 11
    * Projekt SDK - version 16.0.2
---


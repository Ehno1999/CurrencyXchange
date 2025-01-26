Förutsättningar innan du kör programmet
För att köra applikationen behöver du följande installerat:

JDK (Java Development Kit)
Apache Maven

Klona projektet från GitHub.
Gå till projektmappen i terminalen:

Navigera till projektmappen via terminalen och kör:
mvn clean install

För att starta applikationen, kör följande kommando i terminalen:

mvn spring-boot:run

Applikationen kommer att köras på porten: http://localhost:8080

Ett GUI finns tillgängligt på:
http://localhost:8080

För att testa endpoints utan GUI kan du använda följande portar:

Hämta valutakurs:
http://localhost:8080/find/{baseCurrency}/{targetCurrency}/{date}
Utför valutaväxling:
http://localhost:8080/exchange/{baseCurrency}/{targetCurrency}/{date}/{amount}
Få data för en specifik valuta:
http://localhost:8080/{baseCurrency}/{targetCurrency}/{date}

Där:

baseCurrency är basvalutan
targetCurrency är valutan du vill växla till
date är datumet för valutakursen
amount är beloppet du vill växla
Köra Tester

För att köra tester, använd kommandot:

mvn test
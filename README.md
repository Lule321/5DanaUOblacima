# 5DanaUOblacima

Koriscen alat:
Eclipse IDE for Enterprise and Web Developers 2021 - 12 sa instaliranim Spring Tools 4 dodatkom (preko Marketplace-a)

Koriscene tehnologije:
   - Maven (Generisan od Eclipse-a)
   - Spring Boot
   - MySql
   - JPA/Hibernate
   - JUnit - preko Spring Boot dependency-ja
   - Java verzija 11 (za Spring Boot, ja sam koristion 11.0.11)

Pokretanje projekta:
    Preko eclipse sa projekat lako pokrece, naime treba naci klasu ProjectApplication i pokrenuti je bilo kao Spring project ili kao java aplikaciju. Projekat sam generise bazu podataka.

    Sa vasom bazom se povezujete tako sto u application.properties fajlu promenite port ako je potrebno (stoji 3306), i promenite spring.datasource.username i spring.datasource.password u Vas username i password.

    Za testiranje sam koristio pokretanja preko code coverage-a, i to tako sto se preko JUnit-a pokrene OrderRequestTest.

    Ako stignem, mozda napisem kako se pokrece bez eclipse-a.

Napomene:
    Projekat nije konkurentan, te ce vise paralelnih zahteva verovatno generisati gresku u bazi podataka (race condition). Nisam stigao ovo da odradim. Za testiranje nisam testirao view-ove, nego klasu odgovornu za algoritam ubacivanja Order-a. Nisam skroz zavrsio njeno testiranje, ali je vecina gotova.

Struktura projekta:

    .idea:
        - Poceo sam da radim u intellij-u, pa sam odustao od njega (hteo sam da vidim kako on izgleda, a i verzija ultimate nije bas open source), svakako nebitan folder
    
    README.md:
        - Fajl, sa osnovnim informacijama o projektu
    
    workspace:
        - Workspace za eclipse
    
    
    workspace\project:
        - Folder sa projektom

    workspace\project\src:
        - Folder sa java fajlovima projekta

    workspace\project\src\main\resources:
        - Folder sa jedino bitnim application.properties fajlom za podesavanje Spring aplikacije.

    
    Paketi:

        workspace\project\src\main\java\entities:
            - Sadrzi entitete baze podataka
            - OrderBookElement
            - OrderBookEntity
            - OrderEntity
            - TradeEntity
        
        workspace\project\src\main\java\entities\utilities:
            - Sadrzi enume koji olaksavaju rad sa entitetima
            - Status (enum koj oznacava OrderEntity status)
            - Type (enum koj oznacava OrderEntity type)

        workspace\project\src\main\java\repository:
            - Sadrzi repository interface-e za rad sa bazom podataka, Spring ih posle sam povezuje (preko @Autowired)
            - OrderBookRepository
            - OrderRepository
            - TradeRepository
        
        workspace\project\src\main\java\com\five_days_in_cloud\project
            - Sadrzi glavne delove projekta
            - ProjectApplication (aplikacija za pokretanje projekta)
            - AppController (aplikacija za generisanje view-ova, tj aplikacija koja prima Http zahteve)
        
        workspace\project\src\main\java\com\five_days_in_cloud\project\utilities:
            - Sadrzi pomocne klase za obradu Http zahteva
            - OrderValidationValues (enum koj numerise greske pri kreiranju OrderEntity-a)
            - OrderRequest (klasa koja implementira algoritam kreiranja Order-a, ukljucujuci i inicijalizaciju OrderEntity-a i njegovu validaciju)
        

        workspace\project\src\test\java\com\five_days_in_cloud\project:
            - Sadrzi klasu za testiranje OrderRequest-a
            - ProjectApplicationTest nisam koristio
            - OrderRequestTest (preko Unit testove testira OrderRequest klasu)
        
    workspace\project\pom.xml:
        - Fajl sa dependency-ima koje koristimo u projektu


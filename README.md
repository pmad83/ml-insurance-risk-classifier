# Aplikacja do klasyfikacji ryzyka ubezpieczeniowego

## Architektura rozwiązania

### Komponenty systemu

1. **Aplikacja klasyfikacyjna** `ml-insurance-risk-classifier`
    - Aplikacja zbudowana na Spring Boot, wykorzystująca model uczenia maszynowego do klasyfikacji ryzyka ubezpieczeniowego.
    - Używa biblioteki Tribuo do budowy i trenowania modelu ML.
    - Udostępnia REST API umożliwiające klasyfikację klientów na podstawie ich danych.

2. **Baza danych H2**
    - Wbudowana baza danych H2 jest używana głównie w środowisku deweloperskim i testowym.
    - Służy do przechowywania historycznych danych klientów i wyników klasyfikacji.

### Technologie

Aplikacja korzysta z następujących technologii i komponentów:

- **Java 21** - najnowsza wersja LTS, zapewniająca stabilność i długoterminowe wsparcie.
- **Spring Boot** - główny framework aplikacji.
- **Spring Data JPA** - warstwa dostępu do bazy danych oparta na JPA, umożliwiająca interakcję z bazą danych w sposób obiektowy.
- **Spring Web** - pozwala na tworzenie interfejsów REST dla aplikacji.
- **Tribuo** - biblioteka do uczenia maszynowego, wykorzystywana do budowy modelu klasyfikacyjnego.
- **OpenAPI/Swagger** - narzędzia do dokumentacji REST API i generowania interfejsów REST.
- **H2 Database** - wbudowana baza danych, używana do testów i dewelopmentu.

## Uruchomienie aplikacji

Aby uruchomić aplikację, należy wykonać następujące kroki:

1. Przejść do katalogu projektu np. `cd C:\git\ml-insurance-risk-classifier`.
2. Uruchomić polecenie `gradlew bootRun`.

## Dostęp do aplikacji

Poniżej znajduje się lista adresów, pod jakimi dostępne są poszczególne komponenty aplikacji.

| Nazwa komponentu | Adres                                                                                      |
|------------------|--------------------------------------------------------------------------------------------|
| SwaggerUI        | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |
| Usługa REST      | [http://localhost:8080/api/customer/classify](http://localhost:8080/customer/classify)     |

Przykładowe zapytanie REST do klasyfikacji klienta:

```json
{
  "age": 25,
  "accidentsQty": 1,
  "insuranceSum": 100,
  "claimsHistory": 0
}
```

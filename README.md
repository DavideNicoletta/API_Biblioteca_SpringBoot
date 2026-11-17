# 📚 Gestionale Biblioteca - REST API (Spring Boot & JWT)

Backend RESTful sviluppato con **Spring Boot** per la gestione completa di una biblioteca: catalogo libri, anagrafica utenti con ruoli, prestiti/restituzioni e autenticazione stateless tramite **Spring Security & JSON Web Token (JWT)**.

Questa versione espone la **stessa API** della riscrittura in C# (ASP.NET Core): percorsi, codici di risposta e formato dei dati sono allineati, così un unico frontend funziona con entrambi i backend.

---

## 🚀 Tecnologie Utilizzate

* **Java 17**
* **Spring Boot**
  * **Spring Web MVC** (REST API)
  * **Spring Data JPA / Hibernate** (Persistenza dati)
  * **Spring Security** (Autenticazione e Autorizzazione basata su ruoli)
* **JJWT (`io.jsonwebtoken`)** (Generazione, firma e validazione token JWT)
* **BCrypt** (Hashing sicuro delle password)
* **MySQL** (Database relazionale)
* **Maven** (Gestione dipendenze e build)

---

## 🏗️ Architettura del Progetto

Il progetto segue un'architettura stratificata (*Layered Architecture*) con separazione tramite **DTO** e **Mapper**:

```text
src/main/java/com/example/demo/
├── controllers/       # Endpoint REST (AuthController, BookController, UserController, BorrowController)
├── dto/               # Data Transfer Objects (RequestDTO e ResponseDTO)
├── exceptions/        # NotFoundException, ConflictException e GlobalExceptionHandler (404, 409, 401)
├── mapper/            # Conversione tra Entity e DTO (BookMapper, UserMapper, BorrowMapper)
├── models/            # Entità JPA (User, Role, Book, Borrow)
├── repositories/      # Interfacce Spring Data JPA (UserRepository, RoleRepository, BookRepository, BorrowRepository)
├── response/          # Wrapper standardizzato delle risposte API (ResponseAPI<T>)
├── security/          # Configurazione Spring Security e JWT (SecurityConfig, JwtService, JwtAuthenticationFilter, CustomUserDetailsService)
└── services/          # Logica di business e gestione transazioni (UserService, BookService, BorrowService)
```

---

## ⚙️ Configurazione ed avvio

### Prerequisiti

* Java 17
* MySQL con un database `biblioteca` già creato. Hibernate è impostato su `ddl-auto=validate`: **le tabelle devono esistere** e corrispondere alle entità (`users`, `roles`, `user_roles`, `books`, `borrows`), con i ruoli `ROLE_USER` e `ROLE_ADMIN` presenti nella tabella `roles`.

### Segreti

Password del database e chiave JWT **non sono nel repository**. `application.properties` usa dei segnaposto e Spring legge i valori veri da un file locale, ignorato da Git.

1. Copia `secrets.properties.example` in `secrets.properties` (nella radice del progetto, accanto a `pom.xml`):

   ```properties
   DB_PASSWORD=la_tua_password_mysql
   JWT_SECRET=una_chiave_in_base64_di_almeno_32_byte
   ```

   `DB_USERNAME` è facoltativo (predefinito: `root`).

2. La chiave JWT deve essere in **base64** (almeno 32 byte). Per generarne una, ad esempio in PowerShell:

   ```powershell
   [Convert]::ToBase64String([guid]::NewGuid().ToByteArray() + [guid]::NewGuid().ToByteArray())
   ```

### Avvio

Dalla radice del progetto:

```bash
./mvnw spring-boot:run
```

L'API risponde su `http://localhost:8082`.

---

## 🔐 Autenticazione e Sicurezza (JWT)

L'API utilizza una configurazione **Stateless** basata su token JWT:
1. L'utente effettua il login tramite `POST /api/auth/login` inviando `username` e `password`.
2. Il server verifica le credenziali con `BCryptPasswordEncoder` e restituisce un **token JWT** firmato.
3. Per accedere agli endpoint protetti, il client deve includere il token nell'header HTTP di ogni richiesta:
   ```http
   Authorization: Bearer <token_jwt>
   ```

### Ruoli gestiti
* `ROLE_USER`: consulta il catalogo e gestisce i **propri** prestiti.
* `ROLE_ADMIN`: ha accesso completo: gestione di libri, utenti e prestiti.

### Scelte di sicurezza
* La **registrazione pubblica assegna sempre `ROLE_USER`**: il campo `roles` inviato nel body viene ignorato. I ruoli possono essere modificati solo da un admin tramite `PUT /api/user/{uuid}`.
* Il prestito viene sempre intestato all'**utente del token**: il body contiene solo il libro.
* Un utente normale vede solo i **propri** prestiti; gli altri risultano inesistenti (`404`).
* Il login risponde `401` allo stesso modo per username inesistente, password errata e utente disattivato.
* Le password sono salvate come hash BCrypt e non vengono mai restituite.
* Segreti (password DB e chiave JWT) fuori dal repository.

---

## 📦 Formato delle risposte

Le risposte sono avvolte in un wrapper standard:

```json
{ "status": "SUCCESS", "data": { } }
```

Gli errori usano il codice HTTP appropriato e lo stesso wrapper:

| Codice | `status` | Quando |
| :--- | :--- | :--- |
| `401` | `UNAUTHORIZED` | Credenziali non valide o token mancante/non valido |
| `403` | - | Utente autenticato ma senza il ruolo richiesto |
| `404` | `NOT_FOUND` | Risorsa inesistente |
| `409` | `CONFLICT` | Duplicati (ISBN, username, email), copie esaurite, prestito già restituito |

Lo stato di un prestito è `"Active"` oppure `"Returned"`.

---

## 📡 Riepilogo Endpoint API

### 🔑 Autenticazione (`/api/auth`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Pubblico | Autentica l'utente e restituisce il token JWT (`401` se errate) |

---

### 👤 Utenti (`/api/user`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/user` | Pubblico | Registra un utente con `ROLE_USER` (`409` se username o email esistono già) |
| `GET` | `/api/user` | `ROLE_ADMIN` | Restituisce la lista di tutti gli utenti |
| `GET` | `/api/user/{uuid}` | `ROLE_ADMIN` | Cerca un utente tramite UUID |
| `GET` | `/api/user/username/{username}` | `ROLE_ADMIN` | Cerca un utente tramite username |
| `PUT` | `/api/user/{uuid}` | `ROLE_ADMIN` | Aggiorna i dati o i ruoli di un utente (i campi vuoti non vengono modificati) |
| `DELETE` | `/api/user/{uuid}` | `ROLE_ADMIN` | Elimina un utente tramite UUID (`204`) |
| `DELETE` | `/api/user/username/delete/{username}` | `ROLE_ADMIN` | Elimina un utente tramite username (`204`) |

---

### 📖 Libri (`/api/book`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/book` | Pubblico | Restituisce tutti i libri in catalogo |
| `GET` | `/api/book/uuid/{uuid}` | Pubblico | Cerca un libro tramite UUID (`404` se assente) |
| `GET` | `/api/book/title/{title}` | Pubblico | Cerca un libro tramite titolo |
| `GET` | `/api/book/isbn/{isbn}` | Pubblico | Cerca un libro tramite codice ISBN |
| `POST` | `/api/book` | `ROLE_ADMIN` | Aggiunge un libro (`201`; `409` se l'ISBN esiste già) |
| `PUT` | `/api/book/{uuid}` | `ROLE_ADMIN` | Aggiorna un libro (`404`; `409` se l'ISBN appartiene a un altro libro) |
| `DELETE` | `/api/book/{uuid}` | `ROLE_ADMIN` | Elimina un libro (`204`; `404` se assente) |

---

### 🤝 Prestiti (`/api/borrow`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/borrow` | Autenticato | Admin: tutti i prestiti. Utente: solo i propri |
| `GET` | `/api/borrow/{uuid}` | Autenticato | Dettaglio di un prestito (un utente solo se è il suo) |
| `POST` | `/api/borrow` | Autenticato | Nuovo prestito per l'utente del token; decrementa di 1 le copie (`409` se non disponibili) |
| `PUT` | `/api/borrow/{uuid}/return` | Autenticato | Registra la restituzione (`Returned`) e incrementa di 1 le copie (`409` se già restituito) |
| `DELETE` | `/api/borrow/{uuid}` | `ROLE_ADMIN` | Elimina un prestito (`204`) |

#### Esempio: nuovo prestito

```http
POST /api/borrow
Authorization: Bearer <token_jwt>
Content-Type: application/json

{ "bookUuid": "uuid-del-libro" }
```

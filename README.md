# 📚 Gestionale Biblioteca - REST API (Spring Boot & JWT)

Backend RESTful sviluppato con **Spring Boot** per la gestione completa di una biblioteca: catalogo libri, anagrafica utenti con ruoli, prestiti/restituzioni e autenticazione stateless tramite **Spring Security & JSON Web Token (JWT)**.

---

## 🚀 Tecnologie Utilizzate

* **Java 17**
* **Spring Boot 4.1.1**
  * **Spring Web MVC** (REST API)
  * **Spring Data JPA / Hibernate** (Persistenza dati)
  * **Spring Security** (Autenticazione e Autorizzazione basata su ruoli)
* **JJWT (`io.jsonwebtoken` 0.12.6)** (Generazione, firma e validazione token JWT)
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
├── mapper/            # Conversione tra Entity e DTO (BookMapper, UserMapper, BorrowMapper)
├── models/            # Entità JPA (User, Role, Book, Borrow)
├── repositories/      # Interfacce Spring Data JPA (UserRepository, RoleRepository, BookRepository, BorrowRepository)
├── response/          # Wrapper standardizzato delle risposte API (ResponseAPI<T>)
├── security/          # Configurazione Spring Security e JWT (SecurityConfig, JwtService, JwtAuthenticationFilter, CustomUserDetailsService)
└── services/          # Logica di business e gestione transazioni (UserService, BookService, BorrowService)
```

---

## ⚙️ Configurazione e Avvio

### 1. Requisiti
* **JDK 17** o superiore
* **Maven 3.8+**
* **MySQL Server** attivo sulla porta `3306` con uno schema denominato `biblioteca` (contenente le tabelle `Users`, `Roles`, `user_roles`, `Books`, `borrows`).

### 2. Configurazione (`src/main/resources/application.properties`)
Verifica i parametri di connessione al database e le impostazioni JWT:

```properties
spring.application.name=Gestionale_Biblioteca
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Server
server.port=8082

# JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
```

### 3. Esecuzione
Avvia l'applicazione tramite Maven Wrapper o dal tuo IDE (Eclipse / STS / IntelliJ):

```bash
./mvnw spring-boot:run
```

Il server sarà in ascolto su: `http://localhost:8082`

---

## 🔐 Autenticazione e Sicurezza (JWT)

L'API utilizza una configurazione **Stateless** basata su token JWT:
1. L'utente effettua il login tramite `POST /api/auth/login` inviando `username` e `password`.
2. Il server verifica le credenziali tramite `CustomUserDetailsService` e `BCryptPasswordEncoder` e restituisce un **token JWT** firmato con algoritmo HMAC-SHA.
3. Per accedere agli endpoint protetti, il client deve includere il token nell'header HTTP di ogni richiesta:
   ```http
   Authorization: Bearer <token_jwt>
   ```

### Ruoli gestiti
* `ROLE_USER`: può consultare il catalogo libri e gestire i prestiti.
* `ROLE_ADMIN`: ha accesso completo, inclusa la creazione/modifica/eliminazione dei libri, la gestione degli utenti e la cancellazione dei prestiti.

---

## 📡 Riepilogo Endpoint API

### 🔑 Autenticazione (`/api/auth`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Pubblico | Autentica l'utente e restituisce il token JWT |

**Esempio Request (`POST /api/auth/login`):**
```json
{
  "username": "admin_test",
  "password": "password123"
}
```
**Esempio Response:**
```json
{
  "status": "SUCCESS",
  "data": "eyJhbGciOiJIUzM4NCJ9..."
}
```

---

### 👤 Utenti (`/api/user`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/user` | Pubblico | Registra un nuovo utente (password cifrata con BCrypt) |
| `GET` | `/api/user` | `ROLE_ADMIN` | Restituisce la lista di tutti gli utenti |
| `GET` | `/api/user/uuid/{uuid}` | `ROLE_ADMIN` | Cerca un utente tramite UUID |
| `GET` | `/api/user/username/{username}` | `ROLE_ADMIN` | Cerca un utente tramite username |
| `PUT` | `/api/user/update/{uuid}` | `ROLE_ADMIN` | Aggiorna i dati o i ruoli di un utente |
| `DELETE` | `/api/user/uuid/delete/{uuid}` | `ROLE_ADMIN` | Elimina un utente tramite UUID |
| `DELETE` | `/api/user/username/delete/{username}` | `ROLE_ADMIN` | Elimina un utente tramite username |

**Esempio Request (`POST /api/user`):**
```json
{
  "username": "mario.rossi",
  "email": "mario.rossi@email.it",
  "password": "password123",
  "roles": ["ROLE_USER"]
}
```

---

### 📖 Libri (`/api/book`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/book` | Pubblico | Restituisce tutti i libri in catalogo |
| `GET` | `/api/book/uuid/{uuid}` | Pubblico | Cerca un libro tramite UUID |
| `GET` | `/api/book/title/{title}` | Pubblico | Cerca un libro tramite titolo |
| `GET` | `/api/book/isbn/{isbn}` | Pubblico | Cerca un libro tramite codice ISBN |
| `POST` | `/api/book` | `ROLE_ADMIN` | Aggiunge un nuovo libro al catalogo |
| `PUT` | `/api/book/update/{uuid}` | `ROLE_ADMIN` | Aggiorna i dati di un libro esistente |
| `DELETE` | `/api/book/uuid/delete/{uuid}` | `ROLE_ADMIN` | Elimina un libro tramite UUID |
| `DELETE` | `/api/book/isbn/delete/{isbn}` | `ROLE_ADMIN` | Elimina un libro tramite ISBN |

**Esempio Request (`POST /api/book`):**
```json
{
  "title": "Il Signore degli Anelli",
  "author": "J.R.R. Tolkien",
  "isbn": "978-8845292613",
  "price": 25.50,
  "availableCopies": 5
}
```

---

### 🤝 Prestiti (`/api/borrow`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/borrow` | Autenticato (`USER` / `ADMIN`) | Restituisce la lista di tutti i prestiti |
| `GET` | `/api/borrow/{uuid}` | Autenticato (`USER` / `ADMIN`) | Cerca i dettagli di un prestito tramite UUID |
| `POST` | `/api/borrow` | Autenticato (`USER` / `ADMIN`) | Registra un nuovo prestito e decrementa di 1 le copie disponibili del libro |
| `PUT` | `/api/borrow/return/{uuid}` | Autenticato (`USER` / `ADMIN`) | Registra la restituzione del libro (`RETURNED`) e incrementa di 1 le copie disponibili |
| `DELETE` | `/api/borrow/uuid/delete/{uuid}` | `ROLE_ADMIN` | Elimina un prestito tramite UUID |

**Esempio Request (`POST /api/borrow`):**
```json
{
  "userUuid": "bf4c159f-dd39-157c-8000-000000000001",
  "bookUuid": "d1f792c0-c3f7-444e-8000-000000000002",
  "borrowDate": "2026-11-08"
}
```

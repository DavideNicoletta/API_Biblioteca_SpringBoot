# 📚 Gestionale Biblioteca - REST API (Spring Boot & JWT)

Backend RESTful sviluppato con **Spring Boot** per la gestione completa di una biblioteca: catalogo libri, anagrafica utenti con ruoli, prestiti/restituzioni e autenticazione stateless tramite **Spring Security & JSON Web Token (JWT)**.

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
├── mapper/            # Conversione tra Entity e DTO (BookMapper, UserMapper, BorrowMapper)
├── models/            # Entità JPA (User, Role, Book, Borrow)
├── repositories/      # Interfacce Spring Data JPA (UserRepository, RoleRepository, BookRepository, BorrowRepository)
├── response/          # Wrapper standardizzato delle risposte API (ResponseAPI<T>)
├── security/          # Configurazione Spring Security e JWT (SecurityConfig, JwtService, JwtAuthenticationFilter, CustomUserDetailsService)
└── services/          # Logica di business e gestione transazioni (UserService, BookService, BorrowService)
```

---

## 🔐 Autenticazione e Sicurezza (JWT)

L'API utilizza una configurazione **Stateless** basata su token JWT:
1. L'utente effettua il login tramite `POST /api/auth/login` inviando `username` e `password`.
2. Il server verifica le credenziali tramite `CustomUserDetailsService` e `BCryptPasswordEncoder` e restituisce un **token JWT** firmato.
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

---

### 🤝 Prestiti (`/api/borrow`)

| Metodo | Endpoint | Accesso | Descrizione |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/borrow` | Autenticato (`USER` / `ADMIN`) | Restituisce la lista di tutti i prestiti |
| `GET` | `/api/borrow/{uuid}` | Autenticato (`USER` / `ADMIN`) | Cerca i dettagli di un prestito tramite UUID |
| `POST` | `/api/borrow` | Autenticato (`USER` / `ADMIN`) | Registra un nuovo prestito e decrementa di 1 le copie disponibili del libro |
| `PUT` | `/api/borrow/return/{uuid}` | Autenticato (`USER` / `ADMIN`) | Registra la restituzione del libro (`RETURNED`) e incrementa di 1 le copie disponibili |
| `DELETE` | `/api/borrow/uuid/delete/{uuid}` | `ROLE_ADMIN` | Elimina un prestito tramite UUID |


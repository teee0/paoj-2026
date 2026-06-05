# Proiect Individual — Programare Avansată pe Obiecte în Java (2026)


## Etapa I — Modelare și implementare OOP

### 1. Definirea sistemului

## Proiect Bibliotecă Multimedia

### 1.1 Lista acțiunilor posibile în sistem (10 acțiuni)
1. Adaugă un item nou (Film / Muzică) în catalog.
2. Înregistrează un cititor nou.
3. Împrumută un item unui cititor.
4. Returnează un item.
5. Caută un item după titlu.
6. Listează tot catalogul (sortat alfabetic).
7. Grupează și afișează itemii după tipul lor.
8. Verifică disponibilitatea unui item.
9. Afișează istoricul/lista de împrumuturi active pentru un cititor.
10. Elimină un cititor din sistem.

### 1.2 Lista obiectelor din domeniu (8+ tipuri)
1. `Persoana` (abstractă)
2. `Autor`
3. `Cititor`
4. `Item` (abstractă)
5. `ItemAudiovizual`
6. `Film`
7. `ItemMuzica`
8. `Imprumut` (imutabil)

---

### 2. Implementare Java

#### 2.1 — Clase și OOP

- [x] Cel puțin **8 clase** care modelează obiectele definite la punctul 1
- [x] Atribute **`private`** sau **`protected`**, cu getteri/setteri acolo unde e necesar
- [x] Metode `toString()`, `equals()` și `hashCode()` suprascrise la cel puțin **2 clase**
- [x] Cel puțin **o ierarhie de moștenire** (`extends`) cu minim **2 niveluri**
- [x] Cel puțin **o clasă abstractă** sau **o interfață** folosită în ierarhie
  _(ex: clasă abstractă `Persoana` cu metodă abstractă `getRol()`)_
- [x] Cel puțin **o clasă imutabilă**: atribute `final`, fără setteri, inițializate complet în constructor
- [x] Cel puțin **2 excepții custom** aruncate și tratate în cod
  

#### 2.2 — Colecții

- [x] Cel puțin **2 tipuri diferite de colecții** (`List`, `Set`, `Map`, `Queue`, etc.)
- [x] Cel puțin **una sortată** — prin implementarea `Comparable` pe clasă sau prin `Comparator`
- [x] Cel puțin **un `Map`** folosit pentru indexare sau grupare

#### 2.3 — Servicii

- [x] Cel puțin **2 clase de serviciu** care expun operațiile sistemului
- [x] Fiecare serviciu implementat ca **Singleton** (constructor privat + metodă statică `getInstance()`)
- [x] Serviciile expun cel puțin operațiile: **adaugă, șterge, caută după id/nume, listează toate**
- [x] O clasă **`Main`** care apelează **toate cele 10 acțiuni** definite la punctul 1,
  demonstrând funcționarea completă a sistemului

#### 2.4 — Organizare și calitate

- [x] Codul organizat în **sub-pachete** logice:
  ```
  com.pao.proiect.<tema>/
  ├── model/        ← clasele de domeniu
  ├── service/      ← serviciile singleton
  ├── exception/    ← excepțiile custom
  └── Main.java
  ```
- [x] Fără cod duplicat — logica comună extrasă în metode sau clase de bază
- [x] Fără `NullPointerException` la rulare — validează inputurile în servicii

---

## Criterii de evaluare

### Etapa I — 12 puncte din 25

| Criteriu                                                         | Punctaj |
|------------------------------------------------------------------|---------|
| README: 10 acțiuni + 8 tipuri de obiecte                         | 1p      |
| ≥8 clase cu atribute private, getteri/setteri, `toString`        | 2p      |
| Ierarhie moștenire (≥2 niveluri) + clasă abstractă / interfață   | 2p      |
| Clasă imutabilă + ≥2 excepții custom                             | 1p      |
| ≥2 colecții diferite (una sortată) + ≥1 Map                      | 2p      |
| ≥2 servicii Singleton cu operații CRUD în memorie                | 2p      |
| `Main` demonstrativ care apelează toate cele 10 acțiuni          | 1p      |
| Organizare în pachete, fără duplicat, fără NPE                   | 1p      |
| **Total Etapa I**                                                | **12p** |

---

## Etapa II — Persistență JDBC, Tranzacții și Audit

### Implementare

- [x] `schema.sql` + `db.properties` + `DatabaseConnection` (Singleton)
- [x] Interfață generică `Repository<T, ID>` în `repository/`
- [x] CRUD JDBC pentru `Autor`, `Cititor`, `Item`, `Imprumut`
- [x] `PreparedStatement` + `try-with-resources` pentru toate interogările
- [x] Tranzacții JDBC (`commit` / `rollback`) la operații multi-tabel
- [x] ≥3 interogări cu `JOIN` (împrumuturi active, cititori cu număr împrumuturi, top iteme)
- [x] `AuditService` thread-safe → scrie în `audit.csv` (append)

### Rulare locală (SQLite)

1. Driverul SQLite JDBC este în `lib/sqlite-jdbc-3.46.1.0.jar` — adaugă-l în classpath (IntelliJ: Add as Library)
2. Rulează `com.pao.project.Main` din rădăcina proiectului (`paoj-2026/`)
3. Baza de date se creează/reinițializează automat în `data/paoj_proiect.db`
4. Fișierul de audit: `audit.csv` în directorul de lucru curent

**Terminal (macOS/zsh):**
```bash
# ești deja în folderul proiectului — NU mai face cd paoj-2026
CP="lib/sqlite-jdbc-3.46.1.0.jar:lib/slf4j-api-2.0.16.jar:lib/slf4j-nop-2.0.16.jar"
mkdir -p out
javac -cp "${CP}:src" -d out $(find src/com/pao/project -name "*.java")
java -cp "${CP}:out:src" com.pao.project.Main
```

> **Important (zsh):** folosește `"${CP}:out:src"`, nu `"$CP:out:src"` — altfel zsh dă `bad substitution`.

### Etapa II — 13 puncte din 25

| Criteriu                                                              | Punctaj |
|-----------------------------------------------------------------------|---------|
| `schema.sql` complet (PK, ≥2 FK) + `db.properties` + `DatabaseConnection` singleton | 1p      |
| Interfață generică `Repository<T, ID>`                                | 1p      |
| CRUD complet pentru ≥4 entități                                       | 4p      |
| `PreparedStatement` + `try-with-resources`                          | 2p      |
| ≥1 tranzacție JDBC explicită                                          | 2p      |
| ≥3 interogări SQL cu `JOIN`                                           | 2p      |
| `AuditService` CSV thread-safe, apelat din toate cele 10 acțiuni      | 1p      |
| **Total Etapa II**                                                    | **13p** |

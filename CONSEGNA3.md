# Consegna 3 — Il Punteggio del compagno e i livelli (laboratorio 1)

**Scadenza: domenica 8 novembre 2026, alle 23:59.** Si lavora nel vostro repository `lab1-…`, quello con il `Punteggio` comprato o sorteggiato. Conta com'è il repository a quell'ora: se i test automatici sono verdi **e** resta almeno metà delle righe del codice di partenza, prendete un gettone.

Alla fine Pong diventa un gioco a livelli: voi contro il giocatore automatico, che a ogni livello sbaglia meno, con la palla che parte un po' più veloce. In ogni scambio, ogni quattro colpi di racchetta la palla accelera. Avete tre vite: perdendo una partita se ne perde una. La classe che tiene il conto dei livelli e delle vite la scrivete voi.

## Prima cosa da fare

Estraete `L08-consegna3.zip` (su Teams) e copiate nel vostro repository `lab1-…`:

- `CONSEGNA3.md`, questo file, nella cartella principale;
- `test/pong/ConsegnaTreTest.java`, i 28 test nuovi, nella cartella `test/pong`;
- i file della cartella `src` dello zip nella cartella `src` del repository, **sopra** quelli che ci sono: `Livelli.java` è nuovo (i contratti ci sono, i corpi dei metodi no); `Partita.java`, `Schermo.java`, `Palla.java`, `Main.java` e `GiocatoreAutomatico.java` sono le versioni con i livelli, già pronte.

I 29 test della consegna 1 (`test/pong/PunteggioTest.java`) restano: devono passare anche loro.

Finché nel `Punteggio` manca `repOK()`, i test non compilano. Scrivete subito il metodo con un corpo provvisorio:

```java
boolean repOK() {
    return true; // da scrivere
}
```

Così i test compilano, e quelli che mancano sono rossi.

## Cosa dovete fare

### 1. Le eccezioni per gli argomenti sbagliati (v. lezione 7)

Il contratto del `Punteggio` cambia: dove la consegna 1 diceva «Pretende», ora il metodo controlla e lancia. Scrivete i contratti nuovi nei commenti, al posto dei vecchi:

- **costruttore** `Punteggio(int puntiPerVincere, int scartoMinimo)` — Pretende: niente. Promette: come prima. Lancia `IllegalArgumentException` se `puntiPerVincere` è minore di 1 o `scartoMinimo` è minore di 1.
- **`segnaPunto(Lato chi)`** — Pretende: niente. Promette: come prima. Se `chi` è null lancia `IllegalArgumentException`, anche a partita finita, e non cambia niente.
- **`getPunti(Lato chi)`** — Pretende: niente. Promette: come prima. Lancia `IllegalArgumentException` se `chi` è null.

Attenzione: con `chi` null deve essere proprio `IllegalArgumentException`. `Objects.requireNonNull` lancia `NullPointerException`, e i test lo considerano sbagliato.

### 2. `repOK()` (v. lezione 6)

`repOK` è il nome che si trova nei libri (Liskov e Guttag) per il metodo che nella lezione 6 si chiamava `rispettaInvariante`: è lo stesso metodo. Un metodo `boolean repOK()`, senza `public`, che risponde vero se lo stato del `Punteggio` ha senso: l'invariante della rappresentazione che avete, cioè dei campi del codice che vi è toccato. Scrivete l'invariante come commento sopra il metodo.

Qualche proprietà possibile: nessun elemento null nella storia; i punti di ciascun lato sono quanti compaiono nella storia (se ci sono campi con i punti); c'è un vincitore se e solo se, contando la storia, un lato ha vinto; nella storia non ci sono punti dopo quello che ha chiuso la partita. Se il codice tiene solo la storia, restano le proprietà sulla storia.

I test chiamano `repOK` dopo molte chiamate diverse: un `repOK` troppo severo (falso su uno stato che ha senso) li fa diventare rossi. Uno troppo largo (per esempio `return true`) i test non lo vedono: sta a voi scriverlo per intero.

### 3. I livelli: la classe `Livelli`

In `src/pong/Livelli.java` scrivete i campi e i corpi dei metodi. I contratti sono nei commenti, sopra ogni metodo: non cambiateli. In breve:

- un gioco nuovo comincia dal livello 1, con tutte le vite;
- se vince la persona (il lato `umano`), si passa al livello dopo; vinto l'ultimo livello, il gioco è completato e il livello resta l'ultimo;
- se vince il giocatore automatico, si perde una vita e si rigioca lo stesso livello; senza vite, il gioco è perso;
- a gioco finito (completato o perso), `partitaFinita` non cambia più niente;
- `ricomincia` riporta al livello 1 con tutte le vite.

Un esempio con 3 livelli e 2 vite, la persona a sinistra:

| Com'è finita la partita | Livello dopo | Vite dopo | Il gioco |
| --- | --- | --- | --- |
| (gioco nuovo) | 1 | 2 | in corso |
| ha vinto il giocatore a sinistra | 2 | 2 | in corso |
| ha vinto il giocatore a destra | 2 | 1 | in corso |
| ha vinto il giocatore a sinistra | 3 | 1 | in corso |
| ha vinto il giocatore a sinistra | 3 | 1 | completato |
| ha vinto il giocatore a destra | 3 | 1 | completato: non cambia niente |

Leggete i contratti con attenzione: i casi in cui ci si sbaglia sono all'ultimo livello e all'ultima vita.

### 4. Nel gioco (non lo controllano i test)

Con `Livelli` scritta, lanciate `Main` e giocate: voi a sinistra con le frecce SU e GIÙ, il giocatore automatico a destra. In basso si vedono il livello e le vite. Se qualcosa nel gioco non torna (un livello saltato, una vita in più), cercate il motivo con il debugger. Una volta trovato, scrivete in `test/pong/MieiTest.java` un test che lo mostra, prima rosso, poi verde dopo la correzione. I vostri test contano: se uno è rosso alla scadenza, la consegna non è verde.

I numeri del gioco (a quanti punti si gioca, quanto va veloce la palla e quanto accelera, quanto sbaglia il giocatore automatico) sono le costanti in cima a `Partita.java`, `Palla.java` e `GiocatoreAutomatico.java`: potete cambiarli per rendere il gioco più bello.

## La regola della metà

Alla scadenza deve restare almeno metà delle righe del codice di partenza: i file `.java` fuori dalla cartella `test`, senza righe vuote e commenti; una riga cambiata conta come tolta. Lo controlla il mercato. Si parte dal codice del compagno: correggere e aggiungere sì, riscrivere da zero no. I file con i livelli che copiate dallo zip cambiano una decina di righe del codice di partenza: ne resta molto più della metà.

## Come si prova e come si consegna

In VS Code, barra **Testing**: devono essere verdi i test di `PunteggioTest`, `ConsegnaTreTest` e i vostri. Su GitHub, a ogni `git push` partono i test ufficiali: GitHub scarica quelli della consegna 3 dal repository pubblico del corso (`poo-uniud-2627/test-ufficiali`, cartella `lab1`) e li copia sopra i file con lo stesso nome nella vostra cartella `test`. Cambiarli nel vostro repository quindi non serve; i vostri `MieiTest` restano e contano. Si consegna con git, come le altre volte.

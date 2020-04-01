# Movie Showcase - Analisi

Movie Showcase è un applicazione che dà la possibilità all'utente di visualizzare una libreria di film, dare un rating, creare preferiti e nascondere

## Backend
I film verranno scaricati utilizzando un servizio di terze parti, [TheMovieDB](https://www.themoviedb.org), il quale mette a disposizione vari dati riguardanti molti film, I dati potrebbero essere strippati per eliminare informazioni non necessarie

Si sceglie di usare TMDB, perchè la documentazione è più veloce da capire e consultare rispetto ad altri servizi

## Schermate

#### Schermata principale

Mostra una lista di film ed ha le seguenti funzionalità

+ Le celle della lista possono essere modificate utilizzando diversi stili (griglia e lista)
+ Ogni item della lista ha le seguenti opzioni
    + Rating, al click viene mostrato un alert dove si può dare un rating a stelle
    + Preferito, al click viene cambiato lo stato di preferito sull'item
    + Nascosto, al click viene nascosto l'item
+ La toolbar di questa schermata ha le seguenti opzioni
    + Mostra/Nascondi preferiti
    + Mostra/Nascondi film adulti
    + Mostra/Nascondi film nascosti
    + Refresh
    + Impostazioni

La lista viene gestita con una recyclerview, per questioni di performance e funzionalità, la lista inoltre avrà la funzionalità di swipe-to-refresh, la quale terrà conto dell'item a cui si è arrivati

Lo scrolling sarà "infinito", raggiunta la fine o un punto vicino alla fine, verrà scaricata la prossima pagina di film

#### Impostazioni
    
+ Data
    + Lingua film, si può cambiare la lingua e metterla diversa dal Locale corrente
    + Mostra film nascosti, mostra activity dove si possono togliere dalla black list
    + Clear data
+ About
    + Developer
    + Source
    + Licenses
    + GDPR

## Librerie

#### Caricamento immagini

Per il caricamento immagini si decide di usare Picasso, dato che è facile e veloce da usare

#### Networking

Per il fetching dei dati viene usato Retrofit, essendo scritta dagli stessi sviluppatori di Picasso ed una soluzione di terze parti con più personalizzazione di quelle fornite da Google

#### Persistence

Viene usato SQLite per la persistenza dei dati sui film, sarà creata una classe singleton che si occuperà di rendere disponibile i dati del db e di aggiornali se necessario

## Tasks

+ Creazione modelli, basandosi sui dati del servizio
+ Creazione db e conversione db => modelli, json => db, modelli => db
+ Creazione lista/adapter
+ Fetch dati
+ Aggiornamento dati locali
+ Funzionalità aggiuntive lista (preferiti, nascondi, rating)

## Workflows

#### Avvio app

+ App viene aperta
+ Dati caricati dal db, se ci sono
+ Dati scaricati dalla rete ed aggiunti al db
+ Aggiornamento manuale da parte dell'utente (o automatico se il db è vuoto)

#### Scroll lista

+ Utente scrolla
+ Nuovi dati vengono scaricati e aggiunti al db
+ Lista viene aggiornata

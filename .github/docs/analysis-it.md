# Movie Showcase

Movie Showcase è un'applicazione Android nativa che permette di visualizzare una collezione di film estratti dal servizio TheMovieDB, questi film possono essere valutati e messi nei preferiti localmente.

## Recupero dati film
I film verranno scaricati utilizzando un servizio di terze parti, [TheMovieDB](https://www.themoviedb.org), il quale mette a disposizione vari dati riguardanti molti film.  
I dati potrebbero essere strippati per eliminare informazioni non necessarie

Si sceglie di usare TMDB, perchè la documentazione è più veloce da consultare ed è più chiara rispetto ad altri servizi

## Fetch e parse JSON 

I dati saranno scaricati usando la libreria [Kon](https://github.com/maxpilotto/kon), la quale è stato sviluppata internamente e si occupa anche del parsing dei dati

Rispetto alla libreria standard inclusa nell'SDK di Android questa mette a disposizione:

+ Support per i tipi URL, IntRange, Enum, Date e Calendar
+ Parsing automatico di un modello Java/Kotlin
+ Fetch di risorse JSON dalla rete

## Lista film

La lista viene gestita con una RecyclerView, per questioni di performance e funzionalità

Lo scrolling sarà "infinito", raggiunta la fine o un punto vicino alla fine, verrà scaricata la prossima pagina di film e/o caricato da locale il gruppo di "nuovi" film

## Image loading and caching

Per caricare le immagini nelle varie cella si decide di usare [Glide](https://github.com/bumptech/glide), perchè risulta più aggiornata della controparte Picasso

## Persistence

I dati, non appena scaricati, saranno memorizzati nel db interno all' app, usando SQLite, senza nessun framework

Se un dato è già presente nel db, i dati di questo verranno sovrascritti con quelli più aggiornati, ad eccezzione dei valori che non arrivano dalla rete, come lo stato di preferito o il rating (quello locale, non quello della piattaforma)

Sarà anche inoltrato un content provider

## Schermate

#### Schermata principale

Questa schermata si occupa della visualizzazione della lista dei film, del rating e del settaggio dei preferiti

Ogni item sulla lista ha le opzioni per:

+ Rating, al click viene mostrato un alert dove si può dare un rating a stelle
+ Preferito, al click viene cambiato lo stato di preferito sull'item

#### Schermata dettaglio

Questa schermata si occupa della visualizzazione dei dati di un film, tra cui data di rilascio, valutazione utenti, valutazione personale, descrizione film e un immagine che fa da cover

La schermata dettaglio riceverà l'id del film tramite un extra passato nell'intent di creazione della nuova activity, quest'ultima ricaricherà dal db i dati del film

## Funzionalità extra

+ Supporto rotazione, diversi layout per landscape e portrait
+ Endless scroll, i film si potranno scrollare in modo "infinito"
+ Temi light/dark, l'app permette di passare da tema chiaro a scuro e viceversa

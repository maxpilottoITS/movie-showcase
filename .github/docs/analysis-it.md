# Movie Showcase - Analisi

Movie Showcase è un applicazione che dà la possibilità all'utente di visualizzare una libreria di film, dare un rating, creare preferiti e nascondere

## Backend
I film verranno scaricati utilizzando un servizio di terze parti, [TheMovieDB](https://www.themoviedb.org), il quale mette a disposizione vari dati riguardanti molti film, I dati potrebbero essere strippati per eliminare informazioni non necessarie

Si sceglie di usare TMDB, perchè la documentazione è più veloce da consultare ed è più chiara rispetto ad altri servizi

## Schermata principale

Mostra una lista di film ed ha le seguenti funzionalità di base

+ Lista implementata con RecyclerView
+ Ogni item della lista ha le seguenti opzioni
    + Rating, al click viene mostrato un alert dove si può dare un rating a stelle
    + Preferito, al click viene cambiato lo stato di preferito sull'item
+ Mostra schermata senza dati
+ Rotazione

Funzionalità extra

+ Le celle della lista possono essere visualizzate utilizzando diversi stili (griglia e lista)
+ La toolbar di questa schermata ha le seguenti opzioni
    + Mostra/Nascondi preferiti
    + Mostra/Nascondi film nascosti
    + Refresh
    + Impostazioni
+ Pull to refresh
+ Infinite scroll

#### Gestione lista 

La lista viene gestita con una RecyclerView, per questioni di performance e funzionalità, la lista inoltre avrà la funzionalità di swipe-to-refresh, la quale terrà conto dell'item a cui si è arrivati

Lo scrolling sarà "infinito", raggiunta la fine o un punto vicino alla fine, verrà scaricata la prossima pagina di film

#### Image loading and caching

Per caricare le immagini nelle varie cella si decide di usare [Glide](https://github.com/bumptech/glide), perchè non è mai stata usata ed è più aggiornata rispetto a Picasso

#### Json parsing

I dati verranno scaricati e convertiti usando la libreria [Kon](https://github.com/maxpilotto/kon), perchè sviluppata internamente

#### Persistence

I dati, non appena scaricati, saranno memorizzati nel db interno all' app, usando SQLite, senza nessun framework

Se un dato è già presente nel db, i dati di questo verranno sovrascritti con quelli più aggiornati, ad eccezzione dei valori che non arrivano dalla rete, come lo stato di preferito o il rating (quello locale, non quello della piattaforma)

## Schermata dettaglio

La schermata dettaglio riceverà l'id del film tramite un extra passato nell'intent di creazione della nuova activity, quest'ultima ricaricherà dal db i dati del film

Funzionalità extra
+ Preferito, Nascosot e Rating direttamente da questa schermata

## Impostazioni
    
+ Data
    + Lingua film, si può cambiare la lingua e metterla diversa dal Locale corrente
    + Mostra film nascosti, mostra activity dove si possono togliere dalla lista dei nascosti
    + Clear data
+ About
    + Developer
    + Source
    + Licenses
    + GDPR

## Librerie

+ [Glide](https://github.com/bumptech/glide)
+ [Kon](https://github.com/maxpilotto/kon)

## Tasks

+ Creazione modelli, basandosi sui dati del servizio
+ Creazione db e conversione db => modelli, json => db, modelli => db
+ Creazione lista/adapter
+ Fetch dati
+ Aggiornamento dati locali
+ Funzionalità aggiuntive lista (preferiti, nascondi, rating)

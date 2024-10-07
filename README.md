# ProgettoIngSW
Repository collaborativa per il progetto Loot Market dedicato all'esame di Ingegneria del Software 2023/2024


TODO
1) Decidere se, dalla home, con il tasto "indietro" si deve andare alla pagina di accesso (se l'app non ricorda le credenziali) oppure se chiudere l'applicazione

Frontend To-Do:
1) Se un utente ha fatto un'offerta per una determinata asta NON si può disiscrivere
2) Lo storico riceve TUTTE le iscrizioni, e sarà il frontend a smistarle nelle 3 categorie
3) Gestire l'impossibilità di disiscrizione ad un'asta se si è presentata anche solo un'offerta

Backend to-do:
1) se l'ultima offerta è fatta da te, esce nella pagina "dettagli asta"
2) nell'area utente terzi, il backend dovrà mandare all'app solo le aste non scadute
3) nell'area utente terzi, il backend dovrà ricevere dall'app l'id del profilo che l'utente sta per visitare e l'id dell'utente per vedere se ha fatto qualche offerta alle aste ceh vedrà comparire
4) nella pagina Auction details se l'id utente loggato è uguale all'id dell'utente dell'auction che si sta visualizzando (se sto vedendo una mia asta) devono disattivarsi i tasti di iscrizione e offerta da presentare all'asta

Scelte implementative Backend:
1) Le aste conservano una stringa contenente una lista di id di utenti iscritti, così come alla stessa maniera gli utenti hanno una stringa contenente una lista di id di aste a cui sono iscritti

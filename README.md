# À propos de l'application PMB

* Licence : [AGPL v3](http://www.gnu.org/licenses/agpl.txt) - Copyright CGI, Région Nouvelle Aquitaine
* Financeur(s) : CGI, Nouvelle Aquitaine
* Développeur(s) : CGI
* Description : module permettant la connexion à PMB.

# Présentation du module 

PMB est un outil qui regroupe les références des documents disponibles au CDI.
Il donne des renseignements pour accéder aux supports réels (livre, magazine, DVD, CD…)
et des liens pour accéder aux supports virtuels (sites internet et livres numériques).
Il permet également la réservation des ressources du CDI ainsi que leur suivi.

## Configuration

<pre>
{
  "config": {
    ...
    "infraMail": "${infraMailPmb}",
    "PMB": {
        "host": "${pmbServer}",
        "endpoint": "${pmbEndpoint}",
        "db_prefix": "${pmbDBPrefix}",
        "page_size": ${pmbPageSize},
        "credentials": {
            "username": "${pmbUsername}",
            "password": "${pmbPassword}"
        }
    }
  }
}
</pre>

Dans votre springboard, vous devez inclure des variables d'environnement :
<pre>
infraMailPmb = ${String}
pmbServer = ${String}
pmbEndpoint = Integer
pmbDBPrefix = ${String}
pmbPageSize = Integer
pmbUsername = ${String}
pmbPassword = ${String}
</pre>

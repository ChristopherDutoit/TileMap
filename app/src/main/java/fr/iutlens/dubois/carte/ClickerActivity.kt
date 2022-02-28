package fr.iutlens.dubois.carte

//Ici tout les imports, voila
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlin.random.Random

class ClickerActivity : AppCompatActivity() {
    /*
    * -----------------------------------------------------------
    * Variables mini-jeux
    *   Mini-jeu 1
    * -----------------------------------------------------------
    */
    //La variable ajout est en gros la valeur du vertical bias de la flèche. Cette dernière commence à 0.85, ce qui correspond au rouge sur la barre
    private var ajout=0.85f
    //Variable qui ne sert à rien pour l'instant, mais on la garde car on sais jamais
    private var timer: Int=1
    //La variable diff est la valeur avec laquelle le vertical bias vas être ajouté, en gros "la force du click".
    private var diff=0.0015f


    /*
    * -----------------------------------------------------------
    * Variables mini-jeux
    *   Mini-jeu 2
    * -----------------------------------------------------------
    */
    //A la même fonction que ajout dans le mini-jeu 1. Le vertical bias commence à 0.99, tout en bas de la barre
    private var ajout_mj2=0.99f
    //La variable winCond qui permet de déterminer
    private var winCond: Int=1

    /*
     * -----------------------------------------------------------
     * Variables mini-jeux
     *   Mini-jeu 3
     * -----------------------------------------------------------
     */


    //Détermination de quelle variante du clicker vas être jouée.
    //Le nombre de variantes au clicker est 3 mais peut être augmenté tant que l'on alimente le when et que l'on crée la fonction adéquate
    private var minrdm: Int=1
    private var maxrdm: Int=3

    private var res: Int=rdm(minrdm,maxrdm)

    override fun onCreate(savedInstanceState: Bundle?) {
        title="Clique pour éviter de dormir !"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)

        //Ici un when pour facilement déterminer quel variante vas être mise. J'ai fais ce système pour éviter que le code ne ressemble à rien avec tous les commentaires.
        when (res) {
            1->minijeu1()
            2->minijeu2()
            //3->minijeu3() Pas disponible
        }
    }

    /*
    * -----------------------------------------------------------
    * Fonctions des mini-jeux (Fonctions principales), vitales
    * -----------------------------------------------------------
    */

    //Mini-jeu 1: Résister à la fatigue en cliquant le plus vite possible pendant 10 secondes. Plus le temps passe, plus cela deviens dur à passer
    private fun minijeu1() {
        //Variable adder pour trouver le bouton...
        val adder: Button = findViewById(R.id.bout)
        //...pour ensuite ajouter un écouteur dessus. Un écouteur qui vas écouter les clicks sur le bouton
        adder.setOnClickListener {
            //On ajoute un if pour checker le vertical bias de la flèche. Pour rappel, ajout correspond au vertical bias de la flèche
            //Si ajout est compris entre 0.02 et 1, alors on peut clicker pour faire baisser le verticalbias de la flèche.
            if (ajout>=0.02 && ajout<1) {
                //On fais monter la flèche en faisant baisser le vertical bias de cette dernière
                ajout-=0.02f
                //La fonction est plus en-dessous.
                progres(ajout)
            }
            //Si la flèche est trop haute (ajout>0.02) alors on la laisse descendre.
            //La flèche ne remonte plus quand on clicke tant que ajout est au-dessus de 0.02.
        }

        //Voici maintenant la fonction qui fait baisser la flèche de manière continue
        //la valeur mainHandler est une fonction qui permet de faire des bloucles, mais à l'infini puisqu'il n'y a pas de conditions de fin comme on peut trouver sur un for ou sur un while.
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                //On effectue une délay toutes les 10 milisecondes, et donc d'effectuer une action toutes les 10 milisecondes.
                mainHandler.postDelayed(this, 10)
                //Ici iun findviewbyid pour débeuger et pour voir la valeur du vertical bias
                findViewById<TextView>(R.id.test1).text=ajout.toString()
                if (ajout>=1) { //Si le vertical bias est en-dessous de 1 (=si la flèche est trop basse)
                    //Alors on ne peut plus clicker et le titre change.
                    title="Perdu !"
                } else {
                    //Si on est toujours dans les conditions de victoire (la flèche n'est pas trop basse)
                    //Alors on diminue le vertical bias de la flèche pour la faire monter
                    ajout+=diff
                    //Plus de détails sur la fonction en-bas
                    progres(ajout)
                }
            }
        })

        /*"Mais pourquoi on check sur 2 méthodes différentes si la flèche est trop basse ? (sur le click et toutes les 10 ms)"
        * Car la première méthode chècke si on peut faire une action quand on clicke, et si on suit cette logique cela veut dire que l'on ne peut jamais perdre si la flèche est trop basse et qu'on le clique pas.
        * Et c'est donc pourquoi on a ajouté une seconde méthode qui, en plus de faire continuellement baisser la flèche, check si cette dernière n'est pas trop basse et donc blocker ou non l'action de baiser le vertical bias de flèche.*/

        //Ici le timer que on vas utiliser pour faire afficher le temps restant au clicker. Le timer sure 10 secondes. Les temps ici sont mesuérés avec des ms et donc (temps en ms)/1000 donne le temps en secondes.
        object : CountDownTimer(10000, 1000) {
            //Chose important à noter: la varibale "millisUntilFinished" est la variable qui permet de dire en ms combien de temps il reste au timer.
            override fun onTick(millisUntilFinished: Long) {
                //On cherche le texte qui s'appelle "remaining" pour lui faire afficher le nombres de secondes restantes. Si vous voulez faire changer la textView, touchez pas à "millisUntilFinished / 1000", sinon ça casse tout.
                findViewById<TextView>(R.id.remaining).setText("Il reste " + millisUntilFinished / 1000 + "secs")
                //findViewById<TextView>(R.id.test2).text=diff.toString()
                //Ce bout de code avec les if/else if sert à augmenter la vitesse de la flèche qui descend en fonction du temps. On vas faire un exemple avec le premier:
                //Si il reste entre 9s et 6s, alors la flèche voir son vertical bias modifié de 0.001 toutes les 10 ms.
                //Toutes les 10 ms car c'est ce qu'on a décidé au-dessus avec le handleur et le délai.
                //Etc, etc...
                if (millisUntilFinished<9000 && millisUntilFinished>=6000) {
                    diff=0.001f
                } else if (millisUntilFinished<6000 && millisUntilFinished>=5000) {
                    diff=0.002f
                } else if (millisUntilFinished<5000 && millisUntilFinished>=3000) {
                    diff=0.0035f
                } else if (millisUntilFinished<3000 && millisUntilFinished>=1500) {
                    diff=0.0045f
                } else if (millisUntilFinished<1500 && millisUntilFinished>=0) {
                    diff=0.006f
                }
            }

            //Ici une fonction pour dire ce qu'on vas faire quand le timer est fini. Idéalement retourner sur la carte mais j'ai tenté mais ça marche pas :/.
            override fun onFinish() {
                /* Pour check si le done marche
                findViewById<TextView>(R.id.remaining).setText("done!")*/
                //Update: Je sais pas trop comment
            }
            //.start pour commencer le timer.
        }.start()
    }



    //Mini-jeu dans lequel on doit tapper le plus dans un temps imparti pour coder quelque chose. On vas utiliser le même système que le mini-jeu 1
    private fun minijeu2() {
        //Première chose à faire est d'initialiser où est-ce-que la flèche noire vas se trouver dès le chargement, et ça on le fais avec progress et on le met à 0.99 car cela correspond à la flèche étant tout en bas de la barre
        progres(0.99f)
        //Ligne pour débeuger, voir si adder marche bien findViewById<TextView>(R.id.test1).setText(ajout_mj2.toString())
        //Ici une ligne pour faire disparaître le texte, car nous n'en n'avons pas besoin.
        //findViewById<TextView>(R.id.test1).setText("")
        title="Clique pour coder un maximum !"
        //On demande à trouver le bouton...
        val adder: Button=findViewById(R.id.bout)
        //...pour y ajouter un écouteur qui vas détecter les clicks
        adder.setOnClickListener {
            if (ajout_mj2>0.02 && winCond==1) {
                //Si la flèche n'est pas tout en haut de la barre, alors on a la possibilité de la faire baisser
                ajout_mj2-=0.01f
                progres(ajout_mj2)
            }
        }

        //Partie du timer. C'est le même système que le premier mini-jeu: un timer qui dure 12secondes et qui se met à jour toutes les 10ms.
        object : CountDownTimer(12000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                //Quand le timer est en train de tourner:
                    //On met à jour le texte qui indique le temps restant
                findViewById<TextView>(R.id.remaining).setText("Il reste " + millisUntilFinished / 1000 + "secs")
                    //On met à jour continuellement le score pour éviter des bugs type le dernier clic n'est pas pris en compte.
                findViewById<TextView>(R.id.test1).setText("Score: "+(100-(ajout_mj2*100)).toInt()+"/100")
            }

            //Ici on affiche le score final. Chose à ajouter: après x secondes, retourner à la carte. Pendant ce délay des X secondes, afficher en grand le score final
            override fun onFinish() {
                //Je met à jour le titre et le textView pour afficher le score final.
                title="Score final: "+(100-(ajout_mj2*100)).toInt()+"/100"
                findViewById<TextView>(R.id.test1).setText("Score final: "+(100-(ajout_mj2*100)).toInt()+"/100")
                //Je met à jour la winCondition, pour éviter que le joueur puisse clicker et modifier son score final même quand le timer est fini
                winCond=0
                //Et je fais disparaitre le timer puisqu'il ne sert plus à rien ici.
                findViewById<TextView>(R.id.remaining).setText("")
            }
        }.start()

    }

    private fun minijeu3() {
        //Pas d'idée de mini-jeu, si tu en as une envoie à Félix un MP
    }

    /*
    * -----------------------------------------------------------
    * Fonctions diverses mais toujours vitales
    * -----------------------------------------------------------
    */

    private fun rdm(min:Int,max:Int): Int {
        return (min..max).random()
    }

    //Et la fonction pour à la fois récupérer la valeur du vertical bias de la flèche mais aussi pour faire aumgmenter/diminuer le vertical bias.
    //La fonction prend en paramètre un nombre flotant, car le vertical bias est une valeur comprise entre 0 et 1.
    //FONCTION TRES IMPORTANTE, A NE PAS SUPR SOUS PEINE DE DEVOIR PORTER UN SOMBRERO LE 21 FEVRIER.
    private fun progres(fl: Float) {
        //On crée un nouveau constrainSet
        val set = ConstraintSet()
        //On demande de trouver la flèche (findViewById) et de récupérer toutes ses contraintes (bottomOf, topOf, Parent, etc...). Ce qui nous intéresse ici c'est le vertical bias.
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraint_layout)
        //On fais un clone temporaire de la flèche avec toutes ses contraintes
        set.clone(constraintLayout)

        //On fais ce que l'on veut au clone de la flèche. Ici, on vas modifier son vertical bias et vas prendre en valeur fl qui est la veleur que le Vertical bias vas prendre.
        set.setVerticalBias(R.id.dark_arrow_clicker, fl)
        //Et enfin on remplace la flèche par son clone avec toutes les nouvelles modifications
        set.applyTo(constraintLayout)
    }

    fun onClickAccueil(view: android.view.View) {
        val intent = Intent(this, AccueilActivity::class.java)
        startActivity(intent)
    }
}
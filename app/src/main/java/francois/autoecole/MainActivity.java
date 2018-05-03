package francois.autoecole;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txtEmail, txtMdp;
    private Button btAnnuler, btSeConnecter;
    private static Candidat candidatConnected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       this.txtEmail =(EditText) findViewById(R.id.idEmail);
       this.txtMdp =(EditText) findViewById(R.id.idMdp);
       this.btAnnuler =(Button) findViewById(R.id.idBtAnnuler);
       this.btSeConnecter =(Button) findViewById(R.id.idBtCo);
       this.btAnnuler.setOnClickListener(this);
       this.btSeConnecter.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.idBtAnnuler:
                this.txtMdp.setText("");
                this.txtEmail.setText("");
                break;

            case R.id.idBtCo: {
                String email = this.txtEmail.getText().toString();
                String mdp = this.txtMdp.getText().toString();
                final Candidat unCandidat = new Candidat(email, mdp, "", "", "");

                //sauvegarde de la reference this dans l'objet ma
                final MainActivity ma = this;


                Thread unT = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        //instanciation de la classe connexion
                        Connexion unConnexion = new Connexion();
                        //execution de la tache asynchrone
                        unConnexion.execute(unCandidat);
                        //test de passage a la page suivante
                        try{
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException exp)
                        {
                            Log.e("Erreur", "attente");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (candidatConnected == null) {
                                    Toast.makeText(ma, "Veuillez vérifiez vos identifiants", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ma, "Bienvneue M." + candidatConnected.getNom()
                                            + " " + candidatConnected.getPrenom(), Toast.LENGTH_SHORT).show();
                                    Intent unIntent = new Intent(ma, MesCours.class);
                                    unIntent.putExtra("eamil", candidatConnected.getEmail());
                                    startActivity(unIntent);
                                }
                            }
                        });
                    }


                });
                //demarrer le processus
                unT.start();

            }
            break;

        }
    }

    public static Candidat getCandidatConnected() {
        return candidatConnected;
    }

    public static void setCandidatConnected(Candidat candidatConnected) {
        MainActivity.candidatConnected = candidatConnected;
    }
}
// CREATION TACHE ASYNCHRONE ACCES A LA BDD

class Connexion extends AsyncTask<Candidat, Void, Candidat>{


    @Override
    protected Candidat doInBackground(Candidat... candidats) {
        String url = "http://192.168.202.79/Android/verifConnexion.php";
        String resultat = null;
        Candidat unCandidat = candidats[0]; //candidat recherché dans la base
        Candidat leCandidat = null;
        try {

            //etablisemet de la conenction
            URL uneUrl = new URL(url);
            //creation d'une connexion http
            HttpURLConnection uneUrlConnection = (HttpURLConnection)uneUrl.openConnection();

            uneUrlConnection.setRequestMethod("GET");

            uneUrlConnection.setDoInput(true);
            uneUrlConnection.setDoOutput(true);

            uneUrlConnection.setReadTimeout(10000);
            uneUrlConnection.setConnectTimeout(15000);

            uneUrlConnection.connect();
            Log.e("info", "connexion reussi a"+url);

            String parametre = "email=" + unCandidat.getEmail() + "&mdp=" + unCandidat.getMdp();
            //on declare un fichier de sortie pour inscrire ou ecrire les parametre

            OutputStream fichier1 = uneUrlConnection.getOutputStream();

            BufferedWriter buffer1 = new BufferedWriter(new OutputStreamWriter(fichier1,"UTF-8"));
            buffer1.write(parametre);
            buffer1.flush();
            buffer1.close();
            fichier1.close();
            Log.e("Info", "Envoi de paramatre reussi");

            InputStream fichier2 = uneUrlConnection.getInputStream();
            BufferedReader buffer2 = new BufferedReader(new InputStreamReader(fichier2, "UTF-8"));
            String ligne = null;
            StringBuilder sb = new StringBuilder();

            while ((ligne = buffer2.readLine()) != null){

                sb.append(ligne);

            }
            buffer2.close();
            fichier2.close();

            resultat = sb.toString();
            Log.e("resultat lu : ", resultat);




        }
        catch(IOException e){
            Log.e("Erreur","impossible de se connecter");

        }

        //parsing Json
        if (resultat != null)
        {
            try
            {
                JSONArray tabJson = new JSONArray(resultat);
                //on définit un objet json premier element du tableau
                JSONObject unObject = tabJson.getJSONObject(0);
                //on recupere l'element nb : count(*)
                int nb = unObject.getInt("nb");
                if(nb == 1)
                {
                    leCandidat = new Candidat(unObject.getString("nom"),unObject.getString("prenom"),"",unCandidat.getEmail(),unCandidat.getMdp());

                }

            }
            catch(JSONException exp)
            {
                Log.e("Erreur : ","Impossible parsing json"+resultat);
            }
        }



        return leCandidat;
    }

    @Override
    protected void onPostExecute(Candidat unCandidat) {
        MainActivity.setCandidatConnected(unCandidat);
    }
}


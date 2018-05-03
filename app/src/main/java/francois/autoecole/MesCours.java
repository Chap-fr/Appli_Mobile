package francois.autoecole;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

public class MesCours extends AppCompatActivity {

    private ListView lvCours;
    private String email;
    private static ArrayList<Cours> mesCours = null;

    public static void setMesCours(ArrayList<Cours> mesCours) {
        MesCours.mesCours = mesCours;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_cours);

        this.lvCours = (ListView) findViewById(R.id.idListe);
        this.email = this.getIntent().getStringExtra("email");
        final MesCours mc = this;
        Thread unT = new Thread(new Runnable() {
            @Override
            public void run() {
                Extraction uneExtraction = new Extraction();
                uneExtraction.execute(email);
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
                        if(mesCours == null){
                            Toast.makeText(mc, "vous n'avez aucun cours",Toast.LENGTH_SHORT).show();

                        }else {
                            ArrayList<String> lesCoursAffiche = new ArrayList<>();
                            for (Cours unCours : mesCours){
                                lesCoursAffiche.add(unCours.toString());
                            }
                            ArrayAdapter unAdaptateur = new ArrayAdapter(mc, android.R.layout.simple_list_item_1, mesCours);
                            lvCours.setAdapter(unAdaptateur);
                        }
                    }
                });

            }
        });
        unT.start();

    }
}

class Extraction extends AsyncTask<String, Void, ArrayList<Cours>>{




    @Override
    protected ArrayList<Cours> doInBackground(String... strings) {
        String url = "http://192.168.202.79/Android/mesCours.php";
        String resultat = null;
        ArrayList<Cours> lesCours = new ArrayList<Cours>();
        String email = strings[0];
        try {
            URL uneURL = new URL(url);
            HttpURLConnection unConnection = (HttpURLConnection)uneURL.openConnection();
            unConnection.setRequestMethod("GET");
            unConnection.setDoOutput(true);
            unConnection.setDoInput(true);
            unConnection.setConnectTimeout(15000);
            unConnection.setReadTimeout(10000);
            Log.e("info", "connexion reussi a"+url);

            String parametre = "email=" + email;


            OutputStream fichier1 = unConnection.getOutputStream();

            BufferedWriter buffer1 = new BufferedWriter(new OutputStreamWriter(fichier1,"UTF-8"));
            buffer1.write(parametre);
            buffer1.flush();
            buffer1.close();
            fichier1.close();

            InputStream fichier2 = unConnection.getInputStream();
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


        }catch (IOException exp){

            Log.e("erreur de co", url);

        }

        if (resultat != null){
            try{
                JSONArray tabJson = new JSONArray(resultat);
                for (int i = 0; i<tabJson.length();i++){

                    JSONObject unObjet = tabJson.getJSONObject(i);
                    Cours unCours = new Cours(

                            unObjet.getString("emailC"),
                            unObjet.getString("nomCand"),
                            unObjet.getString("prenomCand"),
                            unObjet.getString("nomMonit"),
                            unObjet.getString("prenomMoni"),
                            unObjet.getString("DateCours"),
                            unObjet.getString("heuredebut"),
                            unObjet.getString("heurefin"),
                            unObjet.getString("confirmation"),
                            unObjet.getInt("idCours"));
                    lesCours.add(unCours);


                }



            }catch(Exception exp){
                Log.e("erreur : ", "parse JSON");

            }
        }
        return lesCours;
    }

    @Override
    protected void onPostExecute(ArrayList<Cours> cours) {
        super.onPostExecute(cours);
    }
}
package com.example.sqllite;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    //Déclarations
    EditText edtNom, edtPrenom, edtNum;
    Button btnAfficher, btnSupprimer, btnAjouter, btnAffichertout;
    Button btnSuivant, btnPrcedent, btnPremier;
    SQLiteDatabase bd;
    Cursor c1; // utilisé par les boutons suivant et précédent

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNom = (EditText) findViewById(R.id.edtNom);
        edtPrenom = (EditText) findViewById(R.id.edtPrenom);
        edtNum = (EditText) findViewById(R.id.edtNum);
        btnAjouter = (Button) findViewById(R.id.btnAjouter);
        btnSupprimer = (Button) findViewById(R.id.btnSupprimer);
        btnAfficher = (Button) findViewById(R.id.btnAfficher);
        btnAffichertout = (Button) findViewById(R.id.btnAffichertout);
        btnSuivant = (Button) findViewById(R.id.btnSuivant);
        btnPrcedent = (Button) findViewById(R.id.btnPrcedent);
        btnPremier = (Button) findViewById(R.id.btnPremier);
        btnAjouter.setOnClickListener(this);
        btnSupprimer.setOnClickListener(this);
        btnAffichertout.setOnClickListener(this);
        btnSupprimer.setOnClickListener(this);
        btnAfficher.setOnClickListener(this);
        btnSuivant.setOnClickListener(this);
        btnPrcedent.setOnClickListener(this);
        btnPremier.setOnClickListener(this);
        bd = openOrCreateDatabase("GestionJoueurs", Context.MODE_PRIVATE, null);
        bd.execSQL("CREATE TABLE IF NOT EXISTS Joueurs(num integer primary key autoincrement,nom VARCHAR,prenom VARCHAR);");
    }

    public void onClick(View view) {

        if (view == btnAjouter) {
            if (edtNom.getText().toString().trim().length() == 0 ||
                    edtPrenom.getText().toString().trim().length() == 0) {
                AfficheMessage("Erreur", "Entrer toutes les valeurs");
                return;
            }
            bd.execSQL("INSERT INTO joueurs(nom,prenom) VALUES('" + edtNom.getText() + "','" + edtPrenom.getText() + "');");
            AfficheMessage("Succès", "Information ajouter");
            videTexte();
        }// fin bouton ajouter
//---------------DébutAfficher TOUT
        if (view == btnAffichertout) {
            Cursor c = bd.rawQuery("SELECT * FROM Joueurs", null);
            if (c.getCount() == 0) {
                AfficheMessage("Erreur", "Aucune donnée");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("Numéro: " + c.getInt(0) + "\n");
                buffer.append("Non: " + c.getString(1) + "\n");
                buffer.append("Prénom: " + c.getString(2) + "\n");
            }

            c.close();
            AfficheMessage("Les joueurs", buffer.toString());
        }
//----fin afficher tout
//Bouton supprimer
        if (view == btnSupprimer) {
            if (edtNum.getText().toString().trim().length() == 0) {
                AfficheMessage("Erreur", "Entrer un numéro de joueur");
                return;
            }
            Cursor c = bd.rawQuery("SELECT * FROM Joueurs WHERE num=" + edtNum.getText(), null);
            if (c.moveToFirst()) {
                bd.execSQL("DELETE FROM Joueurs WHERE num=" + edtNum.getText());
                AfficheMessage("Succès", "Information détruite");
            } else {
                AfficheMessage("Erreur", "Numéro invalide");
            }
            videTexte();
        } // fin supprimer

// Bouton afficher le premier
        if (view == btnPremier) {
            try {
                c1 = bd.rawQuery("SELECT * FROM Joueurs", null);
                if (c1.getCount() == 0) {
                    AfficheMessage("Erreur", "Aucune donnée");
                    return;
                }
                if (c1.moveToFirst()) {
                    edtNom.setText(c1.getString(1));
                    edtPrenom.setText(c1.getString(2));
                }
            } // fin du try
            catch (Exception se) {
                Toast.makeText(MainActivity.this, "message" + se.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        } // Fin du premier
// bouton suivant :
        if (view == btnSuivant) {
            if (c1.moveToNext())
                edtNom.setText(c1.getString(1));
            edtPrenom.setText(c1.getString(2));
        }

// bouton précedent
        if (view == this.btnPrcedent) {
            if (c1.moveToPrevious())
                edtNom.setText(c1.getString(1));
            edtPrenom.setText(c1.getString(2));
        } // fin précedent
//Bouton afficher un Enregistrement
        if (view == btnAfficher) {
            Cursor c = bd.rawQuery("SELECT * FROM Joueurs where num= " + edtNum.getText(),
                    null);
            if (c.getCount() == 0) {
                AfficheMessage("Erreur", "Aucune donnée");
                return;
            }
            if (c.moveToFirst()) {
                edtNom.setText(c.getString(1));
                edtPrenom.setText(c.getString(2));
            }
        }// fin Afficher
    }//Fin du onClick
    public void AfficheMessage(String titre,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(titre);
        builder.setMessage(message);
        builder.show();
    }
    public void videTexte()
    {
        edtNum.setText("");
        edtNom.setText("");
        edtPrenom.setText("");
    }
}//fin


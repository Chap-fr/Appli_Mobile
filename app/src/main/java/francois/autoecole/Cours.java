package francois.autoecole;

/**
 * Created by François on 29/03/2018.
 */

public class Cours {

    private String emailCours, nomCand, prenomCand, nomMoni, prenomMoni, dateCours, heureDebut, heureFin, confirmation;
    private int idCours;



    public Cours(String emailCours, String nomCand, String prenomCand, String nomMoni, String prenomMoni, String dateCours, String heureDebut, String heureFin, String confirmation, int idCours) {
        this.emailCours = emailCours;
        this.nomCand = nomCand;
        this.prenomCand = prenomCand;
        this.nomMoni = nomMoni;
        this.prenomMoni = prenomMoni;
        this.dateCours = dateCours;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.confirmation = confirmation;
        this.idCours = idCours;



    }

    public String getEmailCours() {
        return emailCours;
    }

    public void setEmailCours(String emailCours) {
        this.emailCours = emailCours;
    }

    public String getNomCand() {
        return nomCand;
    }

    public void setNomCand(String nomCand) {
        this.nomCand = nomCand;
    }

    public String getPrenomCand() {
        return prenomCand;
    }

    public void setPrenomCand(String prenomCand) {
        this.prenomCand = prenomCand;
    }

    public String getNomMoni() {
        return nomMoni;
    }

    public void setNomMoni(String nomMoni) {
        this.nomMoni = nomMoni;
    }

    public String getPrenomMoni() {
        return prenomMoni;
    }

    public void setPrenomMoni(String prenomMoni) {
        this.prenomMoni = prenomMoni;
    }

    public String getDateCours() {
        return dateCours;
    }

    public void setDateCours(String dateCours) {
        this.dateCours = dateCours;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public String toString (){
        return this.getNomMoni() +" "+ this.dateCours +" " + this.heureDebut;
    }
}
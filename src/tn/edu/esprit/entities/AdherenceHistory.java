package tn.edu.esprit.entities;

public class AdherenceHistory {
    private int id;
    private int planId;
    private String checkDate;
    private int adherenceScore;
    private int caloriesConsumed;

    public AdherenceHistory() {}

    // Constructeur avec 5 paramètres (sans protein, carbs, fat)
    public AdherenceHistory(int id, int planId, String checkDate, int adherenceScore, int caloriesConsumed) {
        this.id = id;
        this.planId = planId;
        this.checkDate = checkDate;
        this.adherenceScore = adherenceScore;
        this.caloriesConsumed = caloriesConsumed;
    }

    // Constructeur avec 8 paramètres (pour compatibilité)
    public AdherenceHistory(int id, int planId, String checkDate, int adherenceScore, 
                            int caloriesConsumed, double protein, double carbs, double fat) {
        this(id, planId, checkDate, adherenceScore, caloriesConsumed);
        // protein, carbs, fat sont ignorés
    }

    // Getters
    public int getId() { return id; }
    public int getPlanId() { return planId; }
    public String getCheckDate() { return checkDate; }
    public int getAdherenceScore() { return adherenceScore; }
    public int getCaloriesConsumed() { return caloriesConsumed; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setPlanId(int planId) { this.planId = planId; }
    public void setCheckDate(String checkDate) { this.checkDate = checkDate; }
    public void setAdherenceScore(int adherenceScore) { this.adherenceScore = adherenceScore; }
    public void setCaloriesConsumed(int caloriesConsumed) { this.caloriesConsumed = caloriesConsumed; }
}
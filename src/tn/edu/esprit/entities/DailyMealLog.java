package tn.edu.esprit.entities;

public class DailyMealLog {
    private int id, planId, calories, protein, carbs, fat, healthScore;
    private String date, mealType, notes;

    public DailyMealLog() {}
    
    public DailyMealLog(int id, int planId, String date, String mealType, int calories, int protein, int carbs, int fat, int healthScore, String notes) {
        this.id = id;
        this.planId = planId;
        this.date = date;
        this.mealType = mealType;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.healthScore = healthScore;
        this.notes = notes;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public int getPlanId() { return planId; }
    public String getMealType() { return mealType; }
    public int getCalories() { return calories; }
    public int getProtein() { return protein; }
    public int getCarbs() { return carbs; }
    public int getFat() { return fat; }
    public String getNotes() { return notes; }
    public String getDate() { return date; }
    public int getHealthScore() { return healthScore; }

    // --- SETTERS AJOUTÉS (Indispensables pour le CRUD Modifier) ---
    public void setId(int id) { this.id = id; }
    public void setPlanId(int planId) { this.planId = planId; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setProtein(int protein) { this.protein = protein; }
    public void setCarbs(int carbs) { this.carbs = carbs; }
    public void setFat(int fat) { this.fat = fat; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setDate(String date) { this.date = date; }
    public void setHealthScore(int healthScore) { this.healthScore = healthScore; }
}
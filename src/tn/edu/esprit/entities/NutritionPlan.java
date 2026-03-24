package tn.edu.esprit.entities;

import java.time.LocalDate;

public class NutritionPlan {
    private int id;
    private int userId;          // ← RELATION avec Membre 1 (User)
    private String goalType;
    private int bmrCalories;
    private int dailyCalories;
    private int proteinTarget;
    private int carbTarget;
    private int fatTarget;
    private int adherenceScore;   // Calculé depuis food_items
    private int streakDays;
    private int totalPoints;
    private String level;
    private String status;
    private LocalDate startDate;

    public NutritionPlan() {}

    public NutritionPlan(int id, int userId, String goalType, int bmrCalories, int dailyCalories,
                         int proteinTarget, int carbTarget, int fatTarget, int adherenceScore,
                         int streakDays, int totalPoints, String level, String status, LocalDate startDate) {
        this.id = id;
        this.userId = userId;
        this.goalType = goalType;
        this.bmrCalories = bmrCalories;
        this.dailyCalories = dailyCalories;
        this.proteinTarget = proteinTarget;
        this.carbTarget = carbTarget;
        this.fatTarget = fatTarget;
        this.adherenceScore = adherenceScore;
        this.streakDays = streakDays;
        this.totalPoints = totalPoints;
        this.level = level;
        this.status = status;
        this.startDate = startDate;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getGoalType() { return goalType; }
    public int getBmrCalories() { return bmrCalories; }
    public int getDailyCalories() { return dailyCalories; }
    public int getProteinTarget() { return proteinTarget; }
    public int getCarbTarget() { return carbTarget; }
    public int getFatTarget() { return fatTarget; }
    public int getAdherenceScore() { return adherenceScore; }
    public int getStreakDays() { return streakDays; }
    public int getTotalPoints() { return totalPoints; }
    public String getLevel() { return level; }
    public String getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    public void setBmrCalories(int bmrCalories) { this.bmrCalories = bmrCalories; }
    public void setDailyCalories(int dailyCalories) { this.dailyCalories = dailyCalories; }
    public void setProteinTarget(int proteinTarget) { this.proteinTarget = proteinTarget; }
    public void setCarbTarget(int carbTarget) { this.carbTarget = carbTarget; }
    public void setFatTarget(int fatTarget) { this.fatTarget = fatTarget; }
    public void setAdherenceScore(int adherenceScore) { this.adherenceScore = adherenceScore; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public void setLevel(String level) { this.level = level; }
    public void setStatus(String status) { this.status = status; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
package tn.edu.esprit.entities;

public class NutritionPlan {

    private int id;
    private int calorieTarget;
    private int proteinTarget;
    private int carbTarget;
    private int fatTarget;
    private String goalType;

    public NutritionPlan() {}

    public NutritionPlan(int id, int calorieTarget, int proteinTarget,
                         int carbTarget, int fatTarget, String goalType) {
        this.id = id;
        this.calorieTarget = calorieTarget;
        this.proteinTarget = proteinTarget;
        this.carbTarget = carbTarget;
        this.fatTarget = fatTarget;
        this.goalType = goalType;
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public int getCalorieTarget() {
        return calorieTarget;
    }

    public int getProteinTarget() {
        return proteinTarget;
    }

    public int getCarbTarget() {
        return carbTarget;
    }

    public int getFatTarget() {
        return fatTarget;
    }

    public String getGoalType() {
        return goalType;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setCalorieTarget(int calorieTarget) {
        this.calorieTarget = calorieTarget;
    }

    public void setProteinTarget(int proteinTarget) {
        this.proteinTarget = proteinTarget;
    }

    public void setCarbTarget(int carbTarget) {
        this.carbTarget = carbTarget;
    }

    public void setFatTarget(int fatTarget) {
        this.fatTarget = fatTarget;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    @Override
    public String toString() {
        return "NutritionPlan{" +
                "id=" + id +
                ", calorieTarget=" + calorieTarget +
                ", proteinTarget=" + proteinTarget +
                ", carbTarget=" + carbTarget +
                ", fatTarget=" + fatTarget +
                ", goalType='" + goalType + '\'' +
                '}';
    }
}
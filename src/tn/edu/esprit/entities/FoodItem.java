package tn.edu.esprit.entities;

public class FoodItem {
    private int id;
    private int planId;          // ← RELATION avec NutritionPlan (1:N)
    private String name;
    private int caloriesPer100g;
    private float proteinPer100g;
    private float carbsPer100g;
    private float fatPer100g;
    private float quantityG;
    private int glycemicIndex;
    private String allergens;     // ← RELATION avec Membre 2 (Allergies)
    private boolean isRecommended; // ← RELATION avec Membre 3 (IA)
    private String mealType;
    private String mealDate;
    private String category;

    public FoodItem() {}

    public FoodItem(int id, int planId, String name, int caloriesPer100g, float proteinPer100g,
                    float carbsPer100g, float fatPer100g, float quantityG, int glycemicIndex,
                    String allergens, boolean isRecommended, String mealType, String mealDate, String category) {
        this.id = id;
        this.planId = planId;
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.proteinPer100g = proteinPer100g;
        this.carbsPer100g = carbsPer100g;
        this.fatPer100g = fatPer100g;
        this.quantityG = quantityG;
        this.glycemicIndex = glycemicIndex;
        this.allergens = allergens;
        this.isRecommended = isRecommended;
        this.mealType = mealType;
        this.mealDate = mealDate;
        this.category = category;
    }

    // Getters
    public int getId() { return id; }
    public int getPlanId() { return planId; }
    public String getName() { return name; }
    public int getCaloriesPer100g() { return caloriesPer100g; }
    public float getProteinPer100g() { return proteinPer100g; }
    public float getCarbsPer100g() { return carbsPer100g; }
    public float getFatPer100g() { return fatPer100g; }
    public float getQuantityG() { return quantityG; }
    public int getGlycemicIndex() { return glycemicIndex; }
    public String getAllergens() { return allergens; }
    public boolean isRecommended() { return isRecommended; }
    public String getMealType() { return mealType; }
    public String getMealDate() { return mealDate; }
    public String getCategory() { return category; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setPlanId(int planId) { this.planId = planId; }
    public void setName(String name) { this.name = name; }
    public void setCaloriesPer100g(int caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }
    public void setProteinPer100g(float proteinPer100g) { this.proteinPer100g = proteinPer100g; }
    public void setCarbsPer100g(float carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    public void setFatPer100g(float fatPer100g) { this.fatPer100g = fatPer100g; }
    public void setQuantityG(float quantityG) { this.quantityG = quantityG; }
    public void setGlycemicIndex(int glycemicIndex) { this.glycemicIndex = glycemicIndex; }
    public void setAllergens(String allergens) { this.allergens = allergens; }
    public void setRecommended(boolean recommended) { isRecommended = recommended; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public void setMealDate(String mealDate) { this.mealDate = mealDate; }
    public void setCategory(String category) { this.category = category; }
    
    // Méthodes utilitaires (calcul automatique)
    public int getTotalCalories() {
        return (int)(caloriesPer100g * quantityG / 100);
    }
    
    public float getTotalProtein() {
        return proteinPer100g * quantityG / 100;
    }
    
    public float getTotalCarbs() {
        return carbsPer100g * quantityG / 100;
    }
    
    public float getTotalFat() {
        return fatPer100g * quantityG / 100;
    }
}
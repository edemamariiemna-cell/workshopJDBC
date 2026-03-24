package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tn.edu.esprit.entities.NutritionPlan;
import tn.edu.esprit.entities.FoodItem;
import tn.edu.esprit.entities.AdherenceHistory;
import tn.edu.esprit.services.ServiceNutritionPlan;
import tn.edu.esprit.services.ServiceFoodItem;
import tn.edu.esprit.services.ServiceAdherenceHistory;

import java.time.LocalDate;
import java.util.List;

public class NutritionController {

    // Vues
    @FXML private VBox planView;
    @FXML private VBox foodView;
    @FXML private VBox historyView;

    // ========== PLANS ==========
    @FXML private ComboBox<String> pGoalCombo;
    @FXML private TextField pCal, pProt, pCarb, pFat;
    @FXML private TableView<NutritionPlan> tablePlans;
    @FXML private TableColumn<NutritionPlan, Integer> colId;
    @FXML private TableColumn<NutritionPlan, String> colGoal;
    @FXML private TableColumn<NutritionPlan, Integer> colCal, colProt, colCarb, colFat, colAdherence;
    @FXML private TableColumn<NutritionPlan, String> colStatus;

    // ========== FOOD ITEMS ==========
    @FXML private Label selectedPlanLabel;
    @FXML private TextField fName, fCalPer100, fProtPer100, fCarbPer100, fFatPer100, fQuantity, fAllergens;
    @FXML private ComboBox<String> fMealType, fCategory;
    @FXML private DatePicker fMealDate;
    @FXML private TableView<FoodItem> tableFoods;
    @FXML private TableColumn<FoodItem, String> colFoodName, colMealType, colFoodDate, colAllergens;
    @FXML private TableColumn<FoodItem, Integer> colFoodCal, colFoodQuantity, colTotalCal;

    // ========== HISTORIQUE ==========
    @FXML private Label historyPlanLabel, currentScoreLabel, currentLevelLabel;
    @FXML private TableView<AdherenceHistory> tableHistory;
    @FXML private TableColumn<AdherenceHistory, String> colHistoryDate;
    @FXML private TableColumn<AdherenceHistory, Integer> colHistoryScore, colHistoryCalories;

    // Services
    private ServiceNutritionPlan sp = new ServiceNutritionPlan();
    private ServiceFoodItem sf = new ServiceFoodItem();
    private ServiceAdherenceHistory sh = new ServiceAdherenceHistory();
    private NutritionPlan selectedPlan = null;

    @FXML
    public void initialize() {
        System.out.println("=== INITIALISATION CONTROLLER ===");
        
        // ========== INITIALISATION DES COMBOBOX ==========
        pGoalCombo.getItems().clear();
        pGoalCombo.getItems().addAll("perte_poids", "prise_masse", "maintien");
        pGoalCombo.setValue("perte_poids");
        
        fMealType.getItems().clear();
        fMealType.getItems().addAll("petit_dejeuner", "dejeuner", "diner", "collation");
        fMealType.setValue("dejeuner");
        
        fCategory.getItems().clear();
        fCategory.getItems().addAll("Protéines", "Céréales", "Légumes", "Fruits", "Produits laitiers");
        fCategory.setValue("Protéines");
        
        fMealDate.setValue(LocalDate.now());
        
        // ========== CONFIGURATION DES COLONNES ==========
        
        // Plans
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGoal.setCellValueFactory(new PropertyValueFactory<>("goalType"));
        colCal.setCellValueFactory(new PropertyValueFactory<>("dailyCalories"));
        colProt.setCellValueFactory(new PropertyValueFactory<>("proteinTarget"));
        colCarb.setCellValueFactory(new PropertyValueFactory<>("carbTarget"));
        colFat.setCellValueFactory(new PropertyValueFactory<>("fatTarget"));
        colAdherence.setCellValueFactory(new PropertyValueFactory<>("adherenceScore"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Food Items
        colFoodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFoodCal.setCellValueFactory(new PropertyValueFactory<>("caloriesPer100g"));
        colFoodQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityG"));
        colTotalCal.setCellValueFactory(new PropertyValueFactory<>("totalCalories"));
        colMealType.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        colFoodDate.setCellValueFactory(new PropertyValueFactory<>("mealDate"));
        colAllergens.setCellValueFactory(new PropertyValueFactory<>("allergens"));
        
        // Historique
        colHistoryDate.setCellValueFactory(new PropertyValueFactory<>("checkDate"));
        colHistoryScore.setCellValueFactory(new PropertyValueFactory<>("adherenceScore"));
        colHistoryCalories.setCellValueFactory(new PropertyValueFactory<>("caloriesConsumed"));
        
        // ========== LISTENERS ==========
        tablePlans.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                selectedPlan = newVal;
                pGoalCombo.setValue(newVal.getGoalType());
                pCal.setText(String.valueOf(newVal.getDailyCalories()));
                pProt.setText(String.valueOf(newVal.getProteinTarget()));
                pCarb.setText(String.valueOf(newVal.getCarbTarget()));
                pFat.setText(String.valueOf(newVal.getFatTarget()));
                
                selectedPlanLabel.setText("✅ Plan sélectionné : " + newVal.getGoalType() + " (" + newVal.getDailyCalories() + " kcal)");
                historyPlanLabel.setText("📊 Historique du plan : " + newVal.getGoalType());
                currentScoreLabel.setText(newVal.getAdherenceScore() + "%");
                currentLevelLabel.setText(newVal.getLevel());
                
                refreshHistory();
            }
        });
        
        tableFoods.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                fName.setText(newVal.getName());
                fCalPer100.setText(String.valueOf(newVal.getCaloriesPer100g()));
                fProtPer100.setText(String.valueOf(newVal.getProteinPer100g()));
                fCarbPer100.setText(String.valueOf(newVal.getCarbsPer100g()));
                fFatPer100.setText(String.valueOf(newVal.getFatPer100g()));
                fQuantity.setText(String.valueOf(newVal.getQuantityG()));
                fAllergens.setText(newVal.getAllergens());
                fMealType.setValue(newVal.getMealType());
                fCategory.setValue(newVal.getCategory());
                if (newVal.getMealDate() != null) {
                    fMealDate.setValue(LocalDate.parse(newVal.getMealDate()));
                }
            }
        });
        
        refresh();
    }

    private void refresh() {
        tablePlans.setItems(FXCollections.observableArrayList(sp.getAll()));
        tableFoods.setItems(FXCollections.observableArrayList(sf.getAll()));
    }
    
    private void refreshHistory() {
        if (selectedPlan != null) {
            tableHistory.setItems(FXCollections.observableArrayList(sh.getByPlanId(selectedPlan.getId())));
        }
    }

    // ========== NAVIGATION ==========
    @FXML private void showPlans() { 
        planView.setVisible(true); planView.setManaged(true);
        foodView.setVisible(false); foodView.setManaged(false);
        historyView.setVisible(false); historyView.setManaged(false);
        refresh();
    }

    @FXML private void showFoodItems() { 
        planView.setVisible(false); planView.setManaged(false);
        foodView.setVisible(true); foodView.setManaged(true);
        historyView.setVisible(false); historyView.setManaged(false);
        refresh();
    }

    @FXML private void showHistory() { 
        planView.setVisible(false); planView.setManaged(false);
        foodView.setVisible(false); foodView.setManaged(false);
        historyView.setVisible(true); historyView.setManaged(true);
        refreshHistory();
    }

    // ========== CRUD PLANS ==========
    @FXML
    private void handleAddPlan() {
        try {
            int cal = Integer.parseInt(pCal.getText());
            int prot = Integer.parseInt(pProt.getText());
            int carb = Integer.parseInt(pCarb.getText());
            int fat = Integer.parseInt(pFat.getText());
            String goalType = pGoalCombo.getValue();
            
            if (goalType == null) {
                showAlert("Erreur", "Sélectionnez un objectif");
                return;
            }
            
            int bmr = 1850;
            NutritionPlan plan = new NutritionPlan(0, 1, goalType, bmr, cal, prot, carb, fat, 0, 0, 0, "bronze", "actif", LocalDate.now());
            sp.ajouter(plan);
            refresh();
            clearPlanFields();
            showAlert("Succès", "Plan ajouté !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeurs invalides");
        }
    }

    @FXML private void handleUpdatePlan() {
        if (selectedPlan == null) {
            showAlert("Erreur", "Sélectionnez un plan");
            return;
        }
        try {
            selectedPlan.setDailyCalories(Integer.parseInt(pCal.getText()));
            selectedPlan.setProteinTarget(Integer.parseInt(pProt.getText()));
            selectedPlan.setCarbTarget(Integer.parseInt(pCarb.getText()));
            selectedPlan.setFatTarget(Integer.parseInt(pFat.getText()));
            selectedPlan.setGoalType(pGoalCombo.getValue());
            sp.modifier(selectedPlan);
            refresh();
            clearPlanFields();
            showAlert("Succès", "Plan modifié !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeurs invalides");
        }
    }
    
    @FXML private void handleDeletePlan() {
        if (selectedPlan != null) {
            sp.supprimer(selectedPlan.getId());
            refresh();
            clearPlanFields();
            showAlert("Succès", "Plan supprimé !");
        } else {
            showAlert("Erreur", "Sélectionnez un plan");
        }
    }
    
    private void clearPlanFields() {
        pGoalCombo.setValue("perte_poids");
        pCal.clear(); pProt.clear(); pCarb.clear(); pFat.clear();
    }

    // ========== CRUD FOOD ITEMS ==========
    @FXML
    private void handleAddFood() {
        if (selectedPlan == null) {
            showAlert("Erreur", "Sélectionnez d'abord un plan");
            return;
        }
        try {
            if (fName.getText().isEmpty()) {
                showAlert("Erreur", "Nom de l'aliment requis");
                return;
            }
            
            FoodItem food = new FoodItem(0, selectedPlan.getId(), fName.getText(),
                Integer.parseInt(fCalPer100.getText()),
                Float.parseFloat(fProtPer100.getText()),
                Float.parseFloat(fCarbPer100.getText()),
                Float.parseFloat(fFatPer100.getText()),
                Float.parseFloat(fQuantity.getText()),
                0, fAllergens.getText(), true,
                fMealType.getValue(), fMealDate.getValue().toString(),
                fCategory.getValue());
            sf.ajouter(food);
            
            sp.updateAdherenceScore(selectedPlan.getId(), fMealDate.getValue().toString());
            
            refresh();
            clearFoodFields();
            showAlert("Succès", "Aliment ajouté !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeurs numériques invalides");
        }
    }

    @FXML private void handleUpdateFood() {
        FoodItem selected = tableFoods.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez un aliment");
            return;
        }
        try {
            selected.setName(fName.getText());
            selected.setCaloriesPer100g(Integer.parseInt(fCalPer100.getText()));
            selected.setProteinPer100g(Float.parseFloat(fProtPer100.getText()));
            selected.setCarbsPer100g(Float.parseFloat(fCarbPer100.getText()));
            selected.setFatPer100g(Float.parseFloat(fFatPer100.getText()));
            selected.setQuantityG(Float.parseFloat(fQuantity.getText()));
            selected.setAllergens(fAllergens.getText());
            selected.setMealType(fMealType.getValue());
            selected.setCategory(fCategory.getValue());
            selected.setMealDate(fMealDate.getValue().toString());
            sf.modifier(selected);
            
            if (selectedPlan != null) {
                sp.updateAdherenceScore(selectedPlan.getId(), fMealDate.getValue().toString());
            }
            
            refresh();
            clearFoodFields();
            showAlert("Succès", "Aliment modifié !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeurs numériques invalides");
        }
    }
    
    @FXML private void handleDeleteFood() {
        FoodItem selected = tableFoods.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sf.supprimer(selected.getId());
            refresh();
            clearFoodFields();
            showAlert("Succès", "Aliment supprimé !");
        } else {
            showAlert("Erreur", "Sélectionnez un aliment");
        }
    }
    
    private void clearFoodFields() {
        fName.clear(); fCalPer100.clear(); fProtPer100.clear(); fCarbPer100.clear();
        fFatPer100.clear(); fQuantity.clear(); fAllergens.clear();
        fMealType.setValue("dejeuner");
        fCategory.setValue("Protéines");
        fMealDate.setValue(LocalDate.now());
    }
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
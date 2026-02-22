package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.edu.esprit.entities.*;
import tn.edu.esprit.services.*;
import java.time.LocalDate;

public class MainGUI extends Application {

    // SERVICES
    private ServiceNutritionPlan sPlan = new ServiceNutritionPlan();
    private ServiceDailyMealLog sMeal = new ServiceDailyMealLog();

    // TABLES
    private TableView<NutritionPlan> tablePlans = new TableView<>();
    private TableView<DailyMealLog> tableMeals = new TableView<>();

    // PLAN FIELDS
    private TextField pGoal = new TextField();
    private TextField pCal = new TextField();
    private TextField pProt = new TextField();
    private TextField pCarb = new TextField();
    private TextField pFat = new TextField();

    // MEAL FIELDS
    private TextField mName = new TextField();
    private TextField mCal = new TextField();
    private TextField mProt = new TextField();
    private TextField mCarb = new TextField();
    private TextField mFat = new TextField();
    private TextField mScore = new TextField();
    private TextField mNotes = new TextField();
    
    // LABELS
    private Label selectedPlanLabel = new Label("Aucun plan sélectionné");
    private Label planError = new Label();
    private Label mealError = new Label();

    @Override
    public void start(Stage stage) {
        
        // Configuration de la validation
        setupValidation();
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f6f9;");

        // SECTION PLANS
        setupPlanTable();
        VBox planSection = createPlanSection();
        
        // SECTION REPAS
        setupMealTable();
        VBox mealSection = createMealSection();
        
        // SÉPARATEUR
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        root.getChildren().addAll(planSection, separator, mealSection);

        // LISTENER POUR LA SÉLECTION DE PLAN
        tablePlans.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                // Remplir le formulaire plan
                pGoal.setText(sel.getGoalType());
                pCal.setText(String.valueOf(sel.getCalorieTarget()));
                pProt.setText(String.valueOf(sel.getProteinTarget()));
                pCarb.setText(String.valueOf(sel.getCarbTarget()));
                pFat.setText(String.valueOf(sel.getFatTarget()));
                
                selectedPlanLabel.setText("Plan sélectionné : " + sel.getGoalType() + " (" + sel.getCalorieTarget() + " kcal)");
                selectedPlanLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                // Charger les repas associés
                refreshMeals(sel.getId());
            } else {
                selectedPlanLabel.setText("Aucun plan sélectionné");
                selectedPlanLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                tableMeals.getItems().clear();
            }
        });

        // LISTENER POUR LA SÉLECTION DE REPAS
        tableMeals.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                mName.setText(sel.getMealType());
                mCal.setText(String.valueOf(sel.getCalories()));
                mProt.setText(String.valueOf(sel.getProtein()));
                mCarb.setText(String.valueOf(sel.getCarbs()));
                mFat.setText(String.valueOf(sel.getFat()));
                mScore.setText(String.valueOf(sel.getHealthScore()));
                mNotes.setText(sel.getNotes());
            }
        });

        // Chargement initial
        refreshPlans();

        Scene scene = new Scene(root, 1300, 900);
        
        stage.setTitle("Gestion Nutritionnelle - CRUD Complet");
        stage.setScene(scene);
        stage.show();
    }
    
    // ======================================================
    // SECTION PLAN
    // ======================================================
    
    private VBox createPlanSection() {
        // Formulaire Plan
        GridPane planForm = new GridPane();
        planForm.setHgap(10);
        planForm.setVgap(10);
        planForm.setPadding(new Insets(15));
        planForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        pGoal.setPromptText("Objectif (ex: prise de masse)");
        pCal.setPromptText("Calories");
        pProt.setPromptText("Protéines");
        pCarb.setPromptText("Glucides");
        pFat.setPromptText("Lipides");

        planForm.add(new Label("Objectif:"), 0, 0);
        planForm.add(pGoal, 1, 0);
        planForm.add(new Label("Calories (kcal):"), 0, 1);
        planForm.add(pCal, 1, 1);
        planForm.add(new Label("Protéines (g):"), 0, 2);
        planForm.add(pProt, 1, 2);
        planForm.add(new Label("Glucides (g):"), 0, 3);
        planForm.add(pCarb, 1, 3);
        planForm.add(new Label("Lipides (g):"), 0, 4);
        planForm.add(pFat, 1, 4);
        
        // Boutons Plan
        Button addPlan = new Button("Ajouter");
        Button modPlan = new Button("Modifier");
        Button delPlan = new Button("Supprimer");
        Button clearPlan = new Button("Effacer");
        
        styleButton(addPlan, "#4CAF50");
        styleButton(modPlan, "#2196F3");
        styleButton(delPlan, "#f44336");
        styleButton(clearPlan, "#9E9E9E");
        
        HBox planButtons = new HBox(10, addPlan, modPlan, delPlan, clearPlan);
        planButtons.setPadding(new Insets(10, 0, 0, 0));
        
        // Message d'erreur
        planError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        VBox planFormBox = new VBox(5, 
            new Label("GESTION DES PLANS NUTRITIONNELS"), 
            planForm, 
            planButtons,
            planError
        );
        
        // Tableau des Plans
        Label planTableLabel = new Label("LISTE DES PLANS");
        planTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox planTableBox = new VBox(10, 
            planTableLabel, 
            tablePlans
        );
        planTableBox.setPrefWidth(800);
        
        // Mise en page horizontale
        HBox planContent = new HBox(20, planFormBox, planTableBox);
        HBox.setHgrow(planTableBox, Priority.ALWAYS);
        
        // Actions
        addPlan.setOnAction(e -> handleAddPlan());
        modPlan.setOnAction(e -> handleUpdatePlan());
        delPlan.setOnAction(e -> handleDeletePlan());
        clearPlan.setOnAction(e -> clearPlanForm());
        
        Label sectionTitle = new Label("PLANS NUTRITIONNELS");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        return new VBox(10, sectionTitle, planContent);
    }
    
    // ======================================================
    // SECTION REPAS
    // ======================================================
    
    private VBox createMealSection() {
        // Formulaire Repas
        GridPane mealForm = new GridPane();
        mealForm.setHgap(10);
        mealForm.setVgap(10);
        mealForm.setPadding(new Insets(15));
        mealForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        mName.setPromptText("Nom du repas");
        mCal.setPromptText("Calories");
        mProt.setPromptText("Protéines");
        mCarb.setPromptText("Glucides");
        mFat.setPromptText("Lipides");
        mScore.setPromptText("Score (0-100)");
        mNotes.setPromptText("Notes...");

        mealForm.add(new Label("Repas:"), 0, 0);
        mealForm.add(mName, 1, 0);
        mealForm.add(new Label("Calories:"), 0, 1);
        mealForm.add(mCal, 1, 1);
        mealForm.add(new Label("Protéines:"), 0, 2);
        mealForm.add(mProt, 1, 2);
        mealForm.add(new Label("Glucides:"), 0, 3);
        mealForm.add(mCarb, 1, 3);
        mealForm.add(new Label("Lipides:"), 0, 4);
        mealForm.add(mFat, 1, 4);
        mealForm.add(new Label("Score Santé:"), 0, 5);
        mealForm.add(mScore, 1, 5);
        mealForm.add(new Label("Notes:"), 0, 6);
        mealForm.add(mNotes, 1, 6);

        // Boutons Repas
        Button addMeal = new Button("Ajouter Repas");
        Button modMeal = new Button("Modifier Repas");
        Button delMeal = new Button("Supprimer Repas");
        Button clearMeal = new Button("Effacer");
        
        styleButton(addMeal, "#4CAF50");
        styleButton(modMeal, "#2196F3");
        styleButton(delMeal, "#f44336");
        styleButton(clearMeal, "#9E9E9E");
        
        HBox mealButtons = new HBox(10, addMeal, modMeal, delMeal, clearMeal);
        mealButtons.setPadding(new Insets(10, 0, 0, 0));
        
        // Message d'erreur
        mealError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        VBox mealFormBox = new VBox(5, 
            new Label("GESTION DES REPAS"), 
            selectedPlanLabel,
            mealForm, 
            mealButtons,
            mealError
        );
        
        // Tableau des Repas
        Label mealTableLabel = new Label("REPAS ASSOCIÉS AU PLAN SÉLECTIONNÉ");
        mealTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox mealTableBox = new VBox(10, 
            mealTableLabel, 
            tableMeals
        );
        mealTableBox.setPrefWidth(800);
        
        // Mise en page horizontale
        HBox mealContent = new HBox(20, mealFormBox, mealTableBox);
        HBox.setHgrow(mealTableBox, Priority.ALWAYS);
        
        // Actions
        addMeal.setOnAction(e -> handleAddMeal());
        modMeal.setOnAction(e -> handleUpdateMeal());
        delMeal.setOnAction(e -> handleDeleteMeal());
        clearMeal.setOnAction(e -> clearMealForm());
        
        Label sectionTitle = new Label("REPAS QUOTIDIENS");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        return new VBox(10, sectionTitle, mealContent);
    }
    
    // ======================================================
    // CONFIGURATION DES TABLES
    // ======================================================
    
    private void setupPlanTable() {
        TableColumn<NutritionPlan, String> col1 = new TableColumn<>("Objectif");
        col1.setCellValueFactory(new PropertyValueFactory<>("goalType"));
        col1.setPrefWidth(150);
        
        TableColumn<NutritionPlan, Integer> col2 = new TableColumn<>("Calories");
        col2.setCellValueFactory(new PropertyValueFactory<>("calorieTarget"));
        col2.setPrefWidth(80);
        
        TableColumn<NutritionPlan, Integer> col3 = new TableColumn<>("Protéines");
        col3.setCellValueFactory(new PropertyValueFactory<>("proteinTarget"));
        col3.setPrefWidth(80);
        
        TableColumn<NutritionPlan, Integer> col4 = new TableColumn<>("Glucides");
        col4.setCellValueFactory(new PropertyValueFactory<>("carbTarget"));
        col4.setPrefWidth(80);
        
        TableColumn<NutritionPlan, Integer> col5 = new TableColumn<>("Lipides");
        col5.setCellValueFactory(new PropertyValueFactory<>("fatTarget"));
        col5.setPrefWidth(80);
        
        tablePlans.getColumns().addAll(col1, col2, col3, col4, col5);
        tablePlans.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupMealTable() {
        TableColumn<DailyMealLog, String> col1 = new TableColumn<>("Repas");
        col1.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        col1.setPrefWidth(150);
        
        TableColumn<DailyMealLog, Integer> col2 = new TableColumn<>("Calories");
        col2.setCellValueFactory(new PropertyValueFactory<>("calories"));
        col2.setPrefWidth(80);
        
        TableColumn<DailyMealLog, Integer> col3 = new TableColumn<>("Protéines");
        col3.setCellValueFactory(new PropertyValueFactory<>("protein"));
        col3.setPrefWidth(80);
        
        TableColumn<DailyMealLog, Integer> col4 = new TableColumn<>("Glucides");
        col4.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        col4.setPrefWidth(80);
        
        TableColumn<DailyMealLog, Integer> col5 = new TableColumn<>("Lipides");
        col5.setCellValueFactory(new PropertyValueFactory<>("fat"));
        col5.setPrefWidth(80);
        
        TableColumn<DailyMealLog, Integer> col6 = new TableColumn<>("Score");
        col6.setCellValueFactory(new PropertyValueFactory<>("healthScore"));
        col6.setPrefWidth(70);
        
        tableMeals.getColumns().addAll(col1, col2, col3, col4, col5, col6);
        tableMeals.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    // ======================================================
    // VALIDATION
    // ======================================================
    
    private void setupValidation() {
        // Validation numérique : seulement des chiffres
        TextField[] numericFields = {pCal, pProt, pCarb, pFat, mCal, mProt, mCarb, mFat, mScore};
        for (TextField f : numericFields) {
            f.textProperty().addListener((obs, old, val) -> {
                if (!val.matches("\\d*")) {
                    f.setText(val.replaceAll("[^\\d]", ""));
                }
            });
        }
    }
    
    private boolean validatePlanForm() {
        planError.setText("");
        
        if (pGoal.getText().trim().isEmpty()) {
            planError.setText("❌ L'objectif est requis");
            pGoal.requestFocus();
            return false;
        }
        
        if (pCal.getText().trim().isEmpty()) {
            planError.setText("❌ Les calories sont requises");
            pCal.requestFocus();
            return false;
        }
        
        try {
            int cal = Integer.parseInt(pCal.getText());
            if (cal <= 0) {
                planError.setText("❌ Les calories doivent être > 0");
                pCal.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            planError.setText("❌ Calories doit être un nombre valide");
            pCal.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // MÉTHODE DE VALIDATION CORRIGÉE POUR LES REPAS
    private boolean validateMealForm() {
        mealError.setText("");
        
        NutritionPlan selectedPlan = tablePlans.getSelectionModel().getSelectedItem();
        if (selectedPlan == null) {
            mealError.setText("❌ Veuillez sélectionner un plan nutritionnel d'abord");
            return false;
        }
        
        if (mName.getText().trim().isEmpty()) {
            mealError.setText("❌ Le nom du repas est requis");
            mName.requestFocus();
            return false;
        }
        
        // Vérification des champs numériques
        if (mCal.getText().trim().isEmpty()) {
            mealError.setText("❌ Les calories sont requises");
            mCal.requestFocus();
            return false;
        }
        
        try {
            int cal = Integer.parseInt(mCal.getText());
            if (cal <= 0) {
                mealError.setText("❌ Les calories doivent être > 0");
                mCal.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mealError.setText("❌ Calories doit être un nombre valide");
            mCal.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // ======================================================
    // HANDLERS CRUD - CORRIGÉS
    // ======================================================
    
    private void handleAddPlan() {
        if (!validatePlanForm()) return;
        
        NutritionPlan plan = new NutritionPlan(
            0,
            safeParseInt(pCal.getText()),
            safeParseInt(pProt.getText()),
            safeParseInt(pCarb.getText()),
            safeParseInt(pFat.getText()),
            pGoal.getText()
        );
        
        sPlan.ajouter(plan);
        refreshPlans();
        clearPlanForm();
        showInfo("✅ Plan ajouté avec succès !");
    }
    
    private void handleUpdatePlan() {
        NutritionPlan selected = tablePlans.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un plan à modifier");
            return;
        }
        
        if (!validatePlanForm()) return;
        
        selected.setGoalType(pGoal.getText());
        selected.setCalorieTarget(safeParseInt(pCal.getText()));
        selected.setProteinTarget(safeParseInt(pProt.getText()));
        selected.setCarbTarget(safeParseInt(pCarb.getText()));
        selected.setFatTarget(safeParseInt(pFat.getText()));
        
        sPlan.modifier(selected);
        refreshPlans();
        showInfo("✏️ Plan modifié avec succès !");
    }
    
    private void handleDeletePlan() {
        NutritionPlan selected = tablePlans.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un plan à supprimer");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le plan ?");
        confirm.setContentText("Cette action supprimera également tous les repas associés !");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            sPlan.supprimer(selected.getId());
            refreshPlans();
            tableMeals.getItems().clear();
            clearPlanForm();
            showInfo("🗑️ Plan supprimé !");
        }
    }
    
    // MÉTHODE CORRIGÉE POUR AJOUTER UN REPAS
    private void handleAddMeal() {
        if (!validateMealForm()) return;
        
        NutritionPlan selectedPlan = tablePlans.getSelectionModel().getSelectedItem();
        
        DailyMealLog meal = new DailyMealLog(
            0,
            selectedPlan.getId(),
            LocalDate.now().toString(),
            mName.getText(),
            safeParseInt(mCal.getText()),
            safeParseInt(mProt.getText()),
            safeParseInt(mCarb.getText()),
            safeParseInt(mFat.getText()),
            safeParseInt(mScore.getText()),
            mNotes.getText()
        );
        
        sMeal.ajouter(meal);
        refreshMeals(selectedPlan.getId());
        clearMealForm();
        showInfo("✅ Repas ajouté avec succès !");
    }
    
    private void handleUpdateMeal() {
        DailyMealLog selected = tableMeals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un repas à modifier");
            return;
        }
        
        if (!validateMealForm()) return;
        
        selected.setMealType(mName.getText());
        selected.setCalories(safeParseInt(mCal.getText()));
        selected.setProtein(safeParseInt(mProt.getText()));
        selected.setCarbs(safeParseInt(mCarb.getText()));
        selected.setFat(safeParseInt(mFat.getText()));
        selected.setHealthScore(safeParseInt(mScore.getText()));
        selected.setNotes(mNotes.getText());
        
        sMeal.modifier(selected);
        refreshMeals(selected.getPlanId());
        showInfo("✏️ Repas modifié avec succès !");
    }
    
    private void handleDeleteMeal() {
        DailyMealLog selected = tableMeals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un repas à supprimer");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le repas ?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            sMeal.supprimer(selected.getId());
            refreshMeals(selected.getPlanId());
            clearMealForm();
            showInfo("🗑️ Repas supprimé !");
        }
    }
    
    // ======================================================
    // UTILITAIRES
    // ======================================================
    
    // MÉTHODE SÉCURISÉE POUR PARSER LES ENTIERS
    private int safeParseInt(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private void refreshPlans() {
        tablePlans.getItems().setAll(sPlan.getAll());
    }
    
    private void refreshMeals(int planId) {
        tableMeals.getItems().setAll(sMeal.getByPlan(planId));
    }
    
    private void clearPlanForm() {
        pGoal.clear();
        pCal.clear();
        pProt.clear();
        pCarb.clear();
        pFat.clear();
        planError.setText("");
    }
    
    private void clearMealForm() {
        mName.clear();
        mCal.clear();
        mProt.clear();
        mCarb.clear();
        mFat.clear();
        mScore.clear();
        mNotes.clear();
        mealError.setText("");
    }
    
    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + darken(color) + "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"));
    }
    
    private String darken(String color) {
        switch(color) {
            case "#4CAF50": return "#45a049";
            case "#2196F3": return "#1976D2";
            case "#f44336": return "#d32f2f";
            case "#9E9E9E": return "#757575";
            default: return color;
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
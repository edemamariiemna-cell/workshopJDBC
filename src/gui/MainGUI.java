package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private Label selectedPlanLabel = new Label("⚠ Aucun plan sélectionné");
    private Label planError = new Label();
    private Label mealError = new Label();

    @Override
    public void start(Stage stage) {

        setupValidation();

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f6f9;");

        setupPlanTable();
        VBox planSection = createPlanSection();

        setupMealTable();
        VBox mealSection = createMealSection();

        root.getChildren().addAll(planSection, new Separator(), mealSection);

        // LISTENER PLAN
        tablePlans.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                pGoal.setText(sel.getGoalType());
                pCal.setText(String.valueOf(sel.getCalorieTarget()));
                pProt.setText(String.valueOf(sel.getProteinTarget()));
                pCarb.setText(String.valueOf(sel.getCarbTarget()));
                pFat.setText(String.valueOf(sel.getFatTarget()));

                selectedPlanLabel.setText("✓ Plan sélectionné : " + sel.getGoalType() + " (" + sel.getCalorieTarget() + " kcal)");
                selectedPlanLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                
                refreshMeals(sel.getId());
            } else {
                selectedPlanLabel.setText("⚠ Aucun plan sélectionné");
                selectedPlanLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                tableMeals.getItems().clear();
            }
        });

        // LISTENER MEAL
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

        refreshPlans();

        Scene scene = new Scene(root, 1300, 900);
        stage.setTitle("Gestion Nutritionnelle - CRUD Complet");
        stage.setScene(scene);
        stage.show();
    }

    // ======================================================
    // SECTION PLAN - SANS BOUTON EFFACER
    // ======================================================

    private VBox createPlanSection() {

        GridPane planForm = new GridPane();
        planForm.setHgap(10);
        planForm.setVgap(10);
        planForm.setPadding(new Insets(15));
        planForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        pGoal.setPromptText("Objectif");
        pCal.setPromptText("Calories");
        pProt.setPromptText("Protéines");
        pCarb.setPromptText("Glucides");
        pFat.setPromptText("Lipides");

        planForm.add(new Label("Objectif:"), 0, 0);
        planForm.add(pGoal, 1, 0);
        planForm.add(new Label("Calories:"), 0, 1);
        planForm.add(pCal, 1, 1);
        planForm.add(new Label("Protéines:"), 0, 2);
        planForm.add(pProt, 1, 2);
        planForm.add(new Label("Glucides:"), 0, 3);
        planForm.add(pCarb, 1, 3);
        planForm.add(new Label("Lipides:"), 0, 4);
        planForm.add(pFat, 1, 4);

        // BOUTONS SANS EFFACER
        Button addPlan = new Button("Ajouter");
        Button modPlan = new Button("Modifier");
        Button delPlan = new Button("Supprimer");
        
        styleButton(addPlan, "#4CAF50");
        styleButton(modPlan, "#2196F3");
        styleButton(delPlan, "#f44336");

        HBox buttons = new HBox(10, addPlan, modPlan, delPlan);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(5, 0, 0, 0));

        planError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        VBox formBox = new VBox(8, planForm, buttons, planError);

        addPlan.setOnAction(e -> handleAddPlan());
        modPlan.setOnAction(e -> handleUpdatePlan());
        delPlan.setOnAction(e -> handleDeletePlan());

        VBox tableBox = new VBox(10, new Label("LISTE DES PLANS"), tablePlans);
        tableBox.setPrefWidth(800);

        HBox content = new HBox(20, formBox, tableBox);
        HBox.setHgrow(tableBox, Priority.ALWAYS);

        return new VBox(10, new Label("PLANS NUTRITIONNELS"), content);
    }

    // ======================================================
    // SECTION REPAS - SANS BOUTON EFFACER
    // ======================================================

    private VBox createMealSection() {

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
        mScore.setPromptText("Score");
        mNotes.setPromptText("Notes");

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
        mealForm.add(new Label("Score:"), 0, 5);
        mealForm.add(mScore, 1, 5);
        mealForm.add(new Label("Notes:"), 0, 6);
        mealForm.add(mNotes, 1, 6);

        // BOUTONS SANS EFFACER
        Button addMeal = new Button("Ajouter Repas");
        Button modMeal = new Button("Modifier Repas");
        Button delMeal = new Button("Supprimer Repas");

        styleButton(addMeal, "#4CAF50");
        styleButton(modMeal, "#2196F3");
        styleButton(delMeal, "#f44336");

        HBox mealButtons = new HBox(10, addMeal, modMeal, delMeal);
        mealButtons.setAlignment(Pos.CENTER);
        mealButtons.setPadding(new Insets(5, 0, 0, 0));

        mealError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        VBox mealFormBox = new VBox(8,
                new Label("GESTION DES REPAS"),
                selectedPlanLabel,
                mealForm,
                mealButtons,
                mealError
        );

        addMeal.setOnAction(e -> handleAddMeal());
        modMeal.setOnAction(e -> handleUpdateMeal());
        delMeal.setOnAction(e -> handleDeleteMeal());

        VBox mealTableBox = new VBox(10,
                new Label("REPAS ASSOCIÉS AU PLAN SÉLECTIONNÉ"),
                tableMeals
        );
        mealTableBox.setPrefWidth(800);

        HBox mealContent = new HBox(20, mealFormBox, mealTableBox);
        HBox.setHgrow(mealTableBox, Priority.ALWAYS);

        return new VBox(10, new Label("REPAS QUOTIDIENS"), mealContent);
    }

    // ======================================================
    // TABLES
    // ======================================================

    private void setupPlanTable() {
        tablePlans.getColumns().addAll(
                col("Objectif", "goalType"),
                col("Calories", "calorieTarget"),
                col("Protéines", "proteinTarget"),
                col("Glucides", "carbTarget"),
                col("Lipides", "fatTarget")
        );
        tablePlans.setPlaceholder(new Label("Aucun plan"));
    }

    private void setupMealTable() {
        tableMeals.getColumns().addAll(
                col("Repas", "mealType"),
                col("Calories", "calories"),
                col("Protéines", "protein"),
                col("Glucides", "carbs"),
                col("Lipides", "fat"),
                col("Score", "healthScore")
        );
        tableMeals.setPlaceholder(new Label("Aucun repas"));
    }

    private <T> TableColumn<T, ?> col(String title, String prop) {
        TableColumn<T, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        return c;
    }

    // ======================================================
    // CRUD METHODS
    // ======================================================

    private void handleAddPlan() {
        if (pGoal.getText().trim().isEmpty() || pCal.getText().trim().isEmpty()) {
            planError.setText(" Remplissez les champs requis");
            return;
        }
        
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
        showSuccess("Plan ajouté");
    }

    private void handleUpdatePlan() {
        NutritionPlan selected = tablePlans.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélectionnez un plan");
            return;
        }

        selected.setGoalType(pGoal.getText());
        selected.setCalorieTarget(safeParseInt(pCal.getText()));
        selected.setProteinTarget(safeParseInt(pProt.getText()));
        selected.setCarbTarget(safeParseInt(pCarb.getText()));
        selected.setFatTarget(safeParseInt(pFat.getText()));

        sPlan.modifier(selected);
        refreshPlans();
        showSuccess("Plan modifié");
    }

    private void handleDeletePlan() {
        NutritionPlan selected = tablePlans.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélectionnez un plan");
            return;
        }

        if (confirm("Supprimer ce plan ?")) {
            sPlan.supprimer(selected.getId());
            refreshPlans();
            tableMeals.getItems().clear();
            clearPlanForm();
            showSuccess("Plan supprimé");
        }
    }

    private void handleAddMeal() {
        NutritionPlan selectedPlan = tablePlans.getSelectionModel().getSelectedItem();
        if (selectedPlan == null) {
            mealError.setText(" Sélectionnez un plan");
            return;
        }
        
        if (mName.getText().trim().isEmpty() || mCal.getText().trim().isEmpty()) {
            mealError.setText(" Nom et calories requis");
            return;
        }

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
        showSuccess("Repas ajouté");
    }

    private void handleUpdateMeal() {
        DailyMealLog selected = tableMeals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            mealError.setText("Sélectionnez un repas");
            return;
        }

        selected.setMealType(mName.getText());
        selected.setCalories(safeParseInt(mCal.getText()));
        selected.setProtein(safeParseInt(mProt.getText()));
        selected.setCarbs(safeParseInt(mCarb.getText()));
        selected.setFat(safeParseInt(mFat.getText()));
        selected.setHealthScore(safeParseInt(mScore.getText()));
        selected.setNotes(mNotes.getText());

        sMeal.modifier(selected);
        refreshMeals(selected.getPlanId());
        showSuccess("Repas modifié");
    }

    private void handleDeleteMeal() {
        DailyMealLog selected = tableMeals.getSelectionModel().getSelectedItem();
        if (selected == null) {
            mealError.setText(" Sélectionnez un repas");
            return;
        }

        if (confirm("Supprimer ce repas ?")) {
            sMeal.supprimer(selected.getId());
            refreshMeals(selected.getPlanId());
            clearMealForm();
            showSuccess("Repas supprimé");
        }
    }

    // ======================================================
    // UTILS
    // ======================================================

    private int safeParseInt(String text) {
        if (text == null || text.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void refreshPlans() { 
        tablePlans.getItems().setAll(sPlan.getAll()); 
    }
    
    private void refreshMeals(int id) { 
        tableMeals.getItems().setAll(sMeal.getByPlan(id)); 
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

    private void setupValidation() {
        TextField[] numeric = {pCal, pProt, pCarb, pFat, mCal, mProt, mCarb, mFat, mScore};
        for (TextField f : numeric) {
            f.textProperty().addListener((obs, old, val) -> {
                if (!val.matches("\\d*")) f.setText(val.replaceAll("[^\\d]", ""));
            });
        }
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + darken(color) + "; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;"));
    }

    private String darken(String color) {
        switch(color) {
            case "#4CAF50": return "#45a049";
            case "#2196F3": return "#1976D2";
            case "#f44336": return "#d32f2f";
            default: return color;
        }
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    private boolean confirm(String msg) {
        return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL).showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
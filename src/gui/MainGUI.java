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

public class MainGUI extends Application {

    // ===== SERVICES =====
    private ServiceNutritionPlan sPlan = new ServiceNutritionPlan();
    private ServiceDailyMealLog sMeal = new ServiceDailyMealLog();

    // ===== TABLES =====
    private TableView<NutritionPlan> tablePlans = new TableView<>();
    private TableView<DailyMealLog> tableMeals = new TableView<>();

    // ===== PLAN FIELDS =====
    private TextField pGoal = new TextField();
    private TextField pCal = new TextField();
    private TextField pProt = new TextField();
    private TextField pCarb = new TextField();
    private TextField pFat = new TextField();

    // ===== MEAL FIELDS =====
    private TextField mName = new TextField();
    private TextField mCal = new TextField();
    private TextField mProt = new TextField();
    private TextField mCarb = new TextField();
    private TextField mFat = new TextField();
    private TextField mScore = new TextField();
    private TextField mNotes = new TextField();

    @Override
    public void start(Stage stage) {

        setupNumericValidation(
                pCal, pProt, pCarb, pFat,
                mCal, mProt, mCarb, mFat, mScore
        );

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // ===== TOP (PLANS) =====
        setupPlanTable();
        HBox top = new HBox(20,
                createPlanForm(),
                new VBox(10, new Label("Plans Nutritionnels"), tablePlans)
        );
        HBox.setHgrow(tablePlans, Priority.ALWAYS);

        // ===== BOTTOM (MEALS) =====
        setupMealTable();
        HBox bottom = new HBox(20,
                createMealForm(),
                new VBox(10, new Label("Repas Associés"), tableMeals)
        );
        HBox.setHgrow(tableMeals, Priority.ALWAYS);

        root.getChildren().addAll(top, new Separator(), bottom);

        // ===== MASTER DETAIL =====
        tablePlans.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                pGoal.setText(sel.getGoalType());
                pCal.setText(String.valueOf(sel.getCalorieTarget()));
                pProt.setText(String.valueOf(sel.getProteinTarget()));
                pCarb.setText(String.valueOf(sel.getCarbTarget()));
                pFat.setText(String.valueOf(sel.getFatTarget()));
                refreshMeals(sel.getId());
            }
        });

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

        Scene scene = new Scene(root, 1300, 850);
        refreshPlans();

        stage.setTitle("FitLife Dashboard - FULL CRUD");
        stage.setScene(scene);
        stage.show();
    }

    // ======================================================
    // 🟢 PLAN FORM
    // ======================================================

    private VBox createPlanForm() {

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);

        g.add(new Label("Objectif:"), 0, 0); g.add(pGoal, 1, 0);
        g.add(new Label("Calories:"), 0, 1); g.add(pCal, 1, 1);
        g.add(new Label("Proteines:"), 0, 2); g.add(pProt, 1, 2);
        g.add(new Label("Glucides:"), 0, 3); g.add(pCarb, 1, 3);
        g.add(new Label("Lipides:"), 0, 4); g.add(pFat, 1, 4);

        Button add = new Button("Ajouter");
        add.setOnAction(e -> {
            sPlan.ajouter(new NutritionPlan(
                    0,
                    val(pCal),
                    val(pProt),
                    val(pCarb),
                    val(pFat),
                    pGoal.getText()
            ));
            refreshPlans();
            clearPlan();
        });

        Button mod = new Button("Modifier");
        mod.setOnAction(e -> {
            NutritionPlan sel = tablePlans.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sel.setGoalType(pGoal.getText());
                sel.setCalorieTarget(val(pCal));
                sel.setProteinTarget(val(pProt));
                sel.setCarbTarget(val(pCarb));
                sel.setFatTarget(val(pFat));
                sPlan.modifier(sel);
                refreshPlans();
            }
        });

        Button del = new Button("Supprimer");
        del.setOnAction(e -> {
            NutritionPlan sel = tablePlans.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sPlan.supprimer(sel.getId());
                refreshPlans();
                tableMeals.getItems().clear();
            }
        });

        VBox box = new VBox(10, new Label("Gestion Plan"), g, new HBox(10, add, mod, del));
        box.setPrefWidth(320);
        return box;
    }

    // ======================================================
    // 🍽️ MEAL FORM
    // ======================================================

    private VBox createMealForm() {

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);

        g.add(new Label("Repas:"), 0, 0); g.add(mName, 1, 0);
        g.add(new Label("Calories:"), 0, 1); g.add(mCal, 1, 1);
        g.add(new Label("Proteines:"), 0, 2); g.add(mProt, 1, 2);
        g.add(new Label("Glucides:"), 0, 3); g.add(mCarb, 1, 3);
        g.add(new Label("Lipides:"), 0, 4); g.add(mFat, 1, 4);
        g.add(new Label("Score:"), 0, 5); g.add(mScore, 1, 5);
        g.add(new Label("Notes:"), 0, 6); g.add(mNotes, 1, 6);

        Button add = new Button("Ajouter Repas");
        add.setOnAction(e -> {
            NutritionPlan s = tablePlans.getSelectionModel().getSelectedItem();
            if (s != null) {
                sMeal.ajouter(new DailyMealLog(
                        0,
                        s.getId(),
                        java.time.LocalDate.now().toString(),
                        mName.getText(),
                        val(mCal),
                        val(mProt),
                        val(mCarb),
                        val(mFat),
                        val(mScore),
                        mNotes.getText()
                ));
                refreshMeals(s.getId());
                clearMeal();
            }
        });

        Button mod = new Button("Modifier Repas");
        mod.setOnAction(e -> {
            DailyMealLog sel = tableMeals.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sel.setMealType(mName.getText());
                sel.setCalories(val(mCal));
                sel.setProtein(val(mProt));
                sel.setCarbs(val(mCarb));
                sel.setFat(val(mFat));
                sel.setHealthScore(val(mScore));
                sel.setNotes(mNotes.getText());
                sMeal.modifier(sel);
                refreshMeals(sel.getPlanId());
            }
        });

        Button del = new Button("Supprimer Repas");
        del.setOnAction(e -> {
            DailyMealLog sel = tableMeals.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sMeal.supprimer(sel.getId());
                refreshMeals(sel.getPlanId());
            }
        });

        VBox box = new VBox(10, new Label("Gestion Repas"), g, new HBox(10, add, mod, del));
        box.setPrefWidth(320);
        return box;
    }

    // ======================================================
    // TABLES
    // ======================================================

    private void setupPlanTable() {
        tablePlans.getColumns().addAll(
                col("Objectif", "goalType"),
                col("Calories", "calorieTarget"),
                col("Proteines", "proteinTarget"),
                col("Glucides", "carbTarget"),
                col("Lipides", "fatTarget")
        );
    }

    private void setupMealTable() {
        tableMeals.getColumns().addAll(
                col("Repas", "mealType"),
                col("Calories", "calories"),
                col("Proteines", "protein"),
                col("Glucides", "carbs"),
                col("Lipides", "fat"),
                col("Score", "healthScore")
        );
    }

    private <T> TableColumn<T, ?> col(String title, String prop) {
        TableColumn<T, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        return c;
    }

    // ======================================================
    // UTILS
    // ======================================================

    private void setupNumericValidation(TextField... fields) {
        for (TextField f : fields) {
            f.textProperty().addListener((obs, o, v) -> {
                if (!v.matches("\\d*")) f.setText(v.replaceAll("[^\\d]", ""));
            });
        }
    }

    private int val(TextField t) {
        return t.getText().isEmpty() ? 0 : Integer.parseInt(t.getText());
    }

    private void refreshPlans() { tablePlans.getItems().setAll(sPlan.getAll()); }
    private void refreshMeals(int id) { tableMeals.getItems().setAll(sMeal.getByPlan(id)); }

    private void clearPlan() { pGoal.clear(); pCal.clear(); pProt.clear(); pCarb.clear(); pFat.clear(); }
    private void clearMeal() { mName.clear(); mCal.clear(); mProt.clear(); mCarb.clear(); mFat.clear(); mScore.clear(); mNotes.clear(); }

    public static void main(String[] args) { launch(args); }
}
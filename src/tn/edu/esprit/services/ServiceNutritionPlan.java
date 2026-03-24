package tn.edu.esprit.services;

import tn.edu.esprit.entities.NutritionPlan;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceNutritionPlan {
    private Connection cnx;

    public ServiceNutritionPlan() {
        cnx = DataSource.getConnection();
    }

    // AJOUTER UN PLAN
    public void ajouter(NutritionPlan p) {
        String req = "INSERT INTO nutrition_plans (user_id, goal_type, bmr_calories, daily_calories, " +
                     "protein_target, carb_target, fat_target, start_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getGoalType());
            ps.setInt(3, p.getBmrCalories());
            ps.setInt(4, p.getDailyCalories());
            ps.setInt(5, p.getProteinTarget());
            ps.setInt(6, p.getCarbTarget());
            ps.setInt(7, p.getFatTarget());
            ps.setDate(8, Date.valueOf(p.getStartDate()));
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
            }
            System.out.println("✅ Plan ajouté ID: " + p.getId());
        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout plan: " + e.getMessage());
        }
    }

    // RÉCUPÉRER TOUS LES PLANS
    public List<NutritionPlan> getAll() {
        List<NutritionPlan> list = new ArrayList<>();
        String req = "SELECT * FROM nutrition_plans ORDER BY id DESC";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                NutritionPlan p = new NutritionPlan(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("goal_type"),
                    rs.getInt("bmr_calories"),
                    rs.getInt("daily_calories"),
                    rs.getInt("protein_target"),
                    rs.getInt("carb_target"),
                    rs.getInt("fat_target"),
                    rs.getInt("adherence_score"),
                    rs.getInt("streak_days"),
                    rs.getInt("total_points"),
                    rs.getString("level"),
                    rs.getString("status"),
                    rs.getDate("start_date").toLocalDate()
                );
                list.add(p);
            }
            System.out.println("📊 Plans chargés: " + list.size());
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll: " + e.getMessage());
        }
        return list;
    }

    // RÉCUPÉRER UN PLAN PAR ID
    public NutritionPlan getOne(int id) {
        String req = "SELECT * FROM nutrition_plans WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new NutritionPlan(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("goal_type"),
                    rs.getInt("bmr_calories"),
                    rs.getInt("daily_calories"),
                    rs.getInt("protein_target"),
                    rs.getInt("carb_target"),
                    rs.getInt("fat_target"),
                    rs.getInt("adherence_score"),
                    rs.getInt("streak_days"),
                    rs.getInt("total_points"),
                    rs.getString("level"),
                    rs.getString("status"),
                    rs.getDate("start_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // MODIFIER UN PLAN
    public void modifier(NutritionPlan p) {
        String req = "UPDATE nutrition_plans SET goal_type=?, daily_calories=?, protein_target=?, " +
                     "carb_target=?, fat_target=?, status=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getGoalType());
            ps.setInt(2, p.getDailyCalories());
            ps.setInt(3, p.getProteinTarget());
            ps.setInt(4, p.getCarbTarget());
            ps.setInt(5, p.getFatTarget());
            ps.setString(6, p.getStatus());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SUPPRIMER UN PLAN
    public void supprimer(int id) {
        String req = "DELETE FROM nutrition_plans WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // METTRE À JOUR LE SCORE D'ADHÉRENCE
    public void updateAdherenceScore(int planId, String date) {
        try {
            PreparedStatement ps = cnx.prepareStatement(
                "SELECT SUM(calories_per_100g * quantity_g / 100) as total_cal " +
                "FROM food_items WHERE plan_id = ? AND meal_date = ?"
            );
            ps.setInt(1, planId);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int totalCalories = rs.getInt("total_cal");
                NutritionPlan plan = getOne(planId);
                if (plan != null && plan.getDailyCalories() > 0) {
                    int newScore = (int)((totalCalories * 100.0) / plan.getDailyCalories());
                    if (newScore > 100) newScore = 100;
                    
                    PreparedStatement ps2 = cnx.prepareStatement(
                        "UPDATE nutrition_plans SET adherence_score = ? WHERE id = ?"
                    );
                    ps2.setInt(1, newScore);
                    ps2.setInt(2, planId);
                    ps2.executeUpdate();
                    
                    PreparedStatement ps3 = cnx.prepareStatement(
                        "INSERT INTO adherence_history (plan_id, check_date, adherence_score, calories_consumed) " +
                        "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                        "adherence_score = ?, calories_consumed = ?"
                    );
                    ps3.setInt(1, planId);
                    ps3.setString(2, date);
                    ps3.setInt(3, newScore);
                    ps3.setInt(4, totalCalories);
                    ps3.setInt(5, newScore);
                    ps3.setInt(6, totalCalories);
                    ps3.executeUpdate();
                    
                    System.out.println("📊 Score mis à jour: " + newScore + "%");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur updateAdherenceScore: " + e.getMessage());
        }
    }
}
package tn.edu.esprit.services;

import tn.edu.esprit.entities.NutritionPlan;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceNutritionPlan {
    private Connection cnx = DataSource.getInstance().getConnection();

    // AJOUTER
    public void ajouter(NutritionPlan p) {
        String req = "INSERT INTO nutrition_plan (calorie_target, protein_target_g, carb_target_g, fat_target_g, goal_type, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getCalorieTarget());
            ps.setInt(2, p.getProteinTarget());
            ps.setInt(3, p.getCarbTarget());
            ps.setInt(4, p.getFatTarget());
            ps.setString(5, p.getGoalType());
            ps.setInt(6, 1); // user_id par défaut
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                p.setId(rs.getInt(1));
            }
            System.out.println("✅ Plan ajouté avec ID: " + p.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // MODIFIER
    public void modifier(NutritionPlan p) {
        String req = "UPDATE nutrition_plan SET calorie_target=?, protein_target_g=?, carb_target_g=?, fat_target_g=?, goal_type=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, p.getCalorieTarget());
            ps.setInt(2, p.getProteinTarget());
            ps.setInt(3, p.getCarbTarget());
            ps.setInt(4, p.getFatTarget());
            ps.setString(5, p.getGoalType());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
            System.out.println("✏️ Plan modifié !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SUPPRIMER
    public void supprimer(int id) {
        // D'abord supprimer les repas associés (contrainte foreign key)
        String deleteMeals = "DELETE FROM daily_meal_log WHERE plan_id=?";
        try (PreparedStatement ps = cnx.prepareStatement(deleteMeals)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Puis supprimer le plan
        String req = "DELETE FROM nutrition_plan WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("🗑️ Plan supprimé !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GET ALL
    public List<NutritionPlan> getAll() {
        List<NutritionPlan> list = new ArrayList<>();
        String req = "SELECT * FROM nutrition_plan";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                list.add(new NutritionPlan(
                    rs.getInt("id"),
                    rs.getInt("calorie_target"),
                    rs.getInt("protein_target_g"),
                    rs.getInt("carb_target_g"),
                    rs.getInt("fat_target_g"),
                    rs.getString("goal_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // GET ONE
    public NutritionPlan getOne(int id) {
        String req = "SELECT * FROM nutrition_plan WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new NutritionPlan(
                    rs.getInt("id"),
                    rs.getInt("calorie_target"),
                    rs.getInt("protein_target_g"),
                    rs.getInt("carb_target_g"),
                    rs.getInt("fat_target_g"),
                    rs.getString("goal_type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
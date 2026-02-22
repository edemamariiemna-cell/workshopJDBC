package tn.edu.esprit.services;

import tn.edu.esprit.entities.DailyMealLog;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDailyMealLog {
    private Connection cnx = DataSource.getInstance().getConnection();

    // AJOUTER
    public void ajouter(DailyMealLog m) {
        String req = "INSERT INTO daily_meal_log (plan_id, date, meal_type, calories, protein_g, carbs_g, fat_g, health_score, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getPlanId());
            ps.setString(2, m.getDate());
            ps.setString(3, m.getMealType());
            ps.setInt(4, m.getCalories());
            ps.setInt(5, m.getProtein());
            ps.setInt(6, m.getCarbs());
            ps.setInt(7, m.getFat());
            ps.setInt(8, m.getHealthScore());
            ps.setString(9, m.getNotes());
            
            ps.executeUpdate();
            
            // Récupérer l'ID généré
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setId(rs.getInt(1));
            }
            
            System.out.println("Repas ajouté avec plan_id: " + m.getPlanId()); // ← LOG
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    // MODIFIER
    public void modifier(DailyMealLog m) {
        String req = "UPDATE daily_meal_log SET meal_type=?, calories=?, protein_g=?, carbs_g=?, fat_g=?, health_score=?, notes=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, m.getMealType());
            ps.setInt(2, m.getCalories());
            ps.setInt(3, m.getProtein());
            ps.setInt(4, m.getCarbs());
            ps.setInt(5, m.getFat());
            ps.setInt(6, m.getHealthScore());
            ps.setString(7, m.getNotes());
            ps.setInt(8, m.getId());
            ps.executeUpdate();
            System.out.println(" Repas modifié !");
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    // SUPPRIMER
    public void supprimer(int id) {
        String req = "DELETE FROM daily_meal_log WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(" Repas supprimé !");
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    // GET BY PLAN
    public List<DailyMealLog> getByPlan(int planId) {
        List<DailyMealLog> list = new ArrayList<>();
        String req = "SELECT * FROM daily_meal_log WHERE plan_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, planId);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("Recherche repas pour plan_id: " + planId); // ← LOG
            
            while (rs.next()) {
                DailyMealLog m = new DailyMealLog(
                    rs.getInt("id"),
                    rs.getInt("plan_id"),
                    rs.getString("date"),
                    rs.getString("meal_type"),
                    rs.getInt("calories"),
                    rs.getInt("protein_g"),
                    rs.getInt("carbs_g"),
                    rs.getInt("fat_g"),
                    rs.getInt("health_score"),
                    rs.getString("notes")
                );
                list.add(m);
                System.out.println("Repas trouvé: " + m.getMealType()); // ← LOG
            }
            
            System.out.println("Total repas trouvés: " + list.size()); // ← LOG
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }
    // GET ONE
    public DailyMealLog getOne(int id) {
        String req = "SELECT * FROM daily_meal_log WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DailyMealLog(
                    rs.getInt("id"),
                    rs.getInt("plan_id"),
                    rs.getString("date"),
                    rs.getString("meal_type"),
                    rs.getInt("calories"),
                    rs.getInt("protein_g"),
                    rs.getInt("carbs_g"),
                    rs.getInt("fat_g"),
                    rs.getInt("health_score"),
                    rs.getString("notes")
                );
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null;
    }
}
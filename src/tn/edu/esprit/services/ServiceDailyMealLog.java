package tn.edu.esprit.services;

import tn.edu.esprit.entities.DailyMealLog;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDailyMealLog {
    private Connection cnx = DataSource.getInstance().getConnection();

    public void ajouter(DailyMealLog m) {
        String req = "INSERT INTO daily_meal_log (plan_id, date, meal_type, calories, protein_g, carbs_g, fat_g, health_score, notes) VALUES (?, NOW(), ?, ?, ?, ?, ?, 0, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, m.getPlanId());
            ps.setString(2, m.getMealType());
            ps.setInt(3, m.getCalories());
            ps.setInt(4, m.getProtein());
            ps.setInt(5, m.getCarbs());
            ps.setInt(6, m.getFat());
            ps.setString(7, m.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void modifier(DailyMealLog m) {
        String req = "UPDATE daily_meal_log SET meal_type=?, calories=?, protein_g=?, carbs_g=?, fat_g=?, notes=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, m.getMealType());
            ps.setInt(2, m.getCalories());
            ps.setInt(3, m.getProtein());
            ps.setInt(4, m.getCarbs());
            ps.setInt(5, m.getFat());
            ps.setString(6, m.getNotes());
            ps.setInt(7, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void supprimer(int id) {
        try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM daily_meal_log WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<DailyMealLog> getByPlan(int planId) {
        List<DailyMealLog> list = new ArrayList<>();
        try (PreparedStatement ps = cnx.prepareStatement("SELECT * FROM daily_meal_log WHERE plan_id=?")) {
            ps.setInt(1, planId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DailyMealLog(rs.getInt("id"), rs.getInt("plan_id"), rs.getString("date"), rs.getString("meal_type"),
                        rs.getInt("calories"), rs.getInt("protein_g"), rs.getInt("carbs_g"), rs.getInt("fat_g"), rs.getInt("health_score"), rs.getString("notes")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
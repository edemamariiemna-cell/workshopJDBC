package tn.edu.esprit.services;

import tn.edu.esprit.entities.FoodItem;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFoodItem {
    private Connection cnx;

    public ServiceFoodItem() {
        cnx = DataSource.getConnection();
    }

    // AJOUTER UN ALIMENT
    public void ajouter(FoodItem f) {
        String req = "INSERT INTO food_items (plan_id, name, calories_per_100g, protein_per_100g, " +
                     "carbs_per_100g, fat_per_100g, quantity_g, glycemic_index, allergens, " +
                     "is_recommended, meal_type, meal_date, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, f.getPlanId());
            ps.setString(2, f.getName());
            ps.setInt(3, f.getCaloriesPer100g());
            ps.setFloat(4, f.getProteinPer100g());
            ps.setFloat(5, f.getCarbsPer100g());
            ps.setFloat(6, f.getFatPer100g());
            ps.setFloat(7, f.getQuantityG());
            ps.setInt(8, f.getGlycemicIndex());
            ps.setString(9, f.getAllergens());
            ps.setBoolean(10, f.isRecommended());
            ps.setString(11, f.getMealType());
            ps.setString(12, f.getMealDate());
            ps.setString(13, f.getCategory());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                f.setId(rs.getInt(1));
            }
            System.out.println("✅ Aliment ajouté: " + f.getName());
        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout aliment: " + e.getMessage());
        }
    }

    // RÉCUPÉRER TOUS LES ALIMENTS
    public List<FoodItem> getAll() {
        List<FoodItem> list = new ArrayList<>();
        String req = "SELECT * FROM food_items ORDER BY meal_date DESC, id DESC";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                FoodItem f = new FoodItem(
                    rs.getInt("id"),
                    rs.getInt("plan_id"),
                    rs.getString("name"),
                    rs.getInt("calories_per_100g"),
                    rs.getFloat("protein_per_100g"),
                    rs.getFloat("carbs_per_100g"),
                    rs.getFloat("fat_per_100g"),
                    rs.getFloat("quantity_g"),
                    rs.getInt("glycemic_index"),
                    rs.getString("allergens"),
                    rs.getBoolean("is_recommended"),
                    rs.getString("meal_type"),
                    rs.getString("meal_date"),
                    rs.getString("category")
                );
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // MODIFIER UN ALIMENT
    public void modifier(FoodItem f) {
        String req = "UPDATE food_items SET name=?, calories_per_100g=?, protein_per_100g=?, " +
                     "carbs_per_100g=?, fat_per_100g=?, quantity_g=?, glycemic_index=?, " +
                     "allergens=?, meal_type=?, category=?, meal_date=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, f.getName());
            ps.setInt(2, f.getCaloriesPer100g());
            ps.setFloat(3, f.getProteinPer100g());
            ps.setFloat(4, f.getCarbsPer100g());
            ps.setFloat(5, f.getFatPer100g());
            ps.setFloat(6, f.getQuantityG());
            ps.setInt(7, f.getGlycemicIndex());
            ps.setString(8, f.getAllergens());
            ps.setString(9, f.getMealType());
            ps.setString(10, f.getCategory());
            ps.setString(11, f.getMealDate());
            ps.setInt(12, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SUPPRIMER UN ALIMENT
    public void supprimer(int id) {
        String req = "DELETE FROM food_items WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // RÉCUPÉRER LES ALIMENTS PAR PLAN
    public List<FoodItem> getByPlanId(int planId) {
        List<FoodItem> list = new ArrayList<>();
        String req = "SELECT * FROM food_items WHERE plan_id = ? ORDER BY meal_date DESC";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, planId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FoodItem f = new FoodItem(
                    rs.getInt("id"),
                    rs.getInt("plan_id"),
                    rs.getString("name"),
                    rs.getInt("calories_per_100g"),
                    rs.getFloat("protein_per_100g"),
                    rs.getFloat("carbs_per_100g"),
                    rs.getFloat("fat_per_100g"),
                    rs.getFloat("quantity_g"),
                    rs.getInt("glycemic_index"),
                    rs.getString("allergens"),
                    rs.getBoolean("is_recommended"),
                    rs.getString("meal_type"),
                    rs.getString("meal_date"),
                    rs.getString("category")
                );
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
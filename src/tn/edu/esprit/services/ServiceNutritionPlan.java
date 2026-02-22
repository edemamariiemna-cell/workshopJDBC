package tn.edu.esprit.services;

import tn.edu.esprit.entities.NutritionPlan;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceNutritionPlan {

    private Connection cnx = DataSource.getInstance().getConnection();

    // -------- AJOUTER --------
   
    public void ajouter(NutritionPlan p) {
        // Ajout de 'user_id' dans la requête SQL
        String req = "INSERT INTO nutrition_plan (calorie_target, protein_target_g, carb_target_g, fat_target_g, goal_type, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, p.getCalorieTarget());
            ps.setInt(2, p.getProteinTarget());
            ps.setInt(3, p.getCarbTarget());
            ps.setInt(4, p.getFatTarget());
            ps.setString(5, p.getGoalType());
            
            // FIX: Fournir l'ID de l'utilisateur (assurez-vous que l'ID 1 existe en BDD)
            ps.setInt(6, 1); 
            
            ps.executeUpdate();
            System.out.println("✅ Ajout réussi à la base de données !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //-------- MODIFIER --------
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

    // -------- SUPPRIMER --------
    public void supprimer(int id) {

        try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM nutrition_plan WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("🗑️ Plan supprimé !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------- GET ALL --------
    public List<NutritionPlan> getAll() {

        List<NutritionPlan> list = new ArrayList<>();

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM nutrition_plan")) {

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
}
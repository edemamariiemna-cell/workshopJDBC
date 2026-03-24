package tn.edu.esprit.services;

import tn.edu.esprit.entities.AdherenceHistory;
import tn.edu.esprit.tools.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAdherenceHistory {
    private Connection cnx = DataSource.getConnection();

    public List<AdherenceHistory> getByPlanId(int planId) {
        List<AdherenceHistory> list = new ArrayList<>();
        String req = "SELECT * FROM adherence_history WHERE plan_id = ? ORDER BY check_date DESC";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, planId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AdherenceHistory h = new AdherenceHistory(
                    rs.getInt("id"),
                    rs.getInt("plan_id"),
                    rs.getString("check_date"),
                    rs.getInt("adherence_score"),
                    rs.getInt("calories_consumed")
                );
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public void ajouter(AdherenceHistory h) {
        String req = "INSERT INTO adherence_history (plan_id, check_date, adherence_score, calories_consumed) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, h.getPlanId());
            ps.setString(2, h.getCheckDate());
            ps.setInt(3, h.getAdherenceScore());
            ps.setInt(4, h.getCaloriesConsumed());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
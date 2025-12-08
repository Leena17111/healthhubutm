package com.secj3303.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.secj3303.model.Person;

@Repository
public class PersonDaoJdbc implements PersonDao {

    private final DataSource dataSource;
    

    public PersonDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT * FROM person ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Person p = new Person();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setYob(rs.getInt("yob"));
                p.setAge(rs.getInt("age"));
                p.setWeight(rs.getDouble("weight"));
                p.setHeight(rs.getDouble("height"));
                p.setBmi(rs.getDouble("bmi"));
                p.setCategory(rs.getString("category"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Person findById(int id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        Person p = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Person();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setYob(rs.getInt("yob"));
                    p.setAge(rs.getInt("age"));
                    p.setWeight(rs.getDouble("weight"));
                    p.setHeight(rs.getDouble("height"));
                    p.setBmi(rs.getDouble("bmi"));
                    p.setCategory(rs.getString("category"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p;
    }

    @Override
    public void insert(Person p) {
        String sql = "INSERT INTO person (name, yob, age, weight, height, bmi, category) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getYob());
            ps.setInt(3, p.getAge());
            ps.setDouble(4, p.getWeight());
            ps.setDouble(5, p.getHeight());
            ps.setDouble(6, p.getBmi());
            ps.setString(7, p.getCategory());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Person p) {
        String sql = "UPDATE person SET name=?, yob=?, age=?, weight=?, height=?, bmi=?, category=? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getYob());
            ps.setInt(3, p.getAge());
            ps.setDouble(4, p.getWeight());
            ps.setDouble(5, p.getHeight());
            ps.setDouble(6, p.getBmi());
            ps.setString(7, p.getCategory());
            ps.setInt(8, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM person WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

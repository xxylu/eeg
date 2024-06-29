package org.example;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

@Controller
public class ImageController {
    private static final String URL = "jdbc:sqlite:/Users/oliwierpachulski/Projects/eeg/src/main/resources/database.db";

    @GetMapping("/image")
    public String image(@RequestParam String name, @RequestParam int elecnum, Model model ) {
        String image= null;
        String sql = "SELECT image FROM user_eeg WHERE username = ? AND electrode_number = ?";
        if(elecnum > 18){
            model.addAttribute("name", "SE");
            model.addAttribute("elecnum", "Bad");
            String line = null;
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader("xd.txt"))){
                line = bufferedReader.readLine();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            model.addAttribute("image", line);
            return "eegimage";
        }

        try (Connection connection = DriverManager.getConnection(URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, elecnum);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    image = rs.getString("image");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }

        model.addAttribute("username", name);
        model.addAttribute("electrode", elecnum);
        model.addAttribute("image", image);
        return "eegimage";
    }
}

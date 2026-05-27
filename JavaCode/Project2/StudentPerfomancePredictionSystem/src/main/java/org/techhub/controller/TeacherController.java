package org.techhub.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.techhub.model.Contact;
import org.techhub.model.Performance;
import org.techhub.model.User;
import org.techhub.services.ContactService;
import org.techhub.services.PerformanceService;

@Controller
public class TeacherController {
	
    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/teacher/dashboard")
    public String dashboard() {
        return "TeacherDashboard";
    }

    @GetMapping("/teacher/addPerformance")
    public String addPerformance() {
        return "AddPerformance";
    }

    @GetMapping("/teacher/predict")
    public String predictPage() {
        return "PredictPerformance";
    }

    @PostMapping("/teacher/savePerformance")
    public String save(@ModelAttribute("p") Performance p) {
        performanceService.addPerformance(p);
        return "redirect:/teacher/viewPerformance";
    }

    @GetMapping("/teacher/viewPerformance")
    public String viewPerformance(Model model){
        model.addAttribute("list", performanceService.getAllPerformance());
        return "ViewPerformance";
    }

    @PostMapping("/teacher/updatePerformance")
    public String updatePerformance(@ModelAttribute Performance p) {
        performanceService.updatePerformance(p);
        return "redirect:/teacher/viewPerformance";
    }

    @GetMapping("/teacher/deletePerformance")
    public String deletePerformance(@RequestParam(required = false) Integer id) {
        if (id != null) {
            performanceService.deletePerformance(id);
        }
        return "redirect:/teacher/viewPerformance";
    }

    @GetMapping("/teacher/addStudent")
    public String addStudent() {
        return "AddStudent";
    }

    @GetMapping("/teacher/viewStudents")
    public String viewStudents() {
        return "ViewStudents";
    }

    @PostMapping("/saveContact")
    public String saveContact(@ModelAttribute Contact contact) {
        contactService.saveContact(contact);
        return "redirect:/";
    }

    @GetMapping("/teacher/enquiry")
    public String enquiry(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "Enquiry";
    }
    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {

        String name = (String) session.getAttribute("studentName");

        List<Performance> list = performanceService.getByStudentName(name);

        model.addAttribute("performanceList", list);

        return "StudentDashboard";
    }
    @ResponseBody 
    @PostMapping("/chat")
     // 🔥 MOST IMPORTANT LINE
    public String chat(@RequestParam("message") String message) {

        try {
            String apiKey = "YOUR_API_KEY_HERE"; 

            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String body = "{"
                    + "\"model\":\"gpt-4.1-mini\","
                    + "\"messages\":[{\"role\":\"user\",\"content\":\"" + message + "\"}]"
                    + "}";

            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output, result = "";

            while ((output = br.readLine()) != null) {
                result += output;
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"choices\":[{\"message\":{\"content\":\"Server error 😢\"}}]}";
        }
    }
    
}
package com.dbmsproject.upsideavenue.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbmsproject.upsideavenue.models.AgentReport;
import com.dbmsproject.upsideavenue.models.SalesReport;
import com.dbmsproject.upsideavenue.repositories.SalesRepository;

@Controller
@RequestMapping("")
@Secured({ "ADMIN" })
public class AdminController {

    @Autowired
    private SalesRepository salesRepository;

    @GetMapping("/reports")
    public String agentReport(Model model) {
        List<AgentReport> reports = salesRepository.findAllAgentReport();

        model.addAttribute("reports", reports);

        return "agentReport";
    }

    @GetMapping("/reports/{agentID}")
    public String salesReport(@PathVariable String agentID, Model model) {
        List<SalesReport> reports = salesRepository.findAllSalesReportByAgent(agentID);

        model.addAttribute("agent", agentID);
        model.addAttribute("reports", reports);

        return "salesReport";
    }
}

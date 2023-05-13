package com.dbmsproject.upsideavenue.controllers;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.dbmsproject.upsideavenue.models.Post;
import com.dbmsproject.upsideavenue.models.PostStatus;
import com.dbmsproject.upsideavenue.models.Property;
import com.dbmsproject.upsideavenue.models.PurchaseRequest;
import com.dbmsproject.upsideavenue.models.Rent;
import com.dbmsproject.upsideavenue.models.Sales;
import com.dbmsproject.upsideavenue.models.primaryIds;
import com.dbmsproject.upsideavenue.repositories.PostRepository;
import com.dbmsproject.upsideavenue.repositories.PropertyRepository;
import com.dbmsproject.upsideavenue.repositories.PurchaseRequestRepository;
import com.dbmsproject.upsideavenue.repositories.RentRepository;
import com.dbmsproject.upsideavenue.repositories.SalesRepository;

@Controller
@RequestMapping("")
@Secured({ "AGENT" })
public class AgentController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private RentRepository rentRepository;

    @GetMapping("/myposts")
    public String posts(Model model) {
        model.addAttribute("posts", postRepository
                .findAllPostByAgent(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "posts";
    }

    @GetMapping("/requests")
    public String requests(Model model) {
        model.addAttribute("posts", postRepository
                .findAllPostByAgent(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "requests";
    }

    @GetMapping("/requests/{postID}")
    public String postRequests(@PathVariable UUID postID, Model model) {
        List<PurchaseRequest> requests = purchaseRequestRepository.findAllPurchaseRequestByPost(postID);

        Post post = postRepository.findById(postID).orElse(null);

        model.addAttribute("requests", requests);
        if (post != null)
            model.addAttribute("title", post.getPropertyId().getPropertyName());
        return "requests";
    }

    @PostMapping("/requests/sell/{requestID}")
    public RedirectView sell(@PathVariable UUID requestID, Model model) {

        PurchaseRequest request = purchaseRequestRepository.findById(requestID).orElse(null);

        if (request != null) {
            Post post = request.getPost();
            Property property = post.getPropertyId();

            List<PurchaseRequest> requests = purchaseRequestRepository
                    .findAllPurchaseRequestByPost(request.getPost().getPostId());

            Sales sale = new Sales();
            sale.setSaleDate(new Date(System.currentTimeMillis()));
            sale.setAllId(new primaryIds(post, property.getOwner(), request.getBuyer()));

            post.setPostStatus(PostStatus.SOLD);
            property.setOwner(request.getBuyer());

            salesRepository.save(sale);
            propertyRepository.save(property);
            postRepository.save(post);

            purchaseRequestRepository.deleteAll(requests);
        } else {
            System.out.println("Invalid Purchase Request!");
        }

        return new RedirectView("/requests");
    }

    @PostMapping("/requests/rent/{requestID}")
    public RedirectView rent(@PathVariable UUID requestID, Model model) {

        PurchaseRequest request = purchaseRequestRepository.findById(requestID).orElse(null);

        if (request != null) {
            Post post = request.getPost();
            Property property = post.getPropertyId();

            List<PurchaseRequest> requests = purchaseRequestRepository
                    .findAllPurchaseRequestByPost(request.getPost().getPostId());

            Sales sale = new Sales();
            sale.setSaleDate(new Date(System.currentTimeMillis()));
            sale.setAllId(new primaryIds(post, property.getOwner(), request.getBuyer()));

            post.setPostStatus(PostStatus.SOLD);

            salesRepository.save(sale);

            List<Rent> rents = rentRepository.findAllByProperty(property.getPropertyId());

            Rent rent = new Rent();
            rent.setProperty(property);
            rent.setRenter(request.getBuyer());

            rentRepository.deleteAll(rents);

            rentRepository.save(rent);
            postRepository.save(post);

            purchaseRequestRepository.deleteAll(requests);
        } else {
            System.out.println("Invalid Rent Request!");
        }

        return new RedirectView("/requests");
    }

    @PostMapping("/requests/cancel/{requestID}")
    public RedirectView cancel(@PathVariable UUID requestID, Model model) {

        PurchaseRequest request = purchaseRequestRepository.findById(requestID).orElse(null);

        if (request != null) {
            purchaseRequestRepository.delete(request);
        } else {
            System.out.println("Invalid Purchase Request!");
        }

        return new RedirectView("/requests");
    }
}

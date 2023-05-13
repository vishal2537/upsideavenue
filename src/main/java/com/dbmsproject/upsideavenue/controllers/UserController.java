package com.dbmsproject.upsideavenue.controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.dbmsproject.upsideavenue.models.Photo;
import com.dbmsproject.upsideavenue.models.Post;
import com.dbmsproject.upsideavenue.models.Property;
import com.dbmsproject.upsideavenue.models.PurchaseRequest;
import com.dbmsproject.upsideavenue.models.Role;
import com.dbmsproject.upsideavenue.models.SearchPost;
import com.dbmsproject.upsideavenue.models.User;
import com.dbmsproject.upsideavenue.repositories.PhotoRepository;
import com.dbmsproject.upsideavenue.repositories.PostRepository;
import com.dbmsproject.upsideavenue.repositories.PropertyRepository;
import com.dbmsproject.upsideavenue.repositories.PurchaseRequestRepository;
import com.dbmsproject.upsideavenue.repositories.RentRepository;
import com.dbmsproject.upsideavenue.repositories.UserRepository;

@Controller
@RequestMapping("")
@Secured({ "USER" })
public class UserController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private RentRepository rentRepository;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("properties")
    public String properties(Model model) {
        model.addAttribute("properties", propertyRepository
                .findAllPropertyByOwner(SecurityContextHolder.getContext().getAuthentication().getName()));

        model.addAttribute("rented", rentRepository
                .findAllByRenter(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "properties";
    }

    @GetMapping("properties/add")
    public String addProperty(Model model) {
        model.addAttribute("property", new Property());
        return "addProperty";
    }

    @PostMapping("properties/add")
    public RedirectView addProperty(Property property, @RequestParam("images") MultipartFile[] images,
            @RequestParam("constDate") String constDate, Model model)
            throws SerialException, SQLException, IOException, ParseException {

        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        property.setOwner(owner);

        property.setConstructionDate(new Date(format.parse(constDate).getTime()));

        propertyRepository.save(property);

        for (MultipartFile image : images) {
            Photo photo = new Photo();
            photo.setPropertyId(property);
            photo.setPhoto(image.getBytes());

            photoRepository.save(photo);
        }

        return new RedirectView("/properties");
    }

    @GetMapping("properties/{propertyID}")
    public String propertyDetails(@PathVariable UUID propertyID, Model model) {
        Property property = propertyRepository.findById(propertyID).orElse(null);
        model.addAttribute("property", property);
        return "propertyDetails";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postRepository
                .findAllPostByOwner(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "posts";
    }

    @GetMapping("/posts/create")
    public String createPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("agents", userRepository.findAllUserByRole(Role.AGENT));
        model.addAttribute("properties", propertyRepository
                .findAllPropertyByOwner(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "createPost";
    }

    @PostMapping("/posts/create")
    public RedirectView createPost(Post post, Model model) {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        post.setPostDate(date);
        postRepository.save(post);

        return new RedirectView("/posts");
    }

    @GetMapping("/purchase")
    public String purchase(Model model) {
        model.addAttribute("posts", postRepository
                .findAllPostNotOwned(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("search", new SearchPost());
        return "purchase";
    }

    @PostMapping("/purchase")
    public String purchaseFilter(SearchPost search, Model model) throws ParseException {
        Date minDate = null;
        Date maxDate = null;

        if (!search.getMinDate().equals(""))
            minDate = new Date(format.parse(search.getMinDate()).getTime());
        if (!search.getMaxDate().equals(""))
            maxDate = new Date(format.parse(search.getMaxDate()).getTime());

        model.addAttribute("posts", postRepository.filter(
                search.getOwner(),
                search.getCity(),
                search.getState(),
                search.getMinPrice(),
                search.getMaxPrice(),
                search.getMode(),
                search.getMinSize(),
                search.getMaxSize(),
                search.getFurnished(),
                search.getMinBedrooms(),
                search.getMaxBedrooms(),
                minDate,
                maxDate));

        model.addAttribute("search", search);
        return "purchase";
    }

    @GetMapping("posts/{postID}")
    public String postDetails(@PathVariable UUID postID, Model model) {
        Post post = postRepository.findById(postID).orElse(null);
        model.addAttribute("post", post);

        PurchaseRequest pr = purchaseRequestRepository.findPurchaseRequestByPostAndBuyer(postID,
                SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        model.addAttribute("purchaseRequested", pr != null);

        return "postDetails";
    }

    @PostMapping("purchase/{postID}")
    public RedirectView purchase(@PathVariable UUID postID, Model model) {

        PurchaseRequest pr = purchaseRequestRepository.findPurchaseRequestByPostAndBuyer(postID,
                SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if (pr == null) {
            PurchaseRequest request = new PurchaseRequest();

            Post post = postRepository.findById(postID).orElse(null);
            request.setPost(post);

            User buyer = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            request.setBuyer(buyer);

            if (post.getPropertyId().getOwner().getUsername().equals(buyer.getUsername()))
                System.out.println("Request Already Exists!");
            else
                purchaseRequestRepository.save(request);
        } else {
            System.out.println("Request Already Exists!");
        }

        return new RedirectView("/posts/" + postID.toString());
    }

    @PostMapping("purchase/cancel/{postID}")
    public RedirectView cancelPurchase(@PathVariable UUID postID, Model model) {

        PurchaseRequest pr = purchaseRequestRepository.findPurchaseRequestByPostAndBuyer(postID,
                SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if (pr != null) {
            purchaseRequestRepository.delete(pr);
        } else {
            System.out.println("Request Doesn't Exists!");
        }

        return new RedirectView("/posts/" + postID.toString());
    }

    @GetMapping("myrequests")
    public String myrequests(Model model) {

        List<PurchaseRequest> requests = purchaseRequestRepository.findAllPurchaseRequestByBuyer(
                SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("requests", requests);

        return "requests";
    }

    @PostMapping("myrequest/cancel/{postID}")
    public RedirectView cancelRequest(@PathVariable UUID postID, Model model) {

        PurchaseRequest pr = purchaseRequestRepository.findPurchaseRequestByPostAndBuyer(postID,
                SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if (pr != null) {
            purchaseRequestRepository.delete(pr);
        } else {
            System.out.println("Request Doesn't Exists!");
        }

        return new RedirectView("/myrequests");
    }
}

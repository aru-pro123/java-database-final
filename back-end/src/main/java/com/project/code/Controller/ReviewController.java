package com.project.code.Controller;

import com.project.code.Model.Review;
import com.project.code.Model.Customer;
import com.project.code.Repo.ReviewRepository;
import com.project.code.Repo.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> reviewList = new ArrayList<>();

        List<Review> reviews = reviewRepository
                .findByStoreIdAndProductId(storeId, productId);

        for (Review review : reviews) {

            Map<String, Object> reviewData = new HashMap<>();

            reviewData.put("comment", review.getComment());
            reviewData.put("rating", review.getRating());

            String customerName = "Unknown";

            if (review.getCustomerId() != null) {
                Optional<Customer> customer =
                        customerRepository.findById(review.getCustomerId());

                if (customer.isPresent()) {
                    customerName = customer.get().getName();
                }
            }

            reviewData.put("customerName", customerName);
            reviewList.add(reviewData);
        }

        response.put("reviews", reviewList);
        return response;
    }
}
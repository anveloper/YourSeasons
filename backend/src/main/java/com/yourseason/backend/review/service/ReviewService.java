package com.yourseason.backend.review.service;

import com.yourseason.backend.common.domain.Message;
import com.yourseason.backend.common.exception.NotFoundException;
import com.yourseason.backend.common.exception.WrongAccessException;
import com.yourseason.backend.consulting.domain.Consulting;
import com.yourseason.backend.consulting.domain.ConsultingRepository;
import com.yourseason.backend.member.consultant.domain.Consultant;
import com.yourseason.backend.member.consultant.domain.ConsultantRepository;
import com.yourseason.backend.member.customer.domain.Customer;
import com.yourseason.backend.member.customer.domain.CustomerRepository;
import com.yourseason.backend.review.controller.dto.ReviewRequest;
import com.yourseason.backend.review.controller.dto.ReviewResponse;
import com.yourseason.backend.review.domain.Review;
import com.yourseason.backend.review.domain.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private static final String CUSTOMER_NOT_FOUND = "해당 고객을 찾을 수 없습니다.";
    private static final String CONSULTANT_NOT_FOUND = "해당 컨설턴트를 찾을 수 없습니다.";
    private static final String REVIEW_NOT_FOUND = "해당 리뷰를 찾을 수 없습니다.";
    private static final String CONSULTING_NOT_FOUND = "해당 컨설팅을 찾을 수 없습니다.";
    private static final String WRONG_ACCESS = "잘못된 접근입니다.";

    private final CustomerRepository customerRepository;
    private final ConsultantRepository consultantRepository;
    private final ReviewRepository reviewRepository;
    private final ConsultingRepository consultingRepository;

    public ReviewResponse createReview(Long customerId, Long consultantId, ReviewRequest reviewRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND));
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new NotFoundException(CONSULTANT_NOT_FOUND));
        Consulting consulting = consultingRepository.findById(reviewRequest.getConsultingId())
                .orElseThrow(() -> new NotFoundException(CONSULTING_NOT_FOUND));

        Review review = reviewRequest.toEntity();
        review.register(customer, consultant, consulting);
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .message("succeeded")
                .build();
    }

    public ReviewResponse updateReview(Long customerId, Long reviewId, ReviewRequest reviewRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND));
        if (!customer.equals(review.getCustomer())) {
            throw new WrongAccessException(WRONG_ACCESS);
        }

        review.updateReview(reviewRequest.getStar(), reviewRequest.getComment());
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .message("succeeded")
                .build();
    }

    public Message deleteReview(Long customerId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(REVIEW_NOT_FOUND));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND));
        if (!customer.equals(review.getCustomer())) {
            throw new WrongAccessException(WRONG_ACCESS);
        }

        review.deleteReview();
        reviewRepository.save(review);

        return new Message("succeeded");
    }
}

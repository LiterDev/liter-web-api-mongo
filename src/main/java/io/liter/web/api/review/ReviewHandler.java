package io.liter.web.api.review;

import io.liter.web.api.collection.CollectionRepository;
import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.User;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class ReviewHandler {

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final CollectionRepository collectionRepository;

    private final FollowerRepository followerRepository;

    public ReviewHandler(UserRepository userRepository
            , ReviewRepository reviewRepository
            , CollectionRepository collectionRepository
            , FollowerRepository followerRepository) {

        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.collectionRepository = collectionRepository;
        this.followerRepository = followerRepository;
    }

    /**
     * GET All Review by UserId
     */
    public Mono<ServerResponse> findAllByUserId(ServerRequest request) {
        log.info("]-----] ReviewHandler::getMainList call [-----[ ");

        Integer page = Integer.parseInt(request.queryParam("page").get());
        Integer size = Integer.parseInt(request.queryParam("size").get());

        ReviewList reviewList = new ReviewList();
        Pagination pagination = new Pagination();

        /*return
         *//*request.principal()
                .flatMap(p -> userRepository.findByUserName(p.getName()))*//*
                this.userRepository.findByUsername("test")
                        .flatMap(user -> {
                            reviewList.setUser(user);

                            return this.reviewRepository.findByUserId(user.getId(),PageRequest.of(page,size))
                                    .collectList()
                                    .map(reviews -> {
                                        reviewList.setReview(reviews);
                                        return user;
                                    });
                        })
                        .flatMap(user -> {
                            pagination.setPage(page);
                            pagination.setSize(size);

                            return this.reviewRepository.countByUserId(user.getId())
                                    .map(c -> {
                                        pagination.setTotal(c);
                                        reviewList.setPagination(pagination);

                                        return reviewList;
                                    });
                        })
                        .flatMap(result -> ok().body(BodyInserters.fromObject(reviewList)))
                        .switchIfEmpty(notFound().build());*/

        return ServerResponse.ok().build();
    }

    /**
     * GET a Review by ReviewId
     */
    public Mono<ServerResponse> findById(ServerRequest request) {

        String reviewId = request.pathVariable("id");

        ReviewDetail reviewDetail = new ReviewDetail();

        return
                /*request.principal()
                        .flatMap(p -> userRepository.findByUserName(p.getName()))*/
                this.userRepository.findByUsername("test")
                        .map(user -> {
                            reviewDetail.setUser(user);
                            return user;
                        })
                        .flatMap(r -> this.reviewRepository.findById(reviewId))
                        .map(review -> {
                            reviewDetail.setReview(review);
                            return review;
                        })
                        .flatMap(review -> {

                            return this.collectionRepository.findAllByCollectionId(review.getCollectionId())
                                    .collectList()
                                    .map(collections -> {
                                        reviewDetail.setCollection(collections);
                                        return review;
                                    });
                        })
                        .map(review -> {
                            return reviewDetail;
                        })
                        .flatMap(result -> ok().body(BodyInserters.fromObject(reviewDetail)))
                        .switchIfEmpty(notFound().build());
    }

    /**
     * GET a Review reward active
     */
    public Mono<ServerResponse> isActive(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Review> reviewMono = reviewRepository.findById(id);

        return reviewMono
                .flatMap(review -> {
                    if (review.getRewardActive() == 1) {
                        return ServerResponse.badRequest().build();
                    } else {
                        return ServerResponse.ok().build();
                    }
                }).switchIfEmpty(notFound().build());
    }

    /**
     * POST a Review
     */
    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] ReviewHandler::post call [-----[ ");

        return request.body(BodyExtractors.toMultipartData())
                .flatMap(map -> {
                    Review review = new Review();

                    Map<String, Part> parts = map.toSingleValueMap();

                    Mono<User> userMono = this.userRepository.findByUsername(((FormFieldPart) parts.get("userName")).value());

                    review.setTitle(((FormFieldPart) parts.get("title")).value());
                    review.setContent(((FormFieldPart) parts.get("content")).value());

                    return Mono.just(review);
                })
                .flatMap(review -> this.reviewRepository.save(review))
                .flatMap((r) -> ServerResponse.created(URI.create("/review/" + r.getId())).build());
    }

    /**
     * PUT a Object
     */
    public Mono<ServerResponse> put(ServerRequest request) {
        log.info("]-----] ReviewHandler::put call [-----[ ");

        return Mono
                .zip(
                        (data) -> {
                            Review r = (Review) data[0];
                            Review r2 = (Review) data[1];

                            if (r.getTitle().isEmpty() == false) {
                                r.setTitle(r2.getTitle());
                            }
                            if (r.getContent().isEmpty() == false) {
                                r.setContent(r2.getContent());
                            }

                            return r;
                        },
                        this.reviewRepository.findById(request.pathVariable("id")),
                        request.bodyToMono(Review.class)
                )
                .cast(Review.class)
                .flatMap((post) -> this.reviewRepository.save(post))
                .flatMap((post) -> noContent().build());
    }

    /**
     * DELETE a Object
     */
    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] ReviewHandler::delete call [-----[ ");

        return this.reviewRepository.findById(request.pathVariable("id"))
                .flatMap((post) -> noContent().build())
                .switchIfEmpty(notFound().build());
    }
}
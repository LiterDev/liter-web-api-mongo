package io.liter.web.api.review;

import io.liter.web.api.collection.CollectionRepository;
import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class ReviewHandler {

    //todo:getAll -> findAllByUserId
    //todo:post -> post

    private final MongoTemplate mongoTemplate;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final CollectionRepository collectionRepository;

    private final FollowerRepository followerRepository;

    public ReviewHandler(
            MongoTemplate mongoTemplate
            , UserRepository userRepository
            , ReviewRepository reviewRepository
            , CollectionRepository collectionRepository
            , FollowerRepository followerRepository) {

        this.mongoTemplate = mongoTemplate;
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

        Integer page = request.queryParam("page").get().isEmpty() ? 0 : Integer.parseInt(request.queryParam("page").get());
        Integer size = request.queryParam("size").get().isEmpty() ? 5 : Integer.parseInt(request.queryParam("size").get());

        ReviewList reviewList = new ReviewList();
        Pagination pagination = new Pagination();

        String userName = request.principal().map(p -> p.getName()).toString();

        return this.userRepository.findByUsername("test1")
                .flatMap(user -> {
                    LookupOperation lookupOperation = LookupOperation.newLookup()
                            .from("follower")
                            .localField("userId")
                            .foreignField("followerId")
                            .as("follower");

                    Aggregation aggregation = Aggregation.newAggregation(
                            lookupOperation,
                            Aggregation.match(Criteria.where("follower.userId").is(user.getId())),
                            Aggregation.skip(page),
                            Aggregation.limit(size)
                    );

                    List<Review> reviews = mongoTemplate
                            .aggregate(aggregation, "review", Review.class)
                            .getMappedResults();

                    log.debug("]-----] ReviewHandler::reviews [-----[ {}", reviews);

                    return ok().body(BodyInserters.fromObject(reviews));

                }).switchIfEmpty(notFound().build());
    }

    /**
     * GET a Review by ReviewId
     */
    public Mono<ServerResponse> findById(ServerRequest request) {

        String reviewId = request.pathVariable("id");

        ReviewDetail reviewDetail = new ReviewDetail();

        return ServerResponse.ok().build();

        /*return
         *//*request.principal()
                        .flatMap(p -> userRepository.findByUserName(p.getName()))*//*
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

                            return this.collectionRepository.findAllByCollectionId(review.get())
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
                        .switchIfEmpty(notFound().build());*/
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

        Review review = new Review();

        return request
                .body(BodyExtractors.toMultipartData())
                .map(map -> {
                    Map<String, Part> parts = map.toSingleValueMap();

                    review.setTitle(((FormFieldPart) parts.get("title")).value());
                    review.setContent(((FormFieldPart) parts.get("content")).value());

                    //todo: tag배열 받아오기

                    return map;
                })
                .flatMap(user -> request.principal().map(p -> p.getName()))
                //.flatMap(user -> request.principal().map(p -> "ssong"))
                .flatMap(user -> this.userRepository.findByUsername(user))
                .flatMap(user -> {
                    review.setUserId(user.getId());
                    review.setUser(user);
                    return Mono.just(review);
                })
                .flatMap(r -> this.reviewRepository.save(r))
                .flatMap(r -> ok().body(BodyInserters.fromObject(r)))
                .switchIfEmpty(notFound().build());
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
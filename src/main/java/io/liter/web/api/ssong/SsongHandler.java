package io.liter.web.api.ssong;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.like.Like;
import io.liter.web.api.like.LikeRepository;
import io.liter.web.api.review.Review;
import io.liter.web.api.review.ReviewContentType;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public class SsongHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private SsongRepository ssongRepository;
    @Autowired
    private LikeRepository likeRepository;

    public Mono<ServerResponse> get(ServerRequest request) {
        log.info("]-----]SsongHandler :: GET[-----[");

        ReviewList reviewList = new ReviewList();
        Pagination pagination = new Pagination();

        Integer page = 0;
        Integer size = 100;

        return  this.userRepository.findByUsername("test002")
                .map(user -> {
                    reviewList.setUser(user);
                    return user;
                })
                .flatMap(user -> this.followerRepository.findByUserId(user.getId()))
                .flatMap(follower ->
                        this.reviewRepository.findByUserIdIn(follower.getFollowerId(), PageRequest.of(page, size))
                                .collectList()
                                .map(collections -> {
                                    reviewList.setReview(collections);
                                    return follower;
                                }))
                .flatMap(follower -> this.reviewRepository.countByUserIdIn(follower.getFollowerId()))
                .map(count -> {
                    pagination.setTotal(count);
                    pagination.setPage(page);
                    pagination.setSize(size);

                    reviewList.setPagination(pagination);

                    return reviewList;
                })
                .flatMap(r -> ServerResponse.ok().body(BodyInserters.fromObject(r)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getall(ServerRequest request) {

        return this.followerRepository.findById(new ObjectId("5b3a1cdcc650c21d9eb4863b"))
                .map(f -> this.reviewRepository.countByUserIdIn(f.getFollowerId()))
                .flatMap(r -> ServerResponse.ok().body(BodyInserters.fromObject(r)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> testSaveReview(ServerRequest request) {
        log.info("]-----] post [-----[ ");

        return this.userRepository.findByUsername("test001")
                .flatMap(user -> {

                    Review review = new Review();
                    review.setUserId(user.getId());
                    review.setUser(user);

                    review.setTitle(user.getUsername());
                    review.setContent(user.getUsername());

                    return ServerResponse.ok().body(this.reviewRepository.save(review), Review.class);
                })
                .switchIfEmpty(notFound().build());

    }

    public Mono<ServerResponse> formdata(ServerRequest request){

        Review review = new Review();

        return request.body(BodyExtractors.toMultipartData())
                        .map(map -> {
                            Map<String, Part> parts = map.toSingleValueMap();

                            review.setTitle(((FormFieldPart) parts.get("title")).value());
                            review.setContent(((FormFieldPart) parts.get("content")).value());

                            //todo: tag배열 받아오기

                            return ServerResponse.ok().build();
                        })
                .flatMap(r -> ServerResponse.ok().build())
                .switchIfEmpty(notFound().build());

    }

    public Mono<ServerResponse> postLike(ServerRequest request){

        Like like = new Like();

        ObjectId reviewId = new ObjectId(request.pathVariable("id"));

        return this.userRepository.findByUsername("test001")
                .flatMap(user -> {
                    List<ObjectId> likeId = new ArrayList<>();
                    likeId.add(user.getId());

                    like.setLikeId(likeId);

                    like.setReviewId(reviewId);

                    return ServerResponse.ok().body(this.likeRepository.save(like), Like.class);
                })
                .switchIfEmpty(notFound().build());
    }

    /**
     * ================================================================================================================
     */

    /**
     * POST a ReviewHandler Post Sample
     */
    public Mono<ServerResponse> reviewPostSample(ServerRequest request) {
        log.info("]-----] ReviewHandler::post call [-----[ ");

        /*
        Mono<List<FilePart>> imageMono = request.body(BodyExtractors.toParts()).collectList()
                .map(m -> m.stream()
                        .filter(p -> (ReviewContentType.checkCode(p.headers().getContentType()).equals(ReviewContentType.IMAGE.getCode())))
                        .map(p -> ((FilePart) p))
                        .collect(Collectors.toList())
                );

        Mono<List<FormFieldPart>> contentMono = request.body(BodyExtractors.toParts()).collectList()
                .map(m -> m.stream()
                        .filter(p -> (ReviewContentType.checkCode(p.headers().getContentType()).equals(ReviewContentType.CONTENT.getCode())))
                        .map(p -> ((FormFieldPart) p))
                        .collect(Collectors.toList())
                );
        */

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
                .flatMap(r -> ServerResponse.ok().body(BodyInserters.fromObject(r)))
                .switchIfEmpty(notFound().build());
    }
}

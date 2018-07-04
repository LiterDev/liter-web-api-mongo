package io.liter.web.api.review;

import io.liter.web.api.collection.MediaCollectionRepository;
import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.like.LikeRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class ReviewHandler {

    //todo:getAll -> findAllByUserId    OK!
    //todo:getId -> findByReviewId      OK!
    //todo:post -> post                 ING~(tag 추가)
    //todo:delete -> delete
    //todo::getIsActive -> isActive

    private final MongoTemplate mongoTemplate;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final MediaCollectionRepository mediaCollectionRepository;

    private final FollowerRepository followerRepository;

    private final LikeRepository likeRepository;

    public ReviewHandler(
            MongoTemplate mongoTemplate
            , UserRepository userRepository
            , ReviewRepository reviewRepository
            , MediaCollectionRepository mediaCollectionRepository
            , FollowerRepository followerRepository
            , LikeRepository likeRepository
    ) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.mediaCollectionRepository = mediaCollectionRepository;
        this.followerRepository = followerRepository;
        this.likeRepository = likeRepository;
    }

    /**
     * GET All Review by UserId
     */
    public Mono<ServerResponse> findByUserIdIn(ServerRequest request) {
        log.info("]-----] ReviewHandler::getMainList call [-----[ ");

        ReviewList reviewList = new ReviewList();
        Pagination pagination = new Pagination();

        Integer page = request.queryParam("page").get().isEmpty() ? 0 : Integer.parseInt(request.queryParam("page").get());
        Integer size = request.queryParam("size").get().isEmpty() ? 10 : Integer.parseInt(request.queryParam("size").get());

        return request.principal()
                .flatMap(p -> this.userRepository.findByUsername(p.getName()))
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

    /**
     * GET a Review by ReviewId
     */
    public Mono<ServerResponse> findByReviewId(ServerRequest request) {

        ReviewDetail reviewDetail = new ReviewDetail();

        ObjectId reviewId = new ObjectId(request.pathVariable("id"));

        Mono<ServerResponse> responseNoLogin =
                ServerResponse.ok().body(this.reviewRepository.findById(reviewId),Review.class)
                        .switchIfEmpty(notFound().build());

        return request.principal()
                .map(p -> p.getName())
                .flatMap(user -> this.userRepository.findByUsername(user))
                .map(user -> {
                    reviewDetail.setUser(user);

                    return user;
                })
                .flatMap(user -> this.likeRepository.countByReviewIdAndLikeId(reviewId, user.getId()))
                .map(likeCount -> {
                    int likeActive = likeCount > 0 ? 1 : 0 ;
                    reviewDetail.setUserLikeActive(likeActive);

                    return likeCount;
                })
                .flatMap(r -> this.reviewRepository.findById(reviewId))
                .map(review -> {
                    reviewDetail.setReview(review);

                    return reviewDetail;
                })
                .flatMap(r -> ServerResponse.ok().body(BodyInserters.fromObject(r)))
                .switchIfEmpty(responseNoLogin);
    }

    /**
     * POST a Review
     */
    @PreAuthorize("hasAuthority('SCOPE_ACCESS')")
    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] ReviewHandler::post call [-----[ ");

        /**
         * 1. jwt토큰 -> user 정보 가져오기
         * 2. form-data map 형태로 꺼내와서 -> title, content, tag 데이터 Review 테이블에 담기
         * 3. form-data 이미지 저장
         * 4. reviewRepository.save
         */

        Review review = new Review();

        return request.principal()
                .map(p -> p.getName())
                .flatMap(user -> this.userRepository.findByUsername(user))
                .map(user -> {
                    review.setUserId(user.getId());
                    review.setUser(user);

                    return user;
                })
                .flatMap(user -> request.body(BodyExtractors.toMultipartData())
                        .map(map -> {
                            Map<String, Part> parts = map.toSingleValueMap();

                            review.setTitle(((FormFieldPart) parts.get("title")).value());
                            review.setContent(((FormFieldPart) parts.get("content")).value());

                            //todo: tag배열 받아오기

                            return review;
                        }))
                .flatMap(r -> this.reviewRepository.save(r))
                .flatMap(r -> ok().body(BodyInserters.fromObject(r)))
                .switchIfEmpty(notFound().build());
    }

    /**
     * GET a Review reward active
     */
    public Mono<ServerResponse> isActive(ServerRequest request) {

        Mono<Review> reviewMono = reviewRepository.findById(new ObjectId(request.pathVariable("id")));

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
     * PUT a Object
     */
    @PreAuthorize("hasAuthority('SCOPE_ACCESS')")
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
                        this.reviewRepository.findById(new ObjectId(request.pathVariable("id"))),
                        request.bodyToMono(Review.class)
                )
                .cast(Review.class)
                .flatMap((post) -> this.reviewRepository.save(post))
                .flatMap((post) -> noContent().build());
    }

    /**
     * DELETE a Object
     */
    @PreAuthorize("hasAuthority('SCOPE_ACCESS')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] ReviewHandler::delete call [-----[ ");

        return this.reviewRepository.findById(new ObjectId(request.pathVariable("id")))
                .flatMap((post) -> noContent().build())
                .switchIfEmpty(notFound().build());
    }
}
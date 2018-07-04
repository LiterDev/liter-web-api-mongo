package io.liter.web.api.review;

import io.liter.web.api.collection.MediaCollectionRepository;
import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.like.LikeRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.review.view.ReviewList;
import io.liter.web.api.user.User;
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
import java.util.Objects;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class ReviewHandler {

    //todo:getAll   -> findAllByUserId      OK!
    //todo:getId    -> findByReviewId       OK!
    //todo:post     -> post                 ING~(tag 추가)
    //todo:delete   -> delete               OK!
    //todo:put      -> put                  ING~(tag 추가 / Content-Type 프론트랑 맞춰야 함(json? form-data?)

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final MediaCollectionRepository mediaCollectionRepository;

    private final FollowerRepository followerRepository;

    private final LikeRepository likeRepository;

    public ReviewHandler(
            UserRepository userRepository
            , ReviewRepository reviewRepository
            , MediaCollectionRepository mediaCollectionRepository
            , FollowerRepository followerRepository
            , LikeRepository likeRepository
    ) {
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
                .flatMap(count -> {
                    pagination.setTotal(count);
                    pagination.setPage(page);
                    pagination.setSize(size);

                    reviewList.setPagination(pagination);

                    return ok().body(BodyInserters.fromObject(reviewList));
                })
                .switchIfEmpty(notFound().build());
    }

    /**
     * GET a Review by ReviewId
     */
    public Mono<ServerResponse> findByReviewId(ServerRequest request) {

        ReviewDetail reviewDetail = new ReviewDetail();

        ObjectId reviewId = new ObjectId(request.pathVariable("id"));

        Mono<ServerResponse> responseNoLogin =
                ServerResponse.ok().body(this.reviewRepository.findById(reviewId), Review.class)
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
                    int likeActive = likeCount > 0 ? 1 : 0;
                    reviewDetail.setUserLikeActive(likeActive);

                    return likeCount;
                })
                .flatMap(r -> this.reviewRepository.findById(reviewId))
                .flatMap(review -> {
                    reviewDetail.setReview(review);

                    return ok().body(BodyInserters.fromObject(reviewDetail));
                })
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
                .flatMap(r -> ok().body(this.reviewRepository.save(r),Review.class))
                .switchIfEmpty(notFound().build());
    }

    /**
     * DELETE a Review
     */
    @PreAuthorize("hasAuthority('SCOPE_ACCESS')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] ReviewHandler::delete call [-----[ ");

        ObjectId reviewId = new ObjectId(request.pathVariable("id"));

        return request.principal()
                .map(p -> p.getName())
                .flatMap(user -> this.userRepository.findByUsername(user))
                .flatMap(user -> this.reviewRepository.findById(reviewId)
                        .filter(review -> Objects.equals(review.getUserId(), user.getId()))
                        .filter(review -> Objects.equals(review.getRewardActive(), 0)))          //0:보상 안받음
                .flatMap(review -> noContent().build(this.reviewRepository.deleteById(reviewId)))
                .switchIfEmpty(notFound().build());
    }

    /**
     * PUT a Review
     */
    @PreAuthorize("hasAuthority('SCOPE_ACCESS')")
    public Mono<ServerResponse> put(ServerRequest request) {
        log.info("]-----] ReviewHandler::put call [-----[ ");

        ObjectId reviewId = new ObjectId(request.pathVariable("id"));

        return request.principal()
                .map(p -> p.getName())
                .flatMap(user -> this.userRepository.findByUsername(user))
                .map(user -> this.reviewRepository.findById(reviewId)
                        .filter(review -> Objects.equals(review.getUserId(), user.getId()))
                        .filter(review -> Objects.equals(review.getRewardActive(), 0)))         //0:보상 안받음
                .flatMap(review -> Mono
                        .zip(
                                (data) -> {
                                    Review original = (Review) data[0];
                                    Review modified = (Review) data[1];

                                    original.setTitle(modified.getTitle().isEmpty() ? original.getTitle() : modified.getTitle());
                                    original.setContent(modified.getContent().isEmpty() ? original.getContent() : modified.getContent());

                                    return original;
                                }
                                , review
                                , request.bodyToMono(Review.class)
                        ).cast(Review.class))
                .flatMap(review -> ok().body(this.reviewRepository.save(review),Review.class))
                .switchIfEmpty(notFound().build());
    }
}